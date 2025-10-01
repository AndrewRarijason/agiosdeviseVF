package mg.bmoi.agiosdevise.service.extraction;

import mg.bmoi.agiosdevise.DTO.ExtractionBkdarDto;
import mg.bmoi.agiosdevise.repository.ExtractionBkdarRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtractionBkdarService {

    private final ExtractionBkdarRepository extractionBkdarRepository;

    public ExtractionBkdarService(ExtractionBkdarRepository extractionBkdarRepository) {
        this.extractionBkdarRepository = extractionBkdarRepository;
    }

    public List<ExtractionBkdarDto> getBkdarExtraction(String annee, String mois) {
        List<Object[]> results = extractionBkdarRepository.findBkdarExtraction(annee, mois);
        return results.stream()
                .map(row -> new ExtractionBkdarDto(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        (BigDecimal) row[3],
                        (BigDecimal) row[4],
                        (BigDecimal) row[5],
                        (BigDecimal) row[6],
                        (BigDecimal) row[7],
                        (String) row[8],
                        (String) row[9],
                        (String) row[10],
                        (String) row[11],
                        (String) row[12],
                        (Date) row[13],
                        (Character) row[14],
                        (String) row[15],
                        (String) row[16]
                ))
                .collect(Collectors.toList());
    }

    public InputStreamResource generateCsv(List<ExtractionBkdarDto> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))){

            writer.write('\ufeff');
            writer.println("AGE;DEV;NCP;NBC;TXC;NBR;TAUX;SOLDE;NOMREST;ADR1;ADR2;CPOS;CLC;DATR;TCLI;VIL;CLI");

            data.forEach(dto -> {
                String age = dto.getAgeBkdar() != null ? dto.getAgeBkdar().trim() : "";
                String dev = dto.getDev() != null ? dto.getDev().trim() : "";
                String ncp = dto.getNcp() != null ? dto.getNcp().trim() : "";
                String nbc = dto.getNbc() != null ? dto.getNbc().toPlainString().replace('.', ',') : "0,00";
                String txc = dto.getTxc() != null ? dto.getTxc().toPlainString().replace('.', ',') : "0,00";
                String nbr = dto.getNbr() != null ? dto.getNbr().toPlainString().replace('.', ',') : "0,00";
                String taux = dto.getTaux() != null ? dto.getTaux().toPlainString().replace('.', ',') : "0,00";
                String solde = dto.getSolde() != null ? dto.getSolde().toPlainString().replace('.', ',') : "0,00";
                String nomrest = dto.getNomrest() != null ? dto.getNomrest().trim() : "";
                String adr1 = dto.getAdr1() != null ? dto.getAdr1().trim() : "";
                String adr2 = dto.getAdr2() != null ? dto.getAdr2().trim() : "";
                String cpos = dto.getCpos() != null ? dto.getCpos().trim() : "";
                String clc = dto.getClc() != null ? dto.getClc().trim() : "";
                String datr = dto.getDatr() != null ? dto.getDatr().toString() : "";
                String tcli = dto.getTcli() != null ? dto.getTcli().toString() : "";
                String vil = dto.getVil() != null ? dto.getVil().trim() : "";
                String cli = dto.getCli() != null ? dto.getCli().trim() : "";

                writer.println(String.join(";", age, dev, ncp, nbc, txc, nbr, taux, solde, nomrest, adr1, adr2, cpos, clc, datr, tcli, vil, cli));
            });

            writer.flush();
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

        } catch (Exception e) {
            throw new RuntimeException("Error lors de la génération du CSV", e);
        }
    }

    public InputStreamResource generateExcel(List<ExtractionBkdarDto> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bkdar");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("AGE");
            header.createCell(1).setCellValue("DEV");
            header.createCell(2).setCellValue("NCP");
            header.createCell(3).setCellValue("NBC");
            header.createCell(4).setCellValue("TXC");
            header.createCell(5).setCellValue("NBR");
            header.createCell(6).setCellValue("TAUX");
            header.createCell(7).setCellValue("SOLDE");
            header.createCell(8).setCellValue("NOMREST");
            header.createCell(9).setCellValue("ADR1");
            header.createCell(10).setCellValue("ADR2");
            header.createCell(11).setCellValue("CPOS");
            header.createCell(12).setCellValue("CLC");
            header.createCell(13).setCellValue("DATR");
            header.createCell(14).setCellValue("TCLI");
            header.createCell(15).setCellValue("VIL");
            header.createCell(16).setCellValue("CLI");

            int rowIdx = 1;
            for (ExtractionBkdarDto dto : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getAgeBkdar() != null ? dto.getAgeBkdar().trim() : "");
                row.createCell(1).setCellValue(dto.getDev() != null ? dto.getDev().trim() : "");
                row.createCell(2).setCellValue(dto.getNcp() != null ? dto.getNcp().trim() : "");
                row.createCell(3).setCellValue(dto.getNbc() != null ? dto.getNbc().doubleValue() : 0.0);
                row.createCell(4).setCellValue(dto.getTxc() != null ? dto.getTxc().doubleValue() : 0.0);
                row.createCell(5).setCellValue(dto.getNbr() != null ? dto.getNbr().doubleValue() : 0.0);
                row.createCell(6).setCellValue(dto.getTaux() != null ? dto.getTaux().doubleValue() : 0.0);
                row.createCell(7).setCellValue(dto.getSolde() != null ? dto.getSolde().doubleValue() : 0.0);
                row.createCell(8).setCellValue(dto.getNomrest() != null ? dto.getNomrest().replaceAll("\\s+$", "") : "");
                row.createCell(9).setCellValue(dto.getAdr1() != null ? dto.getAdr1().trim() : "");
                row.createCell(10).setCellValue(dto.getAdr2() != null ? dto.getAdr2().trim() : "");
                row.createCell(11).setCellValue(dto.getCpos() != null ? dto.getCpos().trim() : "");
                row.createCell(12).setCellValue(dto.getClc() != null ? dto.getClc().trim() : "");
                row.createCell(13).setCellValue(dto.getDatr() != null ? dto.getDatr().toString() : "");
                row.createCell(14).setCellValue(dto.getTcli() != null ? dto.getTcli().toString() : "");
                row.createCell(15).setCellValue(dto.getVil() != null ? dto.getVil().trim() : "");
                row.createCell(16).setCellValue(dto.getCli() != null ? dto.getCli().trim() : "");
            }

            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du fichier Excel", e);
        }
    }
}