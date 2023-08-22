package com.caizi.edu.sms.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-登录账号信息")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOLoginInfo implements Serializable {

    private static final long serialVersionUID = 2882851906545377763L;

    public static final String REDIS_USER_LAST_ROLE_CODE = "EDU_SMS_USER_LAST_ROLE_CODE_";

    public static final String REDIS_USER_LAST_ORG_ID = "EDU_SMS_USER_LAST_ORG_ID_";

    @ApiModelProperty(value = "登录账号中文名")
    private String cnName;

    @ApiModelProperty(value = "上一次登录使用角色编码")
    private String lastRoleCode;

    @ApiModelProperty(value = "上一次登录所属综合机构ID")
    private Long lastOrgId;

    @ApiModelProperty(value = "当前业务用户ID")
    private Long userId;
}
