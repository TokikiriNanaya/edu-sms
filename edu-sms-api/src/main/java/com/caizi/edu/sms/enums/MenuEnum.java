package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 核心业务菜单编码
 */
@Getter
public enum MenuEnum {

    CHECK_TASK("checkTask", "检查任务"),
    DEPT_CHECK_DEPT("deptCheck_dept", "督查（科室）"),
    SELF_CHECK_SCHOOL("selfCheck_school", "自查（校方）");

    private String code;
    private String desc;

    MenuEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(String code) {
        for (MenuEnum value : MenuEnum.values()) {
            if (value.code.equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(String code) {
        for (MenuEnum value : MenuEnum.values()) {
            if (value.code.equals(code)) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的菜单定义");
    }
}
