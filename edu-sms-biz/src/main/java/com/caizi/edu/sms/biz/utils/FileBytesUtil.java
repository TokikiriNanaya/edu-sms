package com.caizi.edu.sms.biz.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileBytesUtil {
    public static byte[] getBytesByFile(String pathStr) {
        try {
            InputStream is = FileBytesUtil.class.getResourceAsStream(pathStr);
            if(is == null){
                is = new FileInputStream(pathStr);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = is.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            is.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
