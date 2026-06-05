package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Moeda {

    private static final Locale BRASIL = Locale.of("pt", "BR");
    private static final NumberFormat FORMATO = NumberFormat.getCurrencyInstance(BRASIL);

    public static String formatar(double valor) {
        return FORMATO.format(valor);
    }
}
