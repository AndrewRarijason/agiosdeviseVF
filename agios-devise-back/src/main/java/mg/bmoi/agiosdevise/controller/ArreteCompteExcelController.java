package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.DTO.SyntheseInteretsDto;
import mg.bmoi.agiosdevise.entity.BkdarTemp;
import mg.bmoi.agiosdevise.entity.DashboardArrete;
import mg.bmoi.agiosdevise.repository.BkdarTempRepository;
import mg.bmoi.agiosdevise.repository.DashboardArreteRepository;
import mg.bmoi.agiosdevise.service.ArreteCompteService;
import mg.bmoi.agiosdevise.service.ArreteExportService;
import mg.bmoi.agiosdevise.service.ExportProgressService;
import mg.bmoi.agiosdevise.service.ExportProgressService;
import mg.bmoi.agiosdevise.util.PdfUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import mg.bmoi.agiosdevise.util.DateFormatUtil;

import mg.bmoi.agiosdevise.util.FolderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/arrete-compte")
public class ArreteCompteExcelController {

    private static final Logger log = LoggerFactory.getLogger(ArreteCompteExcelController.class);
    private final ArreteCompteService arreteCompteService;
    private final ArreteExportService arreteExportService;
    private final BkdarTempRepository bkdarTempRepository;
    private final DashboardArreteRepository dashboardArreteRepository;
    private final ExportProgressService exportProgressService;

    public ArreteCompteExcelController(ArreteCompteService arreteCompteService,
                                       ArreteExportService arreteExportService,
                                       BkdarTempRepository bkdarTempRepository,
                                       DashboardArreteRepository dashboardArreteRepository,
                                       ExportProgressService exportProgressService) {
        this.arreteCompteService = arreteCompteService;
        this.arreteExportService = arreteExportService;
        this.bkdarTempRepository = bkdarTempRepository;
        this.dashboardArreteRepository = dashboardArreteRepository;
        this.exportProgressService = exportProgressService;
    }

    private Date parseDate(String dateStr) {
        String normalized = DateFormatUtil.normalizeToIsoDate(dateStr);
        LocalDate localDate = LocalDate.parse(normalized, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return java.sql.Date.valueOf(localDate);
    }

    // Ajoutez cette méthode pour le SSE (Server-Sent Events)
    @GetMapping("/transactions/excel/progress")
    public SseEmitter getExportProgress() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // Envoyer l'état initial
        try {
            emitter.send(exportProgressService.getProgress());
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        // Planifier des mises à jour périodiques
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                Map<String, Object> progress = exportProgressService.getProgress();
                emitter.send(progress);

                if ("Terminé".equals(progress.get("status")) ||
                        progress.get("status").toString().startsWith("Erreur")) {
                    emitter.complete();
                    scheduler.shutdown();
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
                scheduler.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);

        emitter.onCompletion(() -> {
            future.cancel(true);
            scheduler.shutdown();
        });

        emitter.onTimeout(() -> {
            future.cancel(true);
            scheduler.shutdown();
        });

        return emitter;
    }


    @GetMapping("/transactions/excel")
    public ResponseEntity<Map<String, Object>> exportTransactionsExcel(
            @RequestParam("dateDebutArrete") String dateDebutArrete,
            @RequestParam("dateFinArrete") String dateFinArrete,
            @RequestParam("dateDernierJouvre") String dateDernierJouvre
    ) throws Exception {
        LocalDateTime dateHeureDebutCalcul = LocalDateTime.now();
        Map<String, Object> response = new HashMap<>();
        response.put("date_heure_debut", dateHeureDebutCalcul.toString());

        Date date = parseDate(dateDebutArrete);
        Date dateFin = parseDate(dateFinArrete);

        List<String> comptes = arreteCompteService.getAllComptesInTmpBkdar();
        Path folderPathExcel = FolderUtil.createAndCleanFolder("ARRETES EXCEL");
        Path folderPathPdf = FolderUtil.createAndCleanFolder("ECHELLE ARRETES PDF");

        Map<String, Map<String, Object>> data = new HashMap<>();
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (String ncp : comptes) {
            executor.submit(() -> {
                try {
                    // Calcul arrêté pour ce compte
                    Map<String, Map<String, Object>> compteData = arreteCompteService.gestionTransactionEtSyntheseParComptePourUnCompte(ncp, date, dateFin);
                    Map<String, Object> singleData = compteData.get(ncp);
                    synchronized (data) {
                        data.put(ncp, singleData);
                    }

                    // Génération Excel
                    Map<String, Map<String, Object>> singleAccountData = new HashMap<>();
                    singleAccountData.put(ncp, singleData);
                    byte[] excelBytes = arreteExportService.exportArreteComptes(singleAccountData);

                    String cli = null;
                    if (singleData != null && singleData.get("age") != null) {
                        Optional<BkdarTemp> bkdarOpt = bkdarTempRepository.findByIdNcp(ncp);
                        if (bkdarOpt.isPresent()) {
                            cli = bkdarOpt.get().getId().getCli();
                        }
                    }
                    cli = cli != null ? cli.trim().replaceAll("\\s+", "") : "";
                    String dev = (String) singleData.get("dev");
                    String fileName = cli + "." + ncp + "." + (dev != null ? dev : "") + ".xlsx";
                    Path filePath = folderPathExcel.resolve(fileName);
                    Files.write(filePath, excelBytes);

                    // Génération PDF
                    PdfUtils pdfUtils = new PdfUtils(arreteCompteService, bkdarTempRepository);
                    pdfUtils.generateEchellePdfFiles(Collections.singletonList(ncp), date, dateFin, folderPathPdf, dateDernierJouvre);

                } catch (Exception e) {
                    exceptions.add(e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.MINUTES);

        long nbKo = data.values().stream()
                .filter(map -> "Solde NOK".equals(map.get("resultat")))
                .count();
        long nbTotal = data.size();

        LocalDateTime end = LocalDateTime.now();
        response.put("date_heure_fin", end.toString());
        response.put("nombre_compte_KO", nbKo);
        response.put("nbtotal_comptes_traites", nbTotal);
        response.put("ratio_KO", nbKo + "/" + nbTotal);
        response.put("path_dossier_excel", folderPathExcel.toAbsolutePath().toString());
        response.put("path_dossier_pdf", folderPathPdf.toAbsolutePath().toString());

        // Dashboard
        DashboardArrete existing = dashboardArreteRepository.findByPeriode(date, dateFin)
                .stream().findFirst().orElse(null);

        if (existing != null) {
            existing.setNbTotalComptes((int) nbTotal);
            existing.setNbCompteKo((int) nbKo);
            existing.setDateHeureDebutCalcul(dateHeureDebutCalcul);
            existing.setDateHeureFinCalcul(LocalDateTime.now());
            dashboardArreteRepository.save(existing);
        } else {
            DashboardArrete dashboard = new DashboardArrete();
            dashboard.setNbTotalComptes((int) nbTotal);
            dashboard.setNbCompteKo((int) nbKo);
            dashboard.setDateDebuArrete(date);
            dashboard.setDateFinArrete(dateFin);
            dashboard.setDateHeureDebutCalcul(dateHeureDebutCalcul);
            dashboard.setDateHeureFinCalcul(LocalDateTime.now());
            dashboardArreteRepository.save(dashboard);
        }

        if (!exceptions.isEmpty()) {
            throw new Exception("Erreur(s) lors du traitement: " + exceptions);
        }

        return ResponseEntity.ok(response);
    }

}