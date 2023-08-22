package com.caizi.edu.sms.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-校验用户名结果")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOUserNameCheckResult implements Serializable {

    private static final long serialVersionUID = -620107901449683479L;

    public static final String REDIS_KEY = "EDU_SMS_RANDOM_NAME_KEY_";

    public static final String REDIS_KEY_SMS_HAS_CHECK = "EDU_SMS_RANDOM_NAME_KEY_SMS_HAS_CHECK_";

    public static final String SMS_CHECK_PASS = "1";

    public static final String SMS_CHECK_UN_PASS = "0";


    @ApiModelProperty(value = "用户名校验通过后的临时Key，只能使用一次")
    private String randomNameKey;

    @ApiModelProperty(value = "加密后的手机号码")
    private String phoneStr;
}
