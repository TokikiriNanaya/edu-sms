package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 证件类型
 */
@Getter
public enum IdTypeEnum {

    RESIDENT(10, "居民身份证"),
    OFFICER(20, "军官证"),
    SOLDIER(30, "士兵证"),
    NON_RANKING_OFFICER(40, "文职干部证"),
    ARMY_RETIRED(50, "部队离退休证"),
    HONG_KONG(60, "香港特区护照/身份证明"),
    AO_MEN(70, "澳门特区护照/身份证明"),
    TAIWAN(80, "台湾居民来往大陆通行证"),
    OUTSIDE_BORDERS(90, "境外永久居住证"),
    PASSPORT(100, "护照"),
    REGISTERED_RESIDENCE(110, "户口薄"),
    OTHER(900, "其他");

    private int code;
    private String desc;

    IdTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (IdTypeEnum value : IdTypeEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (IdTypeEnum value : IdTypeEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }

    public static Integer getCodeByDesc(String desc) {
        for (IdTypeEnum value : IdTypeEnum.values()) {
            if (value.desc.equals(desc)) {
                return value.code;
            }
        }
        return null;
    }
}
