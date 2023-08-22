package com.caizi.edu.sms.dto.user;

import com.antnest.mscore.page.Pageable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "传入参数-分页查询用户列表通用条件")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindPageCondition implements Serializable {

    private static final long serialVersionUID = -3151939612300406635L;

    @ApiModelProperty(value = "限定业务用户ID范围")
    private List<Long> userIdList;

    @ApiModelProperty(value = "姓名、电话、证件号码")
    private String keyword;

    @ApiModelProperty(value = "状态")
    private Integer bizUserStatus;

    @ApiModelProperty(value = "分页查询信息")
    private Pageable pageable;
}
