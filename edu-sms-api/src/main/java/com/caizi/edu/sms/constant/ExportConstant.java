package com.caizi.edu.sms.constant;

public class ExportConstant {
    /**
     * 模板文件存储目录
     */
    public static final String TEMPLATE_DIR = "templates/";

    /**
     * 文件后缀名开头，用于获取后缀名称
     */
    public static final String FILE_POINT = ".";

    /**
     * 03版Excel文件后缀
     */
    public static final String FILE_XLS = ".xls";

    /**
     * 07版Excel文件后缀
     */
    public static final String FILE_XLSX = ".xlsx";

    /**
     * 03版Word文件后缀
     */
    public static final String FILE_DOC = ".doc";

    /**
     * 07版Word文件后缀
     */
    public static final String FILE_DOCX = ".docx";

    /**
     * REDIS KEY：导入机构结果状态
     */
    public static final String REDIS_KEY_STATUS_ORG = "IMP_STATUS_ORG";
    /**
     * REDIS KEY：导入机构进度
     */
    public static final String REDIS_KEY_RATE_SUCCESS_ORG = "IMP_RATE_SUCCESS_ORG";
    /**
     * REDIS KEY：导入机构总量
     */
    public static final String REDIS_KEY_RATE_TOTAL_ORG = "IMP_RATE_TOTAL_ORG";

    /**
     * REDIS KEY：导入用户结果状态
     */
    public static final String REDIS_KEY_STATUS_USER = "IMP_STATUS_USER";
    /**
     * REDIS KEY：导入用户进度
     */
    public static final String REDIS_KEY_RATE_SUCCESS_USER = "IMP_RATE_SUCCESS_USER";
    /**
     * REDIS KEY：导入用户总量
     */
    public static final String REDIS_KEY_RATE_TOTAL_USER = "IMP_RATE_TOTAL_USER";

    /**
     * REDIS KEY：导入用户结果状态
     */
    public static final String REDIS_KEY_STATUS_USER_SCHOOL = "IMP_STATUS_USER_SCHOOL";
    /**
     * REDIS KEY：导入用户进度
     */
    public static final String REDIS_KEY_RATE_SUCCESS_USER_SCHOOL = "IMP_RATE_SUCCESS_USER_SCHOOL";
    /**
     * REDIS KEY：导入用户总量
     */
    public static final String REDIS_KEY_RATE_TOTAL_USER_SCHOOL = "IMP_RATE_TOTAL_USER_SCHOOL";

    /**
     * REDIS KEY：导入基础指标项结果状态
     */
    public static final String REDIS_KEY_STATUS_KPI_BASE_ITEM = "IMP_STATUS_KPI_BASE_ITEM";
    /**
     * REDIS KEY：导入基础指标项进度
     */
    public static final String REDIS_KEY_RATE_SUCCESS_KPI_BASE_ITEM = "IMP_RATE_SUCCESS_KPI_BASE_ITEM";
    /**
     * REDIS KEY：导入基础指标项总量
     */
    public static final String REDIS_KEY_RATE_TOTAL_KPI_BASE_ITEM = "IMP_RATE_TOTAL_KPI_BASE_ITEM";

    /**
     * REDIS KEY：导入安保人员结果状态
     */
    public static final String REDIS_KEY_STATUS_SECURITY_PERSON = "IMP_STATUS_SECURITY_PERSON";
    /**
     * REDIS KEY：导入安保人员进度
     */
    public static final String REDIS_KEY_RATE_SUCCESS_SECURITY_PERSON = "IMP_RATE_SUCCESS_SECURITY_PERSON";
    /**
     * REDIS KEY：导入安保人员总量
     */
    public static final String REDIS_KEY_RATE_TOTAL_SECURITY_PERSON = "IMP_RATE_TOTAL_SECURITY_PERSON";

