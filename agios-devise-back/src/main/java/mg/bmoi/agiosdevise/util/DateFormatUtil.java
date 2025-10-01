package mg.bmoi.agiosdevise.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;

public class DateFormatUtil {

    private static final List<DateTimeFormatter> SUPPORTED_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
    );

    public static String normalizeToIsoDate(String date) {
        for (DateTimeFormatter formatter : SUPPORTED_FORMATS) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                return localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (Exception ignored) {
            }
        }
        throw new IllegalArgumentException("Format de date non supporté : " + date);
    }

    public static Date parseDate(String dateStr) {
        String normalized = normalizeToIsoDate(dateStr);
        LocalDate localDate = LocalDate.parse(normalized, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Date dateRetour = Date.valueOf(localDate);
        return dateRetour;
    }

    public static String formatDateToSlash(String dateStr) {
        String normalized = normalizeToIsoDate(dateStr);
        LocalDate localDate = LocalDate.parse(normalized, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String[] getPeriodeTrimestre(String trimestre, String annee) {
        String debut = "";
        String fin = "";
        switch (trimestre) {
            case "T1":
                debut = "01-01-" + annee;
                fin = "31-03-" + annee;
                break;
            case "T2":
                debut = "01-04-" + annee;
                fin = "30-06-" + annee;
                break;
            case "T3":
                debut = "01-07-" + annee;
                fin = "30-09-" + annee;
                break;
            case "T4":
                debut = "01-10-" + annee;
                fin = "31-12-" + annee;
                break;
            default:
                throw new IllegalArgumentException("Période inexistante pour : " + trimestre);
        }
        return new String[]{debut, fin};
    }

    public static String[] getTrimestreFromDateFin(String dateFinPeriode) {
        // Utiliser normalizeToIsoDate pour s'assurer du format dd-MM-yyyy
        String normalizedDate = normalizeToIsoDate(dateFinPeriode);
        LocalDate date = LocalDate.parse(normalizedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        int mois = date.getMonthValue();
        String annee = String.valueOf(date.getYear());
        String trimestre;
        if (mois <= 3) {
            trimestre = "T1";
        } else if (mois <= 6) {
            trimestre = "T2";
        } else if (mois <= 9) {
            trimestre = "T3";
        } else {
            trimestre = "T4";
        }
        return new String[]{trimestre, annee};
    }

    public static String getRlet(String dateFinPeriode) {
        // Utiliser normalizeToIsoDate pour s'assurer du format dd-MM-yyyy
        String normalizedDate = normalizeToIsoDate(dateFinPeriode);
        LocalDate date = LocalDate.parse(normalizedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String mois = String.format("%02d", date.getMonthValue());
        String annee = String.valueOf(date.getYear() % 100); // Prendre les 2 derniers chiffres
        return mois + annee;
    }
}