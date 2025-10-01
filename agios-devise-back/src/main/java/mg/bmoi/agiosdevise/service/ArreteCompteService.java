package mg.bmoi.agiosdevise.service;

import mg.bmoi.agiosdevise.DTO.HistoriqueArreteDto;
import mg.bmoi.agiosdevise.DTO.SyntheseInteretsDto;
import mg.bmoi.agiosdevise.DTO.TransactionDto;
import mg.bmoi.agiosdevise.entity.BkdarTemp;
import mg.bmoi.agiosdevise.entity.BkhisTemp;
import mg.bmoi.agiosdevise.entity.BksldTemp;
import mg.bmoi.agiosdevise.entity.TicDevise;
import mg.bmoi.agiosdevise.repository.*;

import mg.bmoi.agiosdevise.service.insertion.InsertHistoArreteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArreteCompteService {

    private static final Logger log = LoggerFactory.getLogger(ArreteCompteService.class);
    private final BksldTempRepository bksldTempRepository;
    private final BkdarTempRepository bkdarTempRepository;
    private final BkhisTempRepository bkhisTempRepository;
    private final TicDeviseRepository ticDeviseRepository;
    private final DerogationRepository derogationRepository;
    private final PdfModificationService pdfModificationService;
    private final InsertHistoArreteService insertHistoArreteService;
    private final HistoriqueArreteRepository historiqueArreteRepository;
    private final BkageRepository bkageRepository;

    public ArreteCompteService(BksldTempRepository bksldTempRepository, BkdarTempRepository bkdarTempRepository,
                               BkhisTempRepository bkhisTempRepository, TicDeviseRepository ticDeviseRepository,
                               DerogationRepository derogationRepository, PdfModificationService pdfModificationService,
                               InsertHistoArreteService insertHistoArreteService, HistoriqueArreteRepository historiqueArreteRepository,
                               BkageRepository bkageRepository) {
        this.bksldTempRepository = bksldTempRepository;
        this.bkdarTempRepository = bkdarTempRepository;
        this.bkhisTempRepository = bkhisTempRepository;
        this.ticDeviseRepository = ticDeviseRepository;
        this.derogationRepository = derogationRepository;
        this.pdfModificationService = pdfModificationService;
        this.insertHistoArreteService = insertHistoArreteService;
        this.historiqueArreteRepository = historiqueArreteRepository;
        this.bkageRepository = bkageRepository;
    }

    public List<String> getAllComptesInTmpBkdar() {
        log.info("Appel de getAllComptesInTmpBkdar()");
        List<String> comptes = bkdarTempRepository.findAll()
                .stream()
                .map(bkdarTemp -> bkdarTemp.getId().getNcp())
                .distinct()
                .collect(Collectors.toList());
        log.info("Comptes distincts trouvés : {}", comptes.size());
        log.debug("Valeurs des comptes : {}", comptes);
        return comptes;
    }


    public BigDecimal getSoldeDebutArrete(String ncp, Date dateFinArrete) {
        log.info("Appel de getSoldeDebutArrete() pour ncp={}", ncp);
        BigDecimal solde = bkdarTempRepository.findByIdNcp(ncp)
                .map(BkdarTemp::getSolde)
                .orElse(BigDecimal.ZERO);
        log.info("Solde début arrêté pour {} : {}", ncp, solde);
        return solde;
    }

    public Optional<BkhisTemp> getTransactionForCompte(String ncp, Date dateTransaction) {
        log.info("Appel de getTransactionForCompte() pour ncp={}, dateTransaction={}", ncp, dateTransaction);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTransaction);
        cal.add(Calendar.DATE, -1);
        Date dateMoinsUn = cal.getTime();

        Optional<BkhisTemp> tx = bkhisTempRepository.findByIdNcpAndIdDvaAfterOrderByIdDvaAsc(ncp, dateMoinsUn).stream()
                .filter(t -> dateTransaction.equals(t.getId().getDva()))
                .findFirst();
        log.info("Transaction trouvée pour {} à {} : {}", ncp, dateTransaction, tx.isPresent());
        return tx;
    }


    public long calculNbJours(Date date1, Date date2) {
        log.info("Appel de calculNbJours() entre {} et {}", date1, date2);
        if (date1 == null || date2 == null) return 0;
        java.time.LocalDate d1;
        java.time.LocalDate d2;
        if (date1 instanceof java.sql.Date) {
            d1 = ((java.sql.Date) date1).toLocalDate();
        } else {
            d1 = date1.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }
        if (date2 instanceof java.sql.Date) {
            d2 = ((java.sql.Date) date2).toLocalDate();
        } else {
            d2 = date2.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }
        return java.time.temporal.ChronoUnit.DAYS.between(d1, d2);
    }

    // Java
    public Map<String, Map<String, Object>> gestionTransactionEtSyntheseParComptePourUnCompte(String ncp, Date dateDebutArrete, Date dateFinArrete) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        List<TransactionDto> transactions = new ArrayList<>();
        BigDecimal soldeCourant = getSoldeDebutArrete(ncp, dateDebutArrete);

        List<BkhisTemp> txs = bkhisTempRepository.findByIdNcpAndIdDvaAfterOrderByIdDvaAsc(ncp, dateDebutArrete);

        Date dateProchainMouvement = txs.isEmpty() ? dateFinArrete : txs.get(0).getId().getDva();
        long nbJours = calculNbJours(dateDebutArrete, dateProchainMouvement);
        BigDecimal nbDebiteur = soldeCourant.signum() < 0 ? soldeCourant.multiply(BigDecimal.valueOf(nbJours)) : BigDecimal.ZERO;
        BigDecimal nbCrediteur = soldeCourant.signum() > 0 ? soldeCourant.multiply(BigDecimal.valueOf(nbJours)) : BigDecimal.ZERO;
        transactions.add(new TransactionDto(ncp, BigDecimal.ZERO, dateDebutArrete, null, soldeCourant, (int) nbJours, nbDebiteur, nbCrediteur));

        for (int i = 0; i < txs.size(); i++) {
            BkhisTemp tx = txs.get(i);
            Date dateMouvement = tx.getId().getDva();

            Character sen = tx.getId().getSen();
            BigDecimal montant = tx.getMon();
            if (sen != null && montant != null && 'D' == sen) {
                montant = montant.negate();
            }
            soldeCourant = soldeCourant.add(montant != null ? montant : BigDecimal.ZERO);

            Date dateProchain = (i + 1 < txs.size()) ? txs.get(i + 1).getId().getDva() : dateFinArrete;
            nbJours = calculNbJours(dateMouvement, dateProchain);
            if (i + 1 == txs.size()) {
                nbJours += 1;
            }
            nbDebiteur = soldeCourant.signum() < 0 ? soldeCourant.multiply(BigDecimal.valueOf(nbJours)) : BigDecimal.ZERO;
            nbCrediteur = soldeCourant.signum() > 0 ? soldeCourant.multiply(BigDecimal.valueOf(nbJours)) : BigDecimal.ZERO;

            transactions.add(new TransactionDto(ncp, montant, dateMouvement, sen, soldeCourant, (int) nbJours, nbDebiteur, nbCrediteur));
        }

        int totalNbJours = transactions.stream().mapToInt(TransactionDto::getNbJoursInactif).sum();
        BigDecimal totalNbDebiteur = transactions.stream()
                .map(TransactionDto::getNbDebiteur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalNbCrediteur = transactions.stream()
                .map(TransactionDto::getNbCrediteur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal soldeFinal = transactions.isEmpty() ? BigDecimal.ZERO : transactions.get(transactions.size() - 1).getSoldeDepart();

        transactions.add(new TransactionDto(
                ncp,
                null,
                dateFinArrete,
                null,
                soldeFinal,
                totalNbJours,
                totalNbDebiteur,
                totalNbCrediteur
        ));

        BksldTemp bksld = bksldTempRepository.findByNcp(ncp).orElse(null);
        BkdarTemp bkdar = bkdarTempRepository.findByIdNcp(ncp).orElse(null);

        BigDecimal soldeFinalBase = bksld != null ? bksld.getSde() : BigDecimal.ZERO;
        String codeDevise = bksld != null ? bksld.getDev() : null;

        SyntheseInteretsDto synthese = calculSyntheseInterets(transactions, codeDevise, ncp, ticDeviseRepository);

        BigDecimal sumMvtCredit = transactions.stream()
                .filter(tx -> "C".equals(tx.getSen() != null ? tx.getSen().toString() : null) && tx.getMon() != null)
                .map(TransactionDto::getMon)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumMvtDebit = transactions.stream()
                .filter(tx -> "D".equals(tx.getSen() != null ? tx.getSen().toString() : null) && tx.getMon() != null)
                .map(TransactionDto::getMon)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumSoldeNegatif = transactions.stream()
                .filter(tx -> tx.getSoldeDepart() != null && tx.getSoldeDepart().compareTo(BigDecimal.ZERO) < 0)
                .map(TransactionDto::getSoldeDepart)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumSoldePositif = transactions.stream()
                .filter(tx -> tx.getSoldeDepart() != null && tx.getSoldeDepart().compareTo(BigDecimal.ZERO) > 0)
                .map(TransactionDto::getSoldeDepart)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String resultat = soldeFinal.compareTo(soldeFinalBase) == 0 ? "Solde OK" : "Solde NOK";

        List<Map<String, Object>> transactionsMap = convertTransactionsToMap(transactions);

        Map<String, Object> compteData = new LinkedHashMap<>();
        compteData.put("age", bkdar != null ? bkdar.getAge() : null);
        compteData.put("dev", bksld != null ? bksld.getDev() : null);
        compteData.put("nomrest", bkdar != null ? bkdar.getNomrest() : null);
        compteData.put("dateDebutArrete", dateDebutArrete);
        compteData.put("dateFinArrete", dateFinArrete);
        compteData.put("transactions", transactionsMap);
        compteData.put("transactionsDto", transactions);
        compteData.put("synthese", synthese);
        compteData.put("soldeFinalCalcule", soldeFinal);
        compteData.put("soldeFinalBase", soldeFinalBase);
        compteData.put("resultat", resultat);
        compteData.put("sumMvtCredit", sumMvtCredit);
        compteData.put("sumMvtDebit", sumMvtDebit);
        compteData.put("sumSoldeNegatif", sumSoldeNegatif);
        compteData.put("sumSoldePositif", sumSoldePositif);

        result.put(ncp, compteData);

        HistoriqueArreteDto historiqueArreteDto = new HistoriqueArreteDto(
                (String) compteData.get("age"),
                (String) compteData.get("dev"),
                ncp,
                (String) compteData.get("nomrest"),
                (BigDecimal) compteData.get("soldeFinalCalcule"),
                (Date) compteData.get("dateDebutArrete"),
                (Date) compteData.get("dateFinArrete"),
                synthese.getSumMvtCrediteur(),
                synthese.getTauxCrediteur(),
                synthese.getInteretCrediteur(),
                synthese.getSumMvtDebiteur(),
                synthese.getTauxDebiteur(),
                synthese.getInteretDebiteur(),
                synthese.getNetAgios(),
                "Solde NOK".equals(resultat),
                bkdar != null ? bkdar.getTcli() : null
        );

        insertHistoArreteService.insertHistoriqueArrete(historiqueArreteDto);

        return result;
    }





    public Map<String, Map<String, Object>> gestionTransactionEtSyntheseParCompte(Date dateDebutArrete, Date dateFinArrete) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        List<String> comptes = getAllComptesInTmpBkdar();

        for (String ncp : comptes) {
            List<TransactionDto> transactions = new ArrayList<>();
            BigDecimal soldeCourant = getSoldeDebutArrete(ncp, dateDebutArrete);

            List<BkhisTemp> txs = bkhisTempRepository.findByIdNcpAndIdDvaAfterOrderByIdDvaAsc(ncp, dateDebutArrete);

            // Première ligne
            Date dateProchainMouvement = txs.isEmpty() ? dateFinArrete : txs.get(0).getId().getDva();
            long nbJours = calculNbJours(dateDebutArrete, dateProchainMouvement);
            BigDecimal nbDebiteur = soldeCourant.signum() < 0 ? soldeCourant.multiply(BigDecimal.valueOf(nbJours)) : BigDecimal.ZERO;
            BigDecimal nbCrediteur = soldeCourant.signum() > 0 ? soldeCourant.multiply(BigDecimal.valueOf(nbJours)) : BigDecimal.ZERO;
            transactions.add(new TransactionDto(ncp, BigDecimal.ZERO, dateDebutArrete, null, soldeCourant, (int) nbJours, nbDebiteur, nbCrediteur));

            for (int i = 0; i < txs.size(); i++) {
                BkhisTemp tx = txs.get(i);
                Date dateMouvement = tx.getId().getDva();

                Character sen = tx.getId().getSen();
                BigDecimal montant = tx.getMon();
                if (sen != null && montant != null && 'D' == sen) {
                    montant = montant.negate();
                }
                soldeCourant = soldeCourant.add(montant != null ? montant : BigDecimal.ZERO);

                Date dateProchain = (i + 1 < txs.size()) ? txs.get(i + 1).getId().getDva() : dateFinArrete;
                nbJours = calculNbJours(dateMouvement, dateProchain);
                if (i + 1 == txs.size()) {
                    nbJours += 1;
                }
                nbDebiteur = soldeCourant.signum() < 0 ? soldeCourant.multiply(BigDecimal.valueOf(nbJours)) : BigDecimal.ZERO;
                nbCrediteur = soldeCourant.signum() > 0 ? soldeCourant.multiply(BigDecimal.valueOf(nbJours)) : BigDecimal.ZERO;

                transactions.add(new TransactionDto(ncp, montant, dateMouvement, sen, soldeCourant, (int) nbJours, nbDebiteur, nbCrediteur));
            }

            // Calcul des totaux une seule fois
            int totalNbJours = transactions.stream().mapToInt(TransactionDto::getNbJoursInactif).sum();
            BigDecimal totalNbDebiteur = transactions.stream()
                    .map(TransactionDto::getNbDebiteur)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalNbCrediteur = transactions.stream()
                    .map(TransactionDto::getNbCrediteur)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal soldeFinal = transactions.isEmpty() ? BigDecimal.ZERO : transactions.get(transactions.size() - 1).getSoldeDepart();

            transactions.add(new TransactionDto(
                    ncp,
                    null,
                    dateFinArrete,
                    null,
                    soldeFinal,
                    totalNbJours,
                    totalNbDebiteur,
                    totalNbCrediteur
            ));

            // Récupération unique des entités
            BksldTemp bksld = bksldTempRepository.findByNcp(ncp).orElse(null);
            BkdarTemp bkdar = bkdarTempRepository.findByIdNcp(ncp).orElse(null);

            BigDecimal soldeFinalBase = bksld != null ? bksld.getSde() : BigDecimal.ZERO;
            String codeDevise = bksld != null ? bksld.getDev() : null;

            SyntheseInteretsDto synthese = calculSyntheseInterets(transactions, codeDevise, ncp, ticDeviseRepository);

            // Calcul des sommes une seule fois
            BigDecimal sumMvtCredit = transactions.stream()
                    .filter(tx -> "C".equals(tx.getSen() != null ? tx.getSen().toString() : null) && tx.getMon() != null)
                    .map(TransactionDto::getMon)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal sumMvtDebit = transactions.stream()
                    .filter(tx -> "D".equals(tx.getSen() != null ? tx.getSen().toString() : null) && tx.getMon() != null)
                    .map(TransactionDto::getMon)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal sumSoldeNegatif = transactions.stream()
                    .filter(tx -> tx.getSoldeDepart() != null && tx.getSoldeDepart().compareTo(BigDecimal.ZERO) < 0)
                    .map(TransactionDto::getSoldeDepart)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal sumSoldePositif = transactions.stream()
                    .filter(tx -> tx.getSoldeDepart() != null && tx.getSoldeDepart().compareTo(BigDecimal.ZERO) > 0)
                    .map(TransactionDto::getSoldeDepart)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String resultat = soldeFinal.compareTo(soldeFinalBase) == 0 ? "Solde OK" : "Solde NOK";

            // Conversion des transactions en Map via une méthode utilitaire
            List<Map<String, Object>> transactionsMap = convertTransactionsToMap(transactions);

            Map<String, Object> compteData = new LinkedHashMap<>();
            compteData.put("age", bkdar != null ? bkdar.getAge() : null);
            compteData.put("dev", bksld != null ? bksld.getDev() : null);
            compteData.put("nomrest", bkdar != null ? bkdar.getNomrest() : null);
            compteData.put("dateDebutArrete", dateDebutArrete);
            compteData.put("dateFinArrete", dateFinArrete);
            compteData.put("transactions", transactionsMap);
            compteData.put("transactionsDto", transactions);
            compteData.put("synthese", synthese);
            compteData.put("soldeFinalCalcule", soldeFinal);
            compteData.put("soldeFinalBase", soldeFinalBase);
            compteData.put("resultat", resultat);
            compteData.put("sumMvtCredit", sumMvtCredit);
            compteData.put("sumMvtDebit", sumMvtDebit);
            compteData.put("sumSoldeNegatif", sumSoldeNegatif);
            compteData.put("sumSoldePositif", sumSoldePositif);

            result.put(ncp, compteData);

            // Création du DTO
            HistoriqueArreteDto historiqueArreteDto = new HistoriqueArreteDto(
                    (String) compteData.get("age"),
                    (String) compteData.get("dev"),
                    ncp,
                    (String) compteData.get("nomrest"),
                    (BigDecimal) compteData.get("soldeFinalCalcule"),
                    (Date) compteData.get("dateDebutArrete"),
                    (Date) compteData.get("dateFinArrete"),
                    synthese.getSumMvtCrediteur(),
                    synthese.getTauxCrediteur(),
                    synthese.getInteretCrediteur(),
                    synthese.getSumMvtDebiteur(),
                    synthese.getTauxDebiteur(),
                    synthese.getInteretDebiteur(),
                    synthese.getNetAgios(),
                    "Solde NOK".equals(resultat),
                    bkdar != null ? bkdar.getTcli() : null
            );

            insertHistoArreteService.insertHistoriqueArrete(historiqueArreteDto);
        }
        return result;
    }

    // Méthode utilitaire pour convertir la liste de TransactionDto en List<Map<String, Object>>
    private List<Map<String, Object>> convertTransactionsToMap(List<TransactionDto> transactions) {
        List<Map<String, Object>> transactionsMap = new ArrayList<>();
        for (int i = 0; i < transactions.size(); i++) {
            TransactionDto tx = transactions.get(i);
            Map<String, Object> txMap = new LinkedHashMap<>();
            txMap.put("ncp", tx.getNcp());
            txMap.put("mon", tx.getMon());
            txMap.put("dva", tx.getDva());
            txMap.put("sen", tx.getSen());
            txMap.put("soldeDepart", tx.getSoldeDepart());
            txMap.put("nbJoursInactif", tx.getNbJoursInactif());
            if (i == transactions.size() - 1) {
                txMap.put("totalNbDebiteur", tx.getNbDebiteur());
                txMap.put("totalNbCrediteur", tx.getNbCrediteur());
            } else {
                txMap.put("nbDebiteur", tx.getNbDebiteur());
                txMap.put("nbCrediteur", tx.getNbCrediteur());
            }
            transactionsMap.add(txMap);
        }
        return transactionsMap;
    }



    public SyntheseInteretsDto calculSyntheseInterets(List<TransactionDto> transactions, String codeDevise,
                                                      String ncp, TicDeviseRepository ticDeviseRepository) {
        BigDecimal tauxCrediteur;
        boolean isDerogation = derogationRepository.findById(ncp)
                .filter(d -> codeDevise.equals(d.getDev()))
                .isPresent();
        if (isDerogation) {
            tauxCrediteur = BigDecimal.ZERO;
        } else {
            tauxCrediteur = ticDeviseRepository.findByCdIso(codeDevise)
                    .map(TicDevise::getValeur)
                    .orElse(BigDecimal.ZERO);
        }

        // Récupération du taux débiteur depuis BKDAR_D
        List<BigDecimal> tauxList = bkdarTempRepository.findTauxListByNcpAndDev(ncp, codeDevise);
        BigDecimal tauxDebiteur = tauxList.isEmpty() ? BigDecimal.ZERO : tauxList.get(0);



        TransactionDto syntheseTx = transactions.get(transactions.size() - 1);
        BigDecimal totalNbDebiteur = syntheseTx.getNbDebiteur();
        BigDecimal totalNbCrediteur = syntheseTx.getNbCrediteur();

        log.info("Calcul intérêts : totalNbDebiteur={}, tauxDebiteur={}, totalNbCrediteur={}, tauxCrediteur={}",
                totalNbDebiteur, tauxDebiteur, totalNbCrediteur, tauxCrediteur);

        BigDecimal interetCrediteur = totalNbCrediteur.multiply(tauxCrediteur)
                .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)
                .divide(new BigDecimal("360"), 8, RoundingMode.HALF_UP);

        BigDecimal interetDebiteur = totalNbDebiteur.multiply(tauxDebiteur)
                .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)
                .divide(new BigDecimal("360"), 8, RoundingMode.HALF_UP);

        String ircm = "";
        String tauxIrcm = "";

        BigDecimal netAgios = interetCrediteur.add(interetDebiteur).setScale(2, RoundingMode.HALF_UP);

        // Ajout de la condition pour la devise "392" Yen japonais
        if ("392".equals(codeDevise)) {
            netAgios = netAgios.setScale(0, RoundingMode.HALF_UP);
        }

        // Calcul des sommes des mouvements débit et crédit
        BigDecimal sommeMouvementDebit = transactions.stream()
                .filter(tx -> tx.getMon() != null && tx.getMon().compareTo(BigDecimal.ZERO) < 0)
                .map(tx -> tx.getMon().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sommeMouvementCredit = transactions.stream()
                .filter(tx -> tx.getMon() != null && tx.getMon().compareTo(BigDecimal.ZERO) > 0)
                .map(TransactionDto::getMon)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Total nombre de jours (exclure la ligne de synthèse qui est la dernière)
        int totalNbJours = transactions.stream()
                .limit(transactions.size() - 1) // Exclure la dernière ligne (synthèse)
                .mapToInt(TransactionDto::getNbJoursInactif)
                .sum();

        return new SyntheseInteretsDto(
                interetCrediteur, tauxCrediteur,
                interetDebiteur, tauxDebiteur,
                ircm, tauxIrcm,
                netAgios,
                sommeMouvementDebit,
                sommeMouvementCredit,
                totalNbJours
        );
    }


    public byte[] generateAvisAgiosPdf(String ncp, Date dateDebut, Date dateFin) throws Exception {
        try {
            log.info("Début génération PDF pour le compte: {}", ncp);

            Optional<BkdarTemp> bkdarOpt = bkdarTempRepository.findByIdNcp(ncp);

            if (!bkdarOpt.isPresent()) {
                log.error("Compte {} non trouvé", ncp);
                throw new IllegalArgumentException("Compte non trouvé");
            }

            BkdarTemp bkdar = bkdarOpt.get();
            BksldTemp bksld = bksldTempRepository.findByNcp(ncp)
                    .orElseThrow(() -> {
                        log.error("Solde non trouvé pour le compte {}", ncp);
                        return new IllegalArgumentException("Solde non trouvé");
                    });


            // Récupérer l'objet HistoriqueArrete en base
            Optional<mg.bmoi.agiosdevise.entity.HistoriqueArrete> historiqueOpt =
                    historiqueArreteRepository.findByIdNcpAndIdDateDebutArreteAndIdDateFinArrete(ncp, dateDebut, dateFin);

            if (!historiqueOpt.isPresent()) {
                log.error("Historique arrêté non trouvé pour le compte {}", ncp);
                throw new IllegalArgumentException("Historique arrêté non trouvé");
            }

            mg.bmoi.agiosdevise.entity.HistoriqueArrete historique = historiqueOpt.get();

            log.info("Génération PDF pour compte {} - agence {}", ncp, bkdar.getAge());
            return pdfModificationService.generateAvisAgiosFromPdfTemplate(bkdar, bksld, historique, dateDebut, dateFin);

        } catch (Exception e) {
            log.error("Erreur lors de la génération du PDF pour le compte " + ncp, e);
            throw e;
        }
    }

    public byte[] generateEchelleArretesPdf(String ncp, Date dateDebut, Date dateFin, String datedernierJourOuvre) throws Exception {
        try {
            log.info("Début génération PDF échelle arrêté pour le compte: {}", ncp);

            Optional<BkdarTemp> bkdarOpt = bkdarTempRepository.findByIdNcp(ncp);
            if (!bkdarOpt.isPresent()) {
                log.error("Compte {} non trouvé", ncp);
                throw new IllegalArgumentException("Compte non trouvé");
            }
            BkdarTemp bkdar = bkdarOpt.get();

            Map<String, Map<String, Object>> compteData = gestionTransactionEtSyntheseParComptePourUnCompte(ncp, dateDebut, dateFin);
            Map<String, Object> data = compteData.get(ncp);

            if (data == null) {
                log.error("Aucune donnée trouvée pour le compte {}", ncp);
                throw new IllegalArgumentException("Aucune donnée trouvée");
            }

            SyntheseInteretsDto synthese = (SyntheseInteretsDto) data.get("synthese");
            List<TransactionDto> transactions = (List<TransactionDto>) data.get("transactionsDto");

            ArretesPdfService arretesPdfService = new ArretesPdfService(ticDeviseRepository, bkageRepository);
            return arretesPdfService.generateArretesPdf(dateDebut, dateFin, synthese, transactions, bkdar, datedernierJourOuvre);

        } catch (Exception e) {
            log.error("Erreur lors de la génération du PDF échelle arrêté pour le compte " + ncp, e);
            throw e;
        }
    }


    public List<HistoriqueArreteDto> getHistoriqueArretesByPeriode(Date dateDebut, Date dateFin) {
        return historiqueArreteRepository
                .findAllByIdDateDebutArreteAndIdDateFinArrete(dateDebut, dateFin)
                .stream()
                .map(entity -> new HistoriqueArreteDto(
                        entity.getAge(),
                        entity.getDev(),
                        entity.getId().getNcp(),
                        entity.getNomrest(),
                        entity.getSoldeFinalCalcule(),
                        entity.getId().getDateDebutArrete(),
                        entity.getId().getDateFinArrete(),
                        entity.getSumMvtCrediteur(),
                        entity.getTauxCrediteur(),
                        entity.getInteretCrediteur(),
                        entity.getSumMvtDebiteur(),
                        entity.getTauxDebiteur(),
                        entity.getInteretDebiteur(),
                        entity.getNetAgios() != null ? entity.getNetAgios().abs() : null,
                        entity.getKo(),
                        entity.getTcli()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    public HistoriqueArreteDto getHistoriqueArreteById(String ncp, Date dateDebutArrete, Date dateFinArrete) {
        return historiqueArreteRepository
                .findByIdNcpAndIdDateDebutArreteAndIdDateFinArrete(ncp, dateDebutArrete, dateFinArrete)
                .map(entity -> new HistoriqueArreteDto(
                        entity.getAge(),
                        entity.getDev(),
                        entity.getId().getNcp(),
                        entity.getNomrest(),
                        entity.getSoldeFinalCalcule(),
                        entity.getId().getDateDebutArrete(),
                        entity.getId().getDateFinArrete(),
                        entity.getSumMvtCrediteur(),
                        entity.getTauxCrediteur(),
                        entity.getInteretCrediteur(),
                        entity.getSumMvtDebiteur(),
                        entity.getTauxDebiteur(),
                        entity.getInteretDebiteur(),
                        entity.getNetAgios() != null ? entity.getNetAgios().abs() : null,
                        entity.getKo(),
                        entity.getTcli()
                ))
                .orElse(null);
    }
}