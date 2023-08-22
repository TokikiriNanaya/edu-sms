package com.caizi.edu.sms.dto.user;

import com.antnest.mscore.page.Pageable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-查询用户")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindUser implements Serializable {

    private static final long serialVersionUID = 2147759472242221626L;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "状态")
    private Integer bizUserStatus;

    @ApiModelProperty(value = "查询用户类型")
    private String bizUserType;

    @ApiModelProperty(value = "业务用户姓名，模糊")
    private String bizUserName;

    @ApiModelProperty(value = "分页查询信息")
    private Pageable pageable;
}
