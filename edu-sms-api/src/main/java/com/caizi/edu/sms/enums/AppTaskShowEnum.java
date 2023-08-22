package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 移动端任务显示内容配置
 */
@Getter
public enum AppTaskShowEnum {

    TASK(10, "任务"),
    TYPE(20, "类型"),
    DATE(30, "截止日期"),
    RATE(40, "当前进度");

    private int code;
    private String desc;

    AppTaskShowEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (AppTaskShowEnum value : AppTaskShowEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (AppTaskShowEnum value : AppTaskShowEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }
}
