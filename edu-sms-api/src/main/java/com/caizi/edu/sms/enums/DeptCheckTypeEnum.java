package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 督查类型
 */
@Getter
public enum DeptCheckTypeEnum {

    ON_THE_SPOT(10, "实地督查"),
    ONLINE(20, "线上督查"),
    VIDEO(30, "视频督查");

    private int code;
    private String desc;

    DeptCheckTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (DeptCheckTypeEnum value : DeptCheckTypeEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (DeptCheckTypeEnum value : DeptCheckTypeEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }
}
