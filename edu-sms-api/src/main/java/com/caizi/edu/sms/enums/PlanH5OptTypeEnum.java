package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 待办任务操作类型（移动端）
 */
@Getter
public enum PlanH5OptTypeEnum {

    SELF("self", "自查"),
    DEPT("dept", "督查"),
    REFORM("reform", "整改");

    private String value;
    private String desc;

    PlanH5OptTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static boolean containValue(String value) {
        for (PlanH5OptTypeEnum operFlagEnum : PlanH5OptTypeEnum.values()) {
            if (operFlagEnum.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(String value) {
        for (PlanH5OptTypeEnum operFlagEnum : PlanH5OptTypeEnum.values()) {
            if (operFlagEnum.value.equals(value)) {
                return operFlagEnum.desc;
            }
        }
        throw new BizException("未找到编码为【" + value + "】的对应枚举值");
    }
}
