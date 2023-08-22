package com.caizi.edu.sms.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-更新最近一次登录身份")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoUpdateLastLoginRole implements Serializable {

    private static final long serialVersionUID = 3778570573278924816L;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;
}
