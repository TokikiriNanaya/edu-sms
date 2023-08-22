package com.caizi.edu.sms.constant;

public class ApplicationInfo {

    /**
     * 应用名称（请注意：如果需要注册到注册中心，应用名称也使用此值，方便进行openfeign的远程调用）
     */
    public static final String ANTNEST_SERVICE_NAME = "EDU-SMS";
    /**
     * auth 应用统一路径前缀
     */
    public static final String ANTNEST_BASE_PATH = "";

    /**
     * auth 应用统一路径前缀
     */
    public static final String APPLICATION_CODE = "EDU_SMS_PC_ROOT";

    /**
     * 默认初始密码
     */
    public static final String INIT_PASSWORD = "abc12345";

    /**
     * 移动端二维码名称
     */
    public static final String APP_QR_CODE_NAME = "appQrCode.jpg";
}
