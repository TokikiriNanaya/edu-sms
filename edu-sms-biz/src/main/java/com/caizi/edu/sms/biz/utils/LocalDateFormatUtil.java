package com.caizi.edu.sms.biz.utils;

import com.antnest.mscore.util.StringUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateFormatUtil {

    public static final String NORMAL_PATTERN="yyyy-MM-dd";
    public static final String NORMAL_PATTERN_WITH_OUT_H="yyyyMMdd";
    public static final String ALL_PATTERN="yyyy-MM-dd HH:mm:ss";
    public static final String EXPORT_PATTERN="yyyy年MM月dd日 HH:mm";

    public static String transNormalStr(LocalDateTime localDateTime){
        if(null==localDateTime){
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NORMAL_PATTERN);
        return dateTimeFormatter.format(localDateTime);
    }

    public static String transExportPatternStr(LocalDateTime localDateTime){
        if(null==localDateTime){
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(EXPORT_PATTERN);
        return dateTimeFormatter.format(localDateTime);
    }

    public static String transAllStr(LocalDateTime localDateTime){
        if(null==localDateTime){
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ALL_PATTERN);
        return dateTimeFormatter.format(localDateTime);
    }
    public static String transNormalStrWithOutH(LocalDateTime localDateTime){
        if(null==localDateTime){
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NORMAL_PATTERN_WITH_OUT_H);
        return dateTimeFormatter.format(localDateTime);
    }

    public static LocalDate transNormalStrToLocalDate(String localDate){
        if(StringUtil.isBlank(localDate)){
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NORMAL_PATTERN);
        LocalDate parse = LocalDate.parse(localDate, dateTimeFormatter);
        return parse;
    }

    public static LocalDateTime transFullStrToLocalDateTime(String localDateTime){
        if(StringUtil.isBlank(localDateTime)){
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ALL_PATTERN);
        LocalDateTime parse = LocalDateTime.parse(localDateTime, dateTimeFormatter);
        return parse;
    }
}
