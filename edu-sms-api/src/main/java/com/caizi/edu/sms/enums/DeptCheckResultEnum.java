package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 督查结果
 */
@Getter
public enum DeptCheckResultEnum {

    PROBLEM(-10, "异常"),
    NORMAL(10, "正常");

    private int code;
    private String desc;

    DeptCheckResultEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (DeptCheckResultEnum value : DeptCheckResultEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (DeptCheckResultEnum value : DeptCheckResultEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }
}
