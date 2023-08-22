package com.caizi.edu.sms.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "传入参数-批量启用/禁用用户")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoChangeUserStatusBatch implements Serializable {

    private static final long serialVersionUID = -480111609797564761L;

    @ApiModelProperty(value = "操作标识，必传")
    private Integer operFlag;

    @ApiModelProperty(value = "业务用户ID，必传")
    private List<Long> bizUserIdList;
}
