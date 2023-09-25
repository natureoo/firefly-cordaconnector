package io.kaleido.firefly.cordapp.util;


public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    public static boolean isNotNullAndEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static boolean isNullOrEmptyAfterTrim(String str) {
        if (str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    public static boolean isNotNullAndEmptyAfterTrim(String str) {
        return !isNullOrEmptyAfterTrim(str);
    }
}

