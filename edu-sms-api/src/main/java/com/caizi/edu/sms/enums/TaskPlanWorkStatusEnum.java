package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 检查任务流转状态
 */
@Getter
public enum TaskPlanWorkStatusEnum {

    OVER(0, "完结"),
    INIT(10, "草稿"),
    PUBLISH(10, "已发布");

    private int code;
    private String desc;

    TaskPlanWorkStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (TaskPlanWorkStatusEnum value : TaskPlanWorkStatusEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (TaskPlanWorkStatusEnum value : TaskPlanWorkStatusEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }
}
