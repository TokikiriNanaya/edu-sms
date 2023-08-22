package com.caizi.edu.sms.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "视图-用户所属机构主视图")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOUserOrgMain implements Serializable {

    private static final long serialVersionUID = -7046243217366668637L;

    @ApiModelProperty(value = "业务用户ID")
    private Long bizUserId;

    @ApiModelProperty(value = "用户所属机构集合")
    private List<VOUserOrgInfo> userOrgList;
}
