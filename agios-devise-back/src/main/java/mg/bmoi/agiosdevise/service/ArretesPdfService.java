package mg.bmoi.agiosdevise.service;

import static mg.bmoi.agiosdevise.util.PdfUtils.addText;
import static mg.bmoi.agiosdevise.util.PdfUtils.writeMultilineText;

import mg.bmoi.agiosdevise.DTO.SyntheseInteretsDto;
import mg.bmoi.agiosdevise.DTO.TransactionDto;
import mg.bmoi.agiosdevise.entity.Bkage;
import mg.bmoi.agiosdevise.entity.BkdarTemp;
import mg.bmoi.agiosdevise.entity.TicDevise;
import mg.bmoi.agiosdevise.repository.BkageRepository;
import mg.bmoi.agiosdevise.repository.TicDeviseRepository;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class ArretesPdfService {
    private static final Logger log = LoggerFactory.getLogger(ArretesPdfService.class);
    private final TicDeviseRepository ticDeviseRepository;
    private final BkageRepository bkageRepository;

    public ArretesPdfService(TicDeviseRepository ticDeviseRepository, BkageRepository bkageRepository) {
        this.ticDeviseRepository = ticDeviseRepository;
        this.bkageRepository = bkageRepository;
    }

    public byte[] generateArretesPdf(Date dateDebut, Date dateFin,
                                     SyntheseInteretsDto synthese,
                                     List<TransactionDto> transactions,
                                     BkdarTemp bkdar,
                                     String dernierJourOuvre) throws IOException {
        Resource resource = new ClassPathResource("templates/Template-echelle-arrete.pdf");

        if (!resource.exists()) {
            log.error("Template PDF non trouvé");
            throw new FileNotFoundException("Template PDF non trouvé: templates/Template-echelle-arrete.pdf");
        }

        try (InputStream is = resource.getInputStream();
             PDDocument document = PDDocument.load(is)) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateDebutStr = sdf.format(dateDebut);
            String dateFinStr = sdf.format(dateFin);

            log.info("Modification du PDF...");
            PDPage page = document.getPage(0);
            float pageHeight = page.getMediaBox().getHeight();

            DecimalFormat df = new DecimalFormat("#,##0.00");
            float baseY = pageHeight - 234;
            float lineHeight = 12.5f;
            float maxY = 710;

            // Préparation des entêtes
            String periodeText = "Du " + dateDebutStr + " au " + dateFinStr;
            float agenceX = 271;
            float agenceY = pageHeight - 134.5f;

            String ageText = (bkdar != null && bkdar.getAge() != null) ? bkdar.getAge().trim() : "";
            String libelle = bkageRepository.findById(bkdar != null ? bkdar.getAge() : null)
                    .map(Bkage::getLib)
                    .orElse("");
            String agenceText = ageText + " - " + libelle;
            float x = 77;
            float y = pageHeight - 149.5f;

            String dev = ticDeviseRepository.findByCdIso(bkdar != null ? bkdar.getId().getDev() : null)
                    .map(TicDevise::getCdAlpha)
                    .orElse("");
            String ncp = (bkdar != null ? bkdar.getId().getNcp() : null) != null ? bkdar.getId().getNcp().trim() : "";
            String clc = (bkdar != null ? bkdar.getClc() : null) != null ? bkdar.getClc().trim() : "";
            String infoText = dev + "-" + ncp + "-" + clc;
            float infoX = 76.5f;
            float infoY = pageHeight - 162;

            String dateValText = DateFormatUtil.formatDateToSlash(dernierJourOuvre);
            float dateX = 73;
            float dateY = pageHeight - 189.2f;

            String nomRestText = (bkdar != null ? bkdar.getNomrest() : null) != null ? bkdar.getNomrest().trim() : "";
            float nomX = 218;
            float nomEndX = 434;
            float nomY = pageHeight - 158;
            float lnHeight = 12.5f;

            // Boucle sur les mouvements sauf la dernière ligne
            int lastIndex = transactions.size() - 1;
            PDPageContentStream contentStream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setFont(PDType1Font.COURIER, 6.7f);

            // Entêtes page 1
            addText(contentStream, periodeText, agenceX, agenceY);
            addText(contentStream, agenceText, x, y);
            addText(contentStream, infoText, infoX, infoY);
            addText(contentStream, dateValText, dateX, dateY);
            float nextY = writeMultilineText(contentStream, nomRestText, nomX, nomEndX, nomY, lnHeight);
            writeMultilineText(contentStream, "COMPTE A VUE", nomX, nomEndX, nextY, lnHeight);

            BigDecimal maxDebiteur = null;

            for (int i = 0; i < lastIndex; i++) {
                TransactionDto tx = transactions.get(i);

                // Récupération du maximum débiteur
                if (tx.getSoldeDepart() != null) {
                    if (maxDebiteur == null || tx.getSoldeDepart().abs().compareTo(maxDebiteur.abs()) > 0) {
                        maxDebiteur = tx.getSoldeDepart();
                    }
                }
                if (tx.getNbDebiteur() != null) {
                    if (maxDebiteur == null || tx.getNbDebiteur().abs().compareTo(maxDebiteur.abs()) > 0) {
                        maxDebiteur = tx.getNbDebiteur();
                    }
                }

                // Pagination dynamique
                if (baseY > maxY) {
                    contentStream.close();
                    PDPage newPage = new PDPage(page.getMediaBox());
                    document.getPages().insertAfter(newPage, page);
                    page = newPage;
                    pageHeight = page.getMediaBox().getHeight();
                    baseY = pageHeight - 232;

                    contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
                    contentStream.setFont(PDType1Font.COURIER, 6.7f);

                    // Réinsertion des entêtes sur la nouvelle page
                    addText(contentStream, periodeText, agenceX, agenceY);
                    addText(contentStream, agenceText, x, y);
                    addText(contentStream, infoText, infoX, infoY);
                    addText(contentStream, dateValText, dateX, dateY);
                    float nextY2 = writeMultilineText(contentStream, nomRestText, nomX, nomEndX, nomY, lnHeight);
                    writeMultilineText(contentStream, "COMPTE A VUE", nomX, nomEndX, nextY2, lnHeight);
                }

                // Date
                String dvaStr = tx.getDva() != null ? DateFormatUtil.formatDateToSlash(tx.getDva().toString()) : "";
                addText(contentStream, dvaStr, 41, baseY);

                // Montant
                if (tx.getMon() != null) {
                    String monStr = df.format(tx.getMon().abs());
                    if (tx.getMon().compareTo(BigDecimal.ZERO) < 0) {
                        float monX = 147 - PDType1Font.COURIER.getStringWidth(monStr) / 1000 * 8;
                        addText(contentStream, monStr, monX, baseY);
                    } else if (tx.getMon().compareTo(BigDecimal.ZERO) > 0) {
                        float monX = 212 - PDType1Font.COURIER.getStringWidth(monStr) / 1000 * 8;
                        addText(contentStream, monStr, monX, baseY);
                    }
                }

                // Solde départ
                if (tx.getSoldeDepart() != null) {
                    String soldeStr = df.format(tx.getSoldeDepart());
                    if (tx.getSoldeDepart().compareTo(BigDecimal.ZERO) < 0) {
                        String soldeStrabs = df.format(tx.getSoldeDepart().abs());
                        float soldeX = 277 - PDType1Font.COURIER.getStringWidth(soldeStrabs) / 1000 * 8;
                        addText(contentStream, soldeStrabs, soldeX, baseY);
                    } else {
                        float soldeX = 342 - PDType1Font.COURIER.getStringWidth(soldeStr) / 1000 * 8;
                        addText(contentStream, soldeStr, soldeX, baseY);
                    }
                }

                // Jours inactifs
                if (tx.getNbJoursInactif() != 0) {
                    String joursStr = String.valueOf(tx.getNbJoursInactif());
                    float joursX = 358 - PDType1Font.COURIER.getStringWidth(joursStr) / 1000 * 8;
                    addText(contentStream, joursStr, joursX, baseY);
                }

                // Débiteur / Créditeur
                boolean debiteurIsZero = tx.getNbDebiteur() == null || tx.getNbDebiteur().compareTo(BigDecimal.ZERO) == 0;
                boolean crediteurIsZero = tx.getNbCrediteur() == null || tx.getNbCrediteur().compareTo(BigDecimal.ZERO) == 0;

                if (debiteurIsZero && !crediteurIsZero) {
                    String credStr = df.format(tx.getNbCrediteur());
                    float credX = 491 - PDType1Font.COURIER.getStringWidth(credStr) / 1000 * 8;
                    addText(contentStream, credStr, credX, baseY);

                    String tauxCrediteurStr = synthese.getTauxCrediteur() != null ? synthese.getTauxCrediteur().toString() : "";
                    float tauxX = 507;
                    addText(contentStream, tauxCrediteurStr, tauxX, baseY);
                } else if (!debiteurIsZero && crediteurIsZero) {
                    String debStr = df.format(tx.getNbDebiteur().abs());
                    float debX = 426 - PDType1Font.COURIER.getStringWidth(debStr) / 1000 * 8;
                    addText(contentStream, debStr, debX, baseY);

                    String tauxDebiteurStr = synthese.getTauxDebiteur() != null ? synthese.getTauxDebiteur().toString() : "";
                    float tauxX = 507;
                    addText(contentStream, tauxDebiteurStr, tauxX, baseY);
                }

                baseY -= lineHeight;
            }

            contentStream.close();


            // Dans l'avant derniere page en bas, ajout des totaux
            PDPage beforeLastPage = document.getPage(document.getNumberOfPages() - 2);
            PDPageContentStream totalStream = new PDPageContentStream(
                    document, beforeLastPage, PDPageContentStream.AppendMode.APPEND, true, true);
            totalStream.setFont(PDType1Font.COURIER, 6.7f);

            // Récupération des valeurs
            String sumMvtDebitStr = df.format(synthese.getSumMvtDebiteur());
            String sumMvtCreditStr = df.format(synthese.getSumMvtCrediteur());
            String sumSoldeNegatifStr = df.format(
                    transactions.stream()
                            .filter(tx -> tx.getSoldeDepart() != null && tx.getSoldeDepart().compareTo(BigDecimal.ZERO) < 0)
                            .map(TransactionDto::getSoldeDepart)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            String sumSoldePositifStr = df.format(
                    transactions.stream()
                            .filter(tx -> tx.getSoldeDepart() != null && tx.getSoldeDepart().compareTo(BigDecimal.ZERO) > 0)
                            .map(TransactionDto::getSoldeDepart)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            int nbJoursInactifLast = transactions.get(transactions.size() - 1).getNbJoursInactif();

            // Affichage des valeurs aux positions demandées
            addText(totalStream, sumMvtDebitStr, 120, pageHeight - 749);
            addText(totalStream, sumMvtCreditStr, 177f, pageHeight - 761);
            addText(totalStream, sumSoldeNegatifStr, 247f, pageHeight - 749);
            addText(totalStream, sumSoldePositifStr, 307f, pageHeight - 761);
            addText(totalStream, String.valueOf(nbJoursInactifLast), 350, pageHeight - 749);
            addText(totalStream, sumMvtDebitStr, 398, pageHeight - 749);
            addText(totalStream, sumMvtCreditStr, 455f, pageHeight - 761);

            totalStream.close();



            // Dans la derniere page
            PDPage lastPage = document.getPage(document.getNumberOfPages() - 1);
            float lastPageHeight = lastPage.getMediaBox().getHeight();
            PDPageContentStream lastPageStream = new PDPageContentStream(
                    document, lastPage, PDPageContentStream.AppendMode.APPEND, true, true);
            lastPageStream.setFont(PDType1Font.COURIER, 6.7f);

            // Réutilisation des entêtes
            addText(lastPageStream, periodeText, agenceX, lastPageHeight - 134.5f);
            addText(lastPageStream, agenceText, x, lastPageHeight - 149.5f);
            addText(lastPageStream, infoText, infoX, lastPageHeight - 162);
            addText(lastPageStream, dateValText, dateX, lastPageHeight - 187.5f);
            float nextYLast = writeMultilineText(lastPageStream, nomRestText, nomX, nomEndX, lastPageHeight - 158, lnHeight);
            writeMultilineText(lastPageStream, "COMPTE A VUE", nomX, nomEndX, nextYLast, lnHeight);

            // Récupération des valeurs
            String sumMvtDebiteurStr = df.format(synthese.getSumMvtDebiteur());
            String sumMvtCrediteurStr = df.format(synthese.getSumMvtCrediteur().abs());
            String tauxDebiteurStr = synthese.getTauxDebiteur() != null ? df.format(synthese.getTauxDebiteur()) : "";
            String tauxCrediteurStr = synthese.getTauxCrediteur() != null ? df.format(synthese.getTauxCrediteur()) : "";
            String interetDebiteurStr = synthese.getInteretDebiteur() != null ? df.format(synthese.getInteretDebiteur().abs()) : "";
            String interetCrediteurStr = synthese.getInteretCrediteur() != null ? df.format(synthese.getInteretCrediteur().abs()) : "";
            String maxDebiteurStr = (maxDebiteur != null) ? df.format(maxDebiteur.abs()) : "";
            String maxCrediteurStr = "0,00";

            BigDecimal interetDebiteur = new BigDecimal(interetDebiteurStr.replace(" ", "").replace(",", "."));
            BigDecimal interetCrediteur = new BigDecimal(interetCrediteurStr.replace(" ", "").replace(",", "."));
            BigDecimal tva = interetDebiteur.add(interetCrediteur)
                    .multiply(BigDecimal.valueOf(20))
                    .divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP);
            String tvaStr = df.format(tva);

            BigDecimal netAdebiter = interetDebiteur.add(interetCrediteur).add(tva).setScale(2, RoundingMode.HALF_UP);
            String netADebiterStr = df.format(netAdebiter);

            // Définir les positions "bord droit"
            float baseSumMvtX = 270f;
            float baseTauxX   = 340f;
            float baseInteretX= 424f;
            float baseMaxDebX = 270f;
            float baseMaxCredX = 340f;
            float baseTvaX = 424f;
            float baseNetADebX= 424f;

            // Largeurs des textes
            float sumMvtDebWidth = PDType1Font.COURIER.getStringWidth(sumMvtDebiteurStr) / 1000 * 8;
            float sumMvtCredWidth = PDType1Font.COURIER.getStringWidth(sumMvtCrediteurStr) / 1000 * 8;
            float tauxDebWidth    = PDType1Font.COURIER.getStringWidth(tauxDebiteurStr) / 1000 * 8;
            float tauxCredWidth   = PDType1Font.COURIER.getStringWidth(tauxCrediteurStr) / 1000 * 8;
            float interetDebWidth = PDType1Font.COURIER.getStringWidth(interetDebiteurStr) / 1000 * 8;
            float interetCredWidth= PDType1Font.COURIER.getStringWidth(interetCrediteurStr) / 1000 * 8;
            float maxDebWidth = PDType1Font.COURIER.getStringWidth(maxDebiteurStr) / 1000 * 8;
            float maxCredWidth = PDType1Font.COURIER.getStringWidth(maxCrediteurStr) / 1000 * 8;
            float tvaWidth = PDType1Font.COURIER.getStringWidth(tvaStr) / 1000 * 8;
            float netADebiterWidth = PDType1Font.COURIER.getStringWidth(netADebiterStr) / 1000 * 8;

            // Positions X ajustées
            float sumMvtDebX = baseSumMvtX - sumMvtDebWidth;
            float sumMvtCredX = baseSumMvtX - sumMvtCredWidth;
            float tauxDebX    = baseTauxX   - tauxDebWidth;
            float tauxCredX   = baseTauxX   - tauxCredWidth;
            float interetDebX = baseInteretX - interetDebWidth;
            float interetCredX= baseInteretX - interetCredWidth;
            float maxDebX = baseMaxDebX - maxDebWidth;
            float maxCredX = baseMaxCredX - maxCredWidth;
            float tvaX = baseTvaX - tvaWidth;
            float netADebX = baseNetADebX - netADebiterWidth;

            // Écriture alignée à droite
            addText(lastPageStream, sumMvtDebiteurStr, sumMvtDebX, lastPageHeight - 235.5f);
            addText(lastPageStream, sumMvtCrediteurStr, sumMvtCredX, lastPageHeight - 247.8f);
            addText(lastPageStream, tauxDebiteurStr,    tauxDebX,    lastPageHeight - 235.5f);
            addText(lastPageStream, tauxCrediteurStr,   tauxCredX,   lastPageHeight - 247.8f);
            addText(lastPageStream, interetDebiteurStr, interetDebX, lastPageHeight - 235.5f);
            addText(lastPageStream, interetCrediteurStr, interetCredX,lastPageHeight - 247.8f);
            addText(lastPageStream, maxDebiteurStr, maxDebX, lastPageHeight - 272.5f);
            addText(lastPageStream, maxCrediteurStr, maxCredX, lastPageHeight - 272.5f);
            addText(lastPageStream, tvaStr, tvaX, lastPageHeight - 285.3f);
            addText(lastPageStream, netADebiterStr, netADebX, lastPageHeight - 309.6f);



            lastPageStream.close();


            // Deuxième passage : numérotation dynamique des pages
            int totalPages = document.getNumberOfPages();
            PDFont font = PDType1Font.COURIER;
            float fontSize = 6.7f;

            for (int i = 0; i < totalPages; i++) {
                PDPage p = document.getPage(i);
                try (PDPageContentStream cs = new PDPageContentStream(document, p, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    String pageText = "Page " + (i + 1) + "/" + totalPages;
                    float xPos = 508;
                    float yPos = pageHeight - 188;

                    cs.beginText();
                    cs.setFont(font, fontSize);
                    cs.newLineAtOffset(xPos, yPos);
                    cs.showText(pageText);
                    cs.endText();
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            log.info("PDF généré avec succès");
            return baos.toByteArray();
        }
    }


}
