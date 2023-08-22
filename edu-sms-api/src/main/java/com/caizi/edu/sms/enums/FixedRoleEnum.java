package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 固定角色编码定义
 */
@Getter
public enum FixedRoleEnum {

    AREA_ADMIN("areaAdmin", "区管理员"),
    DEPT_USER("deptUser", "科室人员"),
    SCHOOL_ADMIN("schoolAdmin", "校管理员"),
    SCHOOL_USER("schoolUser", "校责任人"),
    OPER_ADMIN("operAdmin", "运维人员");

    private String roleCode;
    private String desc;

    FixedRoleEnum(String roleCode, String desc) {
        this.roleCode = roleCode;
        this.desc = desc;
    }

    public static boolean containCode(String roleCode) {
        for (FixedRoleEnum value : FixedRoleEnum.values()) {
            if (value.roleCode.equals(roleCode)) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(String roleCode) {
        for (FixedRoleEnum value : FixedRoleEnum.values()) {
            if (value.roleCode.equals(roleCode)) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + roleCode + "】的角色定义");
    }
}
