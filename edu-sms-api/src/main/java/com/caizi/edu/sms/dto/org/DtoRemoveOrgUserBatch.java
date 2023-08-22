package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "传入参数-删除机构用户")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoRemoveOrgUserBatch implements Serializable {

    private static final long serialVersionUID = 1041920358254009454L;

    @ApiModelProperty(value = "机构ID，必传")
    private Long orgId;

    @ApiModelProperty(value = "业务用户ID集合，必传")
    private List<Long> bizUserIdList;

    @ApiModelProperty(value = "用户类型")
    private String bizUserType;
}
