package com.surenpi.gitee.client;

public abstract class StringUtils
{
    public static boolean isEmptyOrSpaces(String str) {
        return str == null || "".equals(str.trim());
    }

    public static String trim(String token) {
        if(token != null) {
            return token.trim();
        }

        return null;
    }

    public static boolean startsWithIgnoreCase(String a, String b) {
        return false;
    }

    public static String notNullize(String description) {
        return String.valueOf(description);
    }

    public static boolean equalsIgnoreCase(String myRepositoryName, String myRepositoryName1) {
        return myRepositoryName != null ? myRepositoryName.equalsIgnoreCase(myRepositoryName1) : myRepositoryName1 == null;
    }
}
