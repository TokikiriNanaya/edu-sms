package com.caizi.edu.sms.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-登录身份")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOLoginRole implements Serializable {

    private static final long serialVersionUID = 1261498782944195697L;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "身份优先级排序号")
    private Integer sortNum;
}
