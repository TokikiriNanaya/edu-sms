package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 学段
 */
@Getter
public enum PeriodEnum {

    PRESCHOOL(10, "学前"),
    PRIMARY(20, "小学"),
    JUNIOR(30, "初中"),
    HIGH(40, "高中"),
    ROVATIONAL(50, "职高"),
    SPECIALIZED(60, "特殊教育");

    private int code;
    private String desc;

    PeriodEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (PeriodEnum value : PeriodEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (PeriodEnum value : PeriodEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }

    public static int getCodeByDesc(String desc) {
        for (PeriodEnum value : PeriodEnum.values()) {
            if (value.desc.equals(desc)) {
                return value.code;
            }
        }
        return -1;
    }
}
