package com.surenpi.gitee.client;

public class JOINER {
    public static String join(String ...items) {
        if(items == null) {

            return "";
        }

        StringBuffer buf = new StringBuffer();
        for(String item : items) {
            if(item == null) {
                continue;
            }

            buf.append(item);
        }

        return buf.toString();
    }
}
