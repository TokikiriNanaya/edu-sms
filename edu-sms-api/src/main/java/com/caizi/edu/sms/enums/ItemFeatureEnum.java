package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 指标特征
 */
@Getter
public enum ItemFeatureEnum {

    SUBJECTIVE(10, "主观指标"),
    OBJECTIVE(20, "客观指标");

    private int code;
    private String desc;

    ItemFeatureEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (ItemFeatureEnum value : ItemFeatureEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (ItemFeatureEnum value : ItemFeatureEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }

    public static Integer getCodeByDesc(String desc) {
        for (ItemFeatureEnum value : ItemFeatureEnum.values()) {
            if (value.desc.equals(desc)) {
                return value.code;
            }
        }
        return null;
    }
}
