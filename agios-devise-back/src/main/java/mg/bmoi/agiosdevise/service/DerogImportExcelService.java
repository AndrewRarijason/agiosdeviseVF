package mg.bmoi.agiosdevise.service;

import mg.bmoi.agiosdevise.DTO.DerogImportExcelDto;
import mg.bmoi.agiosdevise.exception.ImportDerogExcelException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DerogImportExcelService {
    public List<DerogImportExcelDto> importFromExcel(MultipartFile file){
        List<DerogImportExcelDto> result = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Ignore l'entête
                DerogImportExcelDto dto = new DerogImportExcelDto();
                dto.setAge(getCellValueAsString(row.getCell(0)));
                dto.setDev(getCellValueAsString(row.getCell(1)));
                dto.setNcp(getCellValueAsString(row.getCell(2)));
                dto.setClc(getCellValueAsString(row.getCell(3)));
                dto.setCli(getCellValueAsString(row.getCell(4)));
                dto.setInti(getCellValueAsString(row.getCell(5)));
                dto.setTyp(getCellValueAsString(row.getCell(6)));
                dto.setGes(getCellValueAsString(row.getCell(7)));
                dto.setDerogation(getCellValueAsString(row.getCell(8)));
                result.add(dto);
            }
        } catch (Exception e) {
            throw new ImportDerogExcelException("Erreur lors de l'importation du fichier Excel", e);
        }
        return result;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long)cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}