package com.jxyq.commons.lang;

public final class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static String camelCaseToSnakeCase(String camel) {
        if (StringUtils.contains(camel, "_")) {
            return camel;
        }

        String snakeCase = StringUtils.EMPTY;
        char[] array = camel.toCharArray();

        for (char c : array) {
            if (c >= 65 && c <= 90) {
                snakeCase += "_" + c;
            } else {
                snakeCase += c;
            }
        }

        return snakeCase;
    }
}
