package com.caizi.edu.sms.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-用户所属机构详情")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOUserOrgInfo implements Serializable {

    private static final long serialVersionUID = -1049380597388770211L;

    @ApiModelProperty(value = "所属机构ID")
    private Long orgId;

    @ApiModelProperty(value = "所属机构名称")
    private String orgName;
}
