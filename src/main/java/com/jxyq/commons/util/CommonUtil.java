package com.jxyq.commons.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CommonUtil {
    /*生成6位数字验证码*/
    public static String produceCode() {
        Random random = new Random();
        int code_int = random.nextInt(1000000);
        return StringUtils.leftPad(String.valueOf(code_int), 6, '0');
    }

    public static int calculateAge(Date birth, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int yearNow = cal.get(Calendar.YEAR);
        int monNow = cal.get(Calendar.MONTH);
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birth);
        int yearBirth = cal.get(Calendar.YEAR);
        int monBirth = cal.get(Calendar.MONTH);
        int dayBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (age <= 0) return 0;

        if (monNow > monBirth) return age;
        if (monNow == monBirth) {
            if (dayNow >= dayBirth) return age;
        }
        age--;
        return age;
    }

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static boolean ifNotExistsCreate(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (SecurityException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static int getPageCount(int total_count, int page_size){
        if (page_size == 0 || total_count == 0) return 0;
        return (total_count % page_size == 0 ? total_count / page_size : total_count / page_size + 1);
    }
}