    /**
     * REDIS KEY：导入危化品结果状态
     */
    public static final String REDIS_KEY_STATUS_HAZARDOUS_CHEMICALS = "IMP_STATUS_HAZARDOUS_CHEMICALS";
    /**
     * REDIS KEY：导入值班信息结果状态
     */
    public static final String REDIS_KEY_STATUS_DUTY_INFO = "IMP_STATUS_DUTY_INFO";
    /**
     * REDIS KEY：导入突发事件结果状态
     */
    public static final String REDIS_KEY_STATUS_EMERGENCY = "IMP_STATUS_EMERGENCY";
    /**
     * REDIS KEY：导入危化品进度
     */
    public static final String REDIS_KEY_RATE_SUCCESS_HAZARDOUS_CHEMICALS = "IMP_RATE_SUCCESS_HAZARDOUS_CHEMICALS";
    /**
     * REDIS KEY：导入值班信息
     */
    public static final String REDIS_KEY_RATE_SUCCESS_DUTY_INFO = "IMP_RATE_SUCCESS_DUTY_INFO";
    /**
     * REDIS KEY：导入突发事件
     */
    public static final String REDIS_KEY_RATE_SUCCESS_EMERGENCY = "IMP_RATE_SUCCESS_EMERGENCY";
    /**
     * REDIS KEY：导入危化品总量
     */
    public static final String REDIS_KEY_RATE_TOTAL_HAZARDOUS_CHEMICALS = "IMP_RATE_TOTAL_HAZARDOUS_CHEMICALS";
    /**
     * REDIS KEY：导入值班信息
     */
    public static final String REDIS_KEY_RATE_TOTAL_DUTY_INFO = "IMP_RATE_TOTAL_DUTY_INFO";
    /**
     * REDIS KEY：导入突发事件
     */
    public static final String REDIS_KEY_RATE_TOTAL_EMERGENCY = "IMP_RATE_TOTAL_EMERGENCY";

    /**
     * REDIS KEY：导入安全设施结果状态
     */
    public static final String REDIS_KEY_STATUS_SAFETY_DEVICE = "IMP_STATUS_SAFETY_DEVICE";
    /**
     * REDIS KEY：导入安全设施进度
     */
    public static final String REDIS_KEY_RATE_SUCCESS_SAFETY_DEVICE = "IMP_RATE_SUCCESS_SAFETY_DEVICE";
    /**
     * REDIS KEY：导入安全设施总量
     */
    public static final String REDIS_KEY_RATE_TOTAL_SAFETY_DEVICE = "IMP_RATE_TOTAL_SAFETY_DEVICE";

    /**
     * 导入模板名称-机构
     */
    public static final String TEMP_NAME_ORG = "temp_org" + FILE_XLSX;
    /**
     * 导入模板名称-用户
     */
    public static final String TEMP_NAME_USER = "temp_user" + FILE_XLSX;
    /**
     * 导入模板名称-用户（校管）
     */
    public static final String TEMP_NAME_USER_SCHOOL = "temp_user_school" + FILE_XLSX;
    /**
     * 导入模板名称-基础指标项
     */
    public static final String TEMP_NAME_KPI_BASE_ITEM = "temp_kpi_base_item" + FILE_XLSX;
    /**
     * 导入模板名称-基础指标项(新)
     */
    public static final String TEMP_NAME_KPI_BASE_ITEM_NEW = "temp_kpi_base_item_new" + FILE_XLSX;

    /**
     * 导入模板名称-安保人员_区管
     */
    public static final String TEMP_NAME_SECURITY_PERSON_AREA = "temp_security_person_area" + FILE_XLSX;
    /**
     * 导入模板名称-安保人员_学校
     */
//    public static final String TEMP_NAME_SECURITY_PERSON_SCHOOL = "temp_security_person_school" + FILE_XLSX;
    /**
     * 导入模板名称-危化品_区管
     */
    public static final String TEMP_NAME_HAZARDOUS_CHEMICALS_AREA = "temp_hazardous_chemicals_area" + FILE_XLSX;
    /**
     * 导入模板名称-危化品_学校
     */
//    public static final String TEMP_NAME_HAZARDOUS_CHEMICALS_SCHOOL = "temp_hazardous_chemicals_school" + FILE_XLSX;
    /**
     * 导入模板名称-安全设施_区管
     */
    public static final String TEMP_NAME_SAFE_DRIVE_AREA = "temp_safe_drive_area" + FILE_XLSX;
    /**
     * 导入模板名称-值班信息
     */
    public static final String TEMP_NAME_DUTY_INFO = "temp_duty_info" + FILE_XLSX;
    /**
     * 导入模板名称-值班信息
     */
    public static final String TEMP_NAME_EMERGENCY_EVENT = "temp_emergency_event_info" + FILE_XLSX;
    /**
     * 导入模板名称-安全设施_区管
     */
//    public static final String TEMP_NAME_SAFE_DRIVE_SCHOOL = "temp_safe_drive_school" + FILE_XLSX;
}
