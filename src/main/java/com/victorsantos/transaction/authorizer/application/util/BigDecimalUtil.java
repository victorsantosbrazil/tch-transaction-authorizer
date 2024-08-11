package com.victorsantos.transaction.authorizer.application.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    private BigDecimalUtil() {}

    public static boolean isGreaterThanZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isLowerThanZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isGreaterThan(BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) > 0;
    }
}
