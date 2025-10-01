package mg.bmoi.agiosdevise.exception;

public class ImportDerogExcelException extends RuntimeException{
    public ImportDerogExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportDerogExcelException(String message) {
        super(message);
    }
}
