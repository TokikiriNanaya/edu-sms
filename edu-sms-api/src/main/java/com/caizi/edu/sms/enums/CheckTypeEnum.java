package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 检查类型
 */
@Getter
public enum CheckTypeEnum {

    SELF(10, "自查"),
    DEPT(20, "督查"),
    SELF_AND_DEPT(30, "自查+督查");

    private int code;
    private String desc;

    CheckTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (CheckTypeEnum value : CheckTypeEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (CheckTypeEnum value : CheckTypeEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }
}
