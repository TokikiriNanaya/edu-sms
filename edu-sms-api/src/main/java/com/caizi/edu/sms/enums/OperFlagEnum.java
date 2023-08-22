package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 任务指标项操作标识
 */
@Getter
public enum OperFlagEnum {

    UP(10, "上移"),
    DOWN(20, "下移");

    private int value;
    private String desc;

    OperFlagEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static boolean containValue(int value) {
        for (OperFlagEnum operFlagEnum : OperFlagEnum.values()) {
            if (operFlagEnum.value == value) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int value) {
        for (OperFlagEnum operFlagEnum : OperFlagEnum.values()) {
            if (operFlagEnum.value == value) {
                return operFlagEnum.desc;
            }
        }
        throw new BizException("未找到编码为【" + value + "】的对应枚举值");
    }
}
