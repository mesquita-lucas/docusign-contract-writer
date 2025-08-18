package com.hub4.domain.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    private static final Locale BRAZIl = Locale.forLanguageTag("br");

    public static String format(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }

        DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(BRAZIl));

        value = value.setScale(2, RoundingMode.HALF_UP);
        return "R$ " + formatter.format(value);
    }
}
