package com.caizi.edu.sms.dto.user;

import com.caizi.edu.sms.entity.BizUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-保存用户")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoSaveUser implements Serializable {

    private static final long serialVersionUID = -2659876435362585325L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "证件类型")
    private Integer idType;

    @ApiModelProperty(value = "证件号码")
    private String idNumber;

    @ApiModelProperty(value = "备注")
    private String remark;

    public static void convertToBizUser(DtoSaveUser dto, BizUser bizUser) {
        if (dto == null) return;
        if (bizUser == null) return;
        bizUser.setName(dto.getName());
        bizUser.setPhone(dto.getPhone());
        bizUser.setIdType(dto.getIdType());
        bizUser.setIdNumber(dto.getIdNumber());
        bizUser.setRemark(dto.getRemark());
    }
}
