package mg.bmoi.agiosdevise.exception;

import mg.bmoi.agiosdevise.controller.DerogImportExcelController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Throwable cause = ex.getCause();

        String message;
        if (cause != null && cause.getMessage() != null && cause.getMessage().contains("Parse attempt failed")) {
            message = "Date invalide : vérifiez le format (jj-MM-aaaa)";
        } else {
            message = "Paramètre invalide : " + ex.getName();
        }

        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDateTimeParseException(DateTimeParseException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Format de date invalide : " + ex.getMessage());
        return error;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullPointerException(NullPointerException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Une erreur interne est survenue : " + ex.getMessage());
        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Une erreur inattendue est survenue : " + ex.getMessage());
        return error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Une erreur est survenu : " + ex.getMessage());
        return error;
    }

    @ExceptionHandler(ImportDerogExcelException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleExcelImportException(ImportDerogExcelException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erreur d'import Excel : " + ex.getMessage());
        return error;
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleSQLException(SQLException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erreur de connexion à la base Oracle : " + ex.getMessage());
        return error;
    }

    @ExceptionHandler(UserValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserValidationException(UserValidationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

}
