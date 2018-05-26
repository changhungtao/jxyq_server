package com.jxyq.app.watch;

public class CharUtils {
    public CharUtils() {
    }

    public static String LatLngToChar(String s) {
        try {
            char[] e = s.toCharArray();
            char[] char2 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};

            for (int i = 0; i < e.length; ++i) {
                if (e[i] == 46) {
                    e[i] = 107;
                } else if (e[i] == 44) {
                    e[i] = 108;
                } else {
                    e[i] = char2[Integer.parseInt(String.valueOf(e[i]))];
                }
            }

            return String.valueOf(e);
        } catch (Exception var4) {
            return null;
        }
    }

    public static String CharToLatLng(String s) {
        try {
            char[] e = s.toCharArray();

            for (int i = 0; i < e.length; ++i) {
                if (e[i] == 107) {
                    e[i] = 46;
                } else if (e[i] == 108) {
                    e[i] = 44;
                } else if (e[i] == 97) {
                    e[i] = 48;
                } else if (e[i] == 98) {
                    e[i] = 49;
                } else if (e[i] == 99) {
                    e[i] = 50;
                } else if (e[i] == 100) {
                    e[i] = 51;
                } else if (e[i] == 101) {
                    e[i] = 52;
                } else if (e[i] == 102) {
                    e[i] = 53;
                } else if (e[i] == 103) {
                    e[i] = 54;
                } else if (e[i] == 104) {
                    e[i] = 55;
                } else if (e[i] == 105) {
                    e[i] = 56;
                } else if (e[i] == 106) {
                    e[i] = 57;
                }
            }

            return String.valueOf(e);
        } catch (Exception var3) {
            return null;
        }
    }
}
