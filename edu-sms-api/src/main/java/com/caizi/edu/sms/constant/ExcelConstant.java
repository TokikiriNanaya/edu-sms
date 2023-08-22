package com.caizi.edu.sms.constant;

public class ExcelConstant {
    /**
     * 模板文件存储目录
     */
    public static final String TEMPLATE_DIR="templates/";

    /**
     * 文件后缀名开头，用于获取后缀名称
     */
    public static final String FILE_POINT=".";

    /**
     * 03版Excel文件后缀
     */
    public static final String FILE_XLS=".xls";

    /**
     * 07版Excel文件后缀
     */
    public static final String FILE_XLSX=".xlsx";

    /**
     * Excel导出的Redis键
     */
    public static final String REDIS_EXPORT="EXPORT_MAP";

    /**
     * Excel导出进度的Redis键
     */
    public static final String REDIS_EXPORT_SIZE="EXPORT_SIZE";

    /**
     * Excel导出进度的Redis键
     */
    public static final String REDIS_EXPORT_RATE="EXPORT_RATE";
}
