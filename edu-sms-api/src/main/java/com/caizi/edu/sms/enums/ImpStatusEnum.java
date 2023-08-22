package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 导入状态
 */
@Getter
public enum ImpStatusEnum {

    FAIL(-20, "导入失败"),
    NOT_FOUND(-10, "未查询到导入记录"),
    FINISH(0, "导入完成"),
    INIT(10, "导入开始");

    private int code;
    private String desc;

    ImpStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (ImpStatusEnum value : ImpStatusEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (ImpStatusEnum value : ImpStatusEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的导入状态编码");
    }
}
