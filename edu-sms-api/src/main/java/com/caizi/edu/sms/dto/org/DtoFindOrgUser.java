package com.caizi.edu.sms.dto.org;

import com.antnest.mscore.page.Pageable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-查询机构用户")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindOrgUser implements Serializable {

    private static final long serialVersionUID = -5666106874767556363L;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "查询用户类型")
    private String bizUserType;

    @ApiModelProperty(value = "姓名、电话、证件号码")
    private String keyword;

    @ApiModelProperty(value = "分页查询信息")
    private Pageable pageable;
}
