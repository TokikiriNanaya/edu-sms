package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 机构类型
 */
@Getter
public enum OrgTypeEnum {

    CUSTOM(10, "自定义"),
    SCHOOL(20, "学校");

    private int code;
    private String desc;

    OrgTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (OrgTypeEnum value : OrgTypeEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (OrgTypeEnum value : OrgTypeEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }

    public static Integer getCodeByDesc(String desc) {
        for (OrgTypeEnum value : OrgTypeEnum.values()) {
            if (value.desc.equals(desc)) {
                return value.code;
            }
        }
        return null;
    }
}
