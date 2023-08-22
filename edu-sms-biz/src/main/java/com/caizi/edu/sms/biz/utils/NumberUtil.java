package com.caizi.edu.sms.biz.utils;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.regex.Pattern;


public class NumberUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    public static boolean isNumber(String str) {
        if (StringUtils.isEmpty(str)) return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}