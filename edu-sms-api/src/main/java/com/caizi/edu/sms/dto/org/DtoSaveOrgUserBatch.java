package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "传入参数-添加机构用户")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoSaveOrgUserBatch implements Serializable {

    private static final long serialVersionUID = -8835799479679484837L;

    @ApiModelProperty(value = "机构ID，必传")
    private Long orgId;

    @ApiModelProperty(value = "学校用户类型，如果机构类型是学校、必传")
    private String bizUserType;

    @ApiModelProperty(value = "业务用户ID，必传")
    private List<Long> bizUserIdList;
}
