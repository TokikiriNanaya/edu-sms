package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 查询整改指标项树标识定义
 */
@Getter
public enum ReformFlagEnum {

    ALL("all", "查全部需整改项"),
    TRUE("true", "查已整改项"),
    FALSE("false", "查未整改项");

    private String code;
    private String desc;

    ReformFlagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(String code) {
        for (ReformFlagEnum value : ReformFlagEnum.values()) {
            if (value.code.equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(String code) {
        for (ReformFlagEnum value : ReformFlagEnum.values()) {
            if (value.code.equals(code)) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的定义");
    }
}
