package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 用户状态
 */
@Getter
public enum BizUserStatusEnum {

    DISABLE(-10, "禁用"),
    ENABLE(10, "启用");

    private int code;
    private String desc;

    BizUserStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (BizUserStatusEnum value : BizUserStatusEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (BizUserStatusEnum value : BizUserStatusEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }
}
