package com.moyu.media.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Random;

public class MYUtil {
    public static int GetTimeStamps() {
        return Integer.parseInt(String.valueOf(System.currentTimeMillis() / 1000));
    }

    public static String GetParam(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val == null ? "" : val.toString();
    }

    public static int ParseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean IsExistParams(Map<String, Object> map, String[] keys) {
        boolean result = true;
        for (int i = 0; i < keys.length; i++) {
            if (map.get(keys[i]) == null || map.get(keys[i]) == "") {
                result = false;
                break;
            }
        }
        return result;
    }

    public static String Random(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String getIp() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
