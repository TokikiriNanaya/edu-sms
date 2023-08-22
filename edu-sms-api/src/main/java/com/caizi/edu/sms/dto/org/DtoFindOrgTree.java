package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-查询机构树")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindOrgTree implements Serializable {

    private static final long serialVersionUID = 879439558692485883L;

    @ApiModelProperty(value = "机构名称，模糊")
    private String orgName;
}
