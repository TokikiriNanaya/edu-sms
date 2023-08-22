package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 检查报告状态
 */
@Getter
public enum CheckReportStatusEnum {

    PUBLISHED(0, "已发布"),
    INIT(10, "未生成"),
    PRODUCED(20, "已生成、未发布");

    private int code;
    private String desc;

    CheckReportStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (CheckReportStatusEnum value : CheckReportStatusEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (CheckReportStatusEnum value : CheckReportStatusEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }
}
