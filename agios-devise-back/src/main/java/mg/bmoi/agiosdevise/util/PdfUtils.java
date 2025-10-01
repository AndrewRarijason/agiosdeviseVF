package mg.bmoi.agiosdevise.util;

import mg.bmoi.agiosdevise.entity.BkdarTemp;
import mg.bmoi.agiosdevise.repository.BkdarTempRepository;
import mg.bmoi.agiosdevise.service.ArreteCompteService;
import mg.bmoi.agiosdevise.service.ArretesPdfService;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PdfUtils {
    private static Logger log = LoggerFactory.getLogger(PdfUtils.class);
    private final ArreteCompteService arreteCompteService;
    private final BkdarTempRepository bkdarTempRepository;

    public PdfUtils(ArreteCompteService arreteCompteService, BkdarTempRepository bkdarTempRepository) {
        this.arreteCompteService = arreteCompteService;
        this.bkdarTempRepository = bkdarTempRepository;
    }

    public static void addText(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text != null ? text : "");
        contentStream.endText();
    }

    // Affiche le texte sur plusieurs lignes et retourne la position Y pour la prochaine ligne
    public static float writeMultilineText(PDPageContentStream contentStream, String text, float startX, float endX, float startY, float lineHeight) throws IOException {
        if (text == null || text.isEmpty()) return startY;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float currentY = startY;
        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            float width = PDType1Font.COURIER.getStringWidth(testLine) / 1000 * 8;
            if (width > (endX - startX) && line.length() > 0) {
                addText(contentStream, line.toString(), startX, currentY);
                currentY -= lineHeight;
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
        }
        if (line.length() > 0) {
            addText(contentStream, line.toString(), startX, currentY);
            currentY -= lineHeight;
        }
        return currentY;
    }

    public void generatePdfFiles(List<String> ncpList, Date dateDebutParsed, Date dateFinParsed, Path folderPath) {
        for (String ncp : ncpList) {
            Optional<BkdarTemp> bkdarOpt = bkdarTempRepository.findByIdNcp(ncp);
            String age = null, cli = null;
            if (bkdarOpt.isPresent()) {
                age = bkdarOpt.get().getAge();
                cli = bkdarOpt.get().getId().getCli();
            }
            String filename = "AVIS." + (age != null ? age : "") + "." + ncp + "." + (cli != null ? cli : "") + ".pdf";
            Path filePath = folderPath.resolve(filename);

            try {
                byte[] pdf = arreteCompteService.generateAvisAgiosPdf(ncp, dateDebutParsed, dateFinParsed);
                Files.write(filePath, pdf);
            } catch (Exception e) {
                log.warn("Erreur export PDF pour le compte {}: {}", ncp, e.getMessage());
            }
        }
    }

    public void generateEchellePdfFiles(List<String> ncpList, Date dateDebutParsed, Date dateFinParsed, Path folderPath, String datedernierJourOuvre) {
        for (String ncp : ncpList) {
            Optional<BkdarTemp> bkdarOpt = bkdarTempRepository.findByIdNcp(ncp);
            String age = null, cli = null;
            if (bkdarOpt.isPresent()) {
                age = bkdarOpt.get().getAge();
                cli = bkdarOpt.get().getId().getCli();
            }
            String filename = "PDF." + (age != null ? age : "") + "." + ncp + "." + (cli != null ? cli : "") + ".pdf";
            Path filePath = folderPath.resolve(filename);

            try {
                byte[] pdf = arreteCompteService.generateEchelleArretesPdf(ncp, dateDebutParsed, dateFinParsed, datedernierJourOuvre);
                Files.write(filePath, pdf);
            } catch (Exception e) {
                log.warn("Erreur export PDF échelle pour le compte {}: {}", ncp, e.getMessage());
            }
        }
    }
}