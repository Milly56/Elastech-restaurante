package org.restaurante.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class FormatadorMoeda {
    private FormatadorMoeda() { }

    public static String formatar(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
                .format(valor).replace('\u00a0', ' ').replace('\u202f', ' ');
    }
}
