package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 性别
 */
@Getter
public enum SexEnum {

    MAN(10, "男"),
    WOMEN(20, "女");

    private int code;
    private String desc;

    SexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (SexEnum value : SexEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (SexEnum value : SexEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }

    public static Integer getCodeByDesc(String desc) {
        for (SexEnum value : SexEnum.values()) {
            if (value.desc.equals(desc)) {
                return value.code;
            }
        }
        return null;
    }
}
