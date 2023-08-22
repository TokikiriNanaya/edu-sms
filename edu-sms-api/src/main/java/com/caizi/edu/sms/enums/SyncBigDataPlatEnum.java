package com.caizi.edu.sms.enums;


import com.antnest.mscore.exception.BizException;
import lombok.Getter;

/**
 * 同步大数据平台配置
 */
@Getter
public enum SyncBigDataPlatEnum {

    CLOSE(0, "关闭"),
    OPEN_HOLD(10, "开启-保留"),
    OPEN_PASS(20, "开启-跳过"),
    OPEN_OVERWRITE(30, "开启-替换");

    private int code;
    private String desc;

    SyncBigDataPlatEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean containCode(int code) {
        for (SyncBigDataPlatEnum value : SyncBigDataPlatEnum.values()) {
            if (value.code == code) {
                return true;
            }
        }
        return false;
    }

    public static String getDesc(int code) {
        for (SyncBigDataPlatEnum value : SyncBigDataPlatEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new BizException("未找到编码为【" + code + "】的对应枚举值");
    }
}
