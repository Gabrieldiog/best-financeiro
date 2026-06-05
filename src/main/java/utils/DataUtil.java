package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataUtil {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter BR_ARQUIVO = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String hoje() {
        return LocalDate.now().format(ISO);
    }

    public static String hojeParaArquivo() {
        return LocalDate.now().format(BR_ARQUIVO);
    }

    public static String paraBanco(String dataBr) {
        try {
            LocalDate data = LocalDate.parse(dataBr, BR);
            return data.format(ISO);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String paraExibicao(String dataIso) {
        try {
            LocalDate data = LocalDate.parse(dataIso, ISO);
            return data.format(BR);
        } catch (DateTimeParseException e) {
            return dataIso;
        }
    }
}
