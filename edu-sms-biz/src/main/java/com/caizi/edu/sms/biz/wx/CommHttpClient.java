package com.caizi.edu.sms.biz.wx;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CommHttpClient {

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";

    public List<String> doRequest(String requestMethod, String path, String data) {
        List<String> resultList = new ArrayList<>();
        try {
            log.info("开始请求数据：" + path + "，参数：" + data);
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //请求方式
            conn.setRequestMethod(requestMethod);
            //设置通用的请求属性
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (data != null) {
                //获取URLConnection对象对应的输出流
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                //发送请求参数即数据
                out.print(data);
                //缓冲数据
                out.flush();
            }
            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = br.readLine()) != null) {
                log.info("响应内容：" + str);
                resultList.add(str);
            }
            //关闭流
            is.close();
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return resultList;
    }
}
