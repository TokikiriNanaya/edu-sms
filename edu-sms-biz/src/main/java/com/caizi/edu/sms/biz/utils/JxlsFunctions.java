package com.caizi.edu.sms.biz.utils;

import com.caizi.edu.sms.constant.ExcelConstant;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Jxls2 自定义函数
 */
public class JxlsFunctions {
    /**
     * 默认的日期格式化
     */
    private String fmt_date = "yyyy-MM-dd";

    /**
     * 默认的时间格式化
     */
    private String fmt_time = "yyyy-MM-dd HH:mm:ss";

    /**
     * 自定义函数：日期格式化
     * @param date  日期
     * @param fmt   格式
     * @return
     */
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        if(fmt == null){
            fmt = this.fmt_date;
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 自定义函数：时间格式化
     * @param time  时间
     * @param fmt   格式
     * @return
     */
    public String dateTimeFmt(Date time, String fmt) {
        if (time == null) {
            return "";
        }
        if(fmt == null){
            fmt = this.fmt_time;
        }
        return dateFmt(time,fmt);
    }

    /**
     * 自定义函数：if判断
     * @param b 条件
     * @param o1    条件为true的返回值
     * @param o2    条件为false的返回值
     * @return
     */
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }

    /**
     * 导出进度写入Redis
     * @param exportId  导出ID
     * @param dataSize  数据总量
     * @param dataIndex 当前写入行数
     */
    public void rate(String exportId,Integer dataSize,Integer dataIndex){
        RedisTemplate redisTemplate = SpringContextUtils.getBean("redisTemplate");

        //导出进度写入Redis
        redisTemplate.opsForHash().put(ExcelConstant.REDIS_EXPORT_SIZE,exportId ,dataSize);
        redisTemplate.opsForHash().put(ExcelConstant.REDIS_EXPORT_RATE,exportId ,dataIndex + 1);

//        System.out.println("导出进度：" + (dataIndex + 1) + "," + dataSize);
    }

    /**
     * 分组情况下导出进度实现
     * @param exportId  导出ID
     * @param dataSize  数据总量
     * @param express   能标识该行数据是否有效的业务数据的值
     * @return
     */
    public Integer autoRate(String exportId,Integer dataSize,String express){
        if(null == express){
            return null;
        }

        RedisTemplate redisTemplate = SpringContextUtils.getBean("redisTemplate");

        //导出进度写入Redis
        Map<String, Object> sizeEntries = redisTemplate.opsForHash().entries(ExcelConstant.REDIS_EXPORT_SIZE);
        Object size = sizeEntries.get(exportId);
        if (null == size) {
            redisTemplate.opsForHash().put(ExcelConstant.REDIS_EXPORT_SIZE,exportId ,dataSize);
        }

        Map<String, Object> indexEntries = redisTemplate.opsForHash().entries(ExcelConstant.REDIS_EXPORT_RATE);
        Object index = indexEntries.get(exportId);
        Integer result = 1;
        if (null != index) {
            result = Integer.valueOf(index.toString()) + 1;
        }

        redisTemplate.opsForHash().put(ExcelConstant.REDIS_EXPORT_RATE,exportId ,result);

//        System.out.println("导出进度：" + result + "," + dataSize);

        return result;
    }

    /**
     * 分组情况下导出进度实现(不返回序号)
     * @param exportId  导出ID
     * @param dataSize  数据总量
     * @param express   能标识该行数据是否有效的业务数据的值
     */
    public void autoRateNoIndex(String exportId,Integer dataSize,String express){
        this.autoRate(exportId,dataSize,express);
    }
}
