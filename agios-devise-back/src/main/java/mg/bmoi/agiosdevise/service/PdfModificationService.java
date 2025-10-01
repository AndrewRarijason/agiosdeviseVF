package mg.bmoi.agiosdevise.service;

import static mg.bmoi.agiosdevise.util.PdfUtils.addText;
import static mg.bmoi.agiosdevise.util.PdfUtils.writeMultilineText;

import mg.bmoi.agiosdevise.DTO.TransactionDto;
import mg.bmoi.agiosdevise.entity.*;
import mg.bmoi.agiosdevise.DTO.SyntheseInteretsDto;
import mg.bmoi.agiosdevise.repository.BkageRepository;
import mg.bmoi.agiosdevise.repository.TicDeviseRepository;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class PdfModificationService {
    private static final Logger log = LoggerFactory.getLogger(PdfModificationService.class);
    private final BkageRepository bkageRepository;
    private final TicDeviseRepository ticDeviseRepository;

    public PdfModificationService(BkageRepository bkageRepository, TicDeviseRepository ticDeviseRepository) {
        this.bkageRepository = bkageRepository;
        this.ticDeviseRepository = ticDeviseRepository;
    }


    public byte[] generateAvisAgiosFromPdfTemplate(
            BkdarTemp bkdar,
            BksldTemp bksld,
            HistoriqueArrete historique,
            Date dateDebut,
            Date dateFin
    ) throws IOException {
        Resource resource = new ClassPathResource("templates/Template-vierge.pdf");

        if (!resource.exists()) {
            log.error("Template PDF non trouvé");
            throw new FileNotFoundException("Template PDF non trouvé dans: templates/Template-vierge.pdf");
        }

        try (InputStream is = resource.getInputStream();
             PDDocument document = PDDocument.load(is)) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateDebutStr = sdf.format(dateDebut);
            String dateFinStr = sdf.format(dateFin);

            log.info("Modification du PDF...");
            for (PDPage page : document.getPages()) {
                float pageHeight = page.getMediaBox().getHeight();

                try (PDPageContentStream contentStream = new PDPageContentStream(
                        document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    contentStream.setFont(PDType1Font.COURIER, 8);

                    // Infos compte
                    String valDevise = ticDeviseRepository.findByCdIso(bkdar.getId().getDev())
                            .map(TicDevise::getCdAlpha)
                            .orElse("");
                    String infoText = bkdar.getId().getNcp() + "-" + bkdar.getClc() + " " + valDevise;
                    addText(contentStream, infoText, 166, pageHeight - 82);

                    // Info agence
                    String libelle = bkageRepository.findById(bkdar.getAge())
                            .map(Bkage::getLib)
                            .orElse("");
                    String agenceText = bkdar.getAge() + " - " + libelle;
                    addText(contentStream, agenceText, 166, pageHeight - 110);

                    // Date de l'avis
                    SimpleDateFormat sdfrench = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateFin);
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    String dateAvis = sdfrench.format(cal.getTime()).replace(".", "");
                    addText(contentStream, dateAvis, 426, pageHeight - 82);

                    // Periode de l'arrete
                    addText(contentStream, dateDebutStr, 172, pageHeight - 169);
                    addText(contentStream, dateFinStr, 172, pageHeight - 183);

                    // Infos client (identique à avant)
                    float startX = 318;
                    float endX = 500;
                    float startY = pageHeight - 130;
                    float lineHeight = 10;
                    StringBuilder addressBuilder = new StringBuilder();
                    if (bkdar.getAdr1() != null && !bkdar.getAdr1().isEmpty()) addressBuilder.append(bkdar.getAdr1());
                    if (bkdar.getAdr2() != null && !bkdar.getAdr2().isEmpty()) {
                        if (addressBuilder.length() > 0) addressBuilder.append(" ");
                        addressBuilder.append(bkdar.getAdr2());
                    }
                    boolean hasAddress = addressBuilder.length() > 0;
                    if (bkdar.getVil() != null && !bkdar.getVil().isEmpty()) {
                        if (hasAddress) addressBuilder.append("-");
                        addressBuilder.append(bkdar.getVil());
                    }
                    if (bkdar.getCpos() != null && !bkdar.getCpos().isEmpty()) {
                        if (addressBuilder.length() > 0) addressBuilder.append(" ");
                        addressBuilder.append(bkdar.getCpos());
                    }
                    String infoClient = bkdar.getNomrest() != null ? bkdar.getNomrest() : "";
                    String adrAndVilAndCpos = addressBuilder.toString();
                    float currentY = startY;
                    currentY = writeMultilineText(contentStream, infoClient, startX, endX, currentY, lineHeight);
                    writeMultilineText(contentStream, adrAndVilAndCpos, startX, endX, currentY, lineHeight);

                    // Formatage des montants
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setGroupingSeparator(' ');
                    symbols.setDecimalSeparator(',');
                    DecimalFormat df = new DecimalFormat("#,##0.################", symbols);

                    // Interets debiteurs
                    String nbDebiteur = historique.getSumMvtDebiteur() != null
                            ? df.format(historique.getSumMvtDebiteur())
                            : "0,00";
                    String txDebiteur = historique.getTauxDebiteur() != null
                            ? df.format(historique.getTauxDebiteur())
                            : "0,00";
                    String intDebiteur = historique.getInteretDebiteur() != null
                            ? df.format(historique.getInteretDebiteur())
                            : "0,00";
                    addText(contentStream, nbDebiteur, 308, pageHeight - 293);
                    addText(contentStream, txDebiteur, 407, pageHeight - 293);
                    addText(contentStream, intDebiteur, 471, pageHeight - 293);

                    // Interets crediteurs
                    String nbCrediteur = historique.getSumMvtCrediteur() != null
                            ? df.format(historique.getSumMvtCrediteur())
                            : "0,00";
                    String txCrediteur = historique.getTauxCrediteur() != null
                            ? df.format(historique.getTauxCrediteur())
                            : "0,00";
                    String intCrediteur = historique.getInteretCrediteur() != null
                            ? df.format(historique.getInteretCrediteur())
                            : "0,00";
                    addText(contentStream, nbCrediteur, 308, pageHeight - 366);
                    addText(contentStream, txCrediteur, 407, pageHeight - 366);
                    addText(contentStream, intCrediteur, 471, pageHeight - 366);

                    // Net agios
                    String agiosCredit = historique.getInteretCrediteur() != null
                            ? df.format(historique.getInteretCrediteur())
                            : "0,00";
                    String agiosDebit = historique.getInteretDebiteur() != null
                            ? df.format(historique.getInteretDebiteur())
                            : "0,00";
                    addText(contentStream, agiosCredit, 316, pageHeight - 468);
                    addText(contentStream, agiosDebit, 438, pageHeight - 468);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            log.info("PDF généré avec succès");
            return baos.toByteArray();
        }
    }

}