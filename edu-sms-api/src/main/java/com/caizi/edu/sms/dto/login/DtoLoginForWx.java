package com.caizi.edu.sms.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-微信账号密码登录")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoLoginForWx implements Serializable {

    private static final long serialVersionUID = 4732688999921730615L;

    @ApiModelProperty(value = "图形验证码(输入)", required = true)
    private String random;

    @ApiModelProperty(value = "图形验证识别码", required = true)
    private String randomKey;

    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    @ApiModelProperty(value = "Md5后的密码", required = true)
    private String password;

    @ApiModelProperty(value = "应用代码", required = true)
    private String bizCode;

    /*
     微信特有参数
     */
    @ApiModelProperty(value = "openId", required = true)
    private String openId;

    @ApiModelProperty(value = "注册入口(公众号:offi-account,小程序:mini-program)", required = true)
    private String entry;
}
