package com.alahr.spring.boot.projects.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private final static String PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
    private final static String PATTERN_2 = "yyyy-MM-dd";

    public static Date parse(String d){
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_2);
        try {
            return sdf.parse(d);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }
}
