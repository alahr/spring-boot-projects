package com.alahr.spring.boot.projects.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public final static String PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
    public final static String PATTERN_2 = "yyyy-MM-dd";

    public static Date parseString2Date(String d){
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_2);
        try {
            return sdf.parse(d);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String parseDate2String(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_1);
        return sdf.format(d);
    }
}
