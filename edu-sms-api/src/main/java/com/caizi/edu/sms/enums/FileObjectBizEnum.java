package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 附件主对象业务编码
 */
@Getter
public enum FileObjectBizEnum {

    SYSCONF_LOGO("SysConf||logo", "系统配置-LOGO"),
    SYSCONF_ICON("SysConf||icon", "系统配置-ICON"),
    SYSCONF_APP("SysConf||app", "系统配置-移动端封面"),

    BIZUSER_COVER("BizUser||cover", "业务用户-封面图片"),

    SELFCHECK_ITEM("SelfCheck||item", "自查-检查项"),
    SELFCHECK_OTHER("SelfCheck||other", "自查-其它"),
    DEPTCHECK_ITEM("DeptCheck||item", "督查-检查项"),
    DEPTCHECK_OTHER("DeptCheck||other", "督查-其它"),
    REFORM_ITEM("Reform||item", "整改-检查项"),
    REFORM_OTHER("Reform||other", "整改-其它"),
    REFORM_REPORT("Reform||Report", "整改-报告"),

    TASKPLAN_CHECKREPORT("TaskPlan||checkReport", "检查任务-总报告"),
    TaskSchool_CHECKREPORT("TaskSchool||checkReport", "参检学校-分报告"),

    MONTHREPORT_MAIN("MonthReport||main", "月报-月报文件"),
    MONTHREPORT_STANDINGBOOK("MonthReport||standingBook", "月报-台账文件");

    private String code;
    private String name;

    FileObjectBizEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static boolean containCode(String code) {
        for (FileObjectBizEnum value : FileObjectBizEnum.values()) {
            if (value.code.equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(String code) {
        for (FileObjectBizEnum value : FileObjectBizEnum.values()) {
            if (value.code.equals(code)) {
                return value.name;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的附件主对象业务编码");
    }
}
