package com.hub4.domain.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CurrencyFormatter {
    private static final Locale BRAZIl = Locale.forLanguageTag("br");

    public static String format(BigDecimal value) {
        if (value == null) {
            return NumberFormat.getCurrencyInstance(BRAZIl).format(BigDecimal.ZERO);
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(BRAZIl);
        return currencyFormat.format(value);
    }
}
