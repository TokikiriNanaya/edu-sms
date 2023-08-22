package com.caizi.edu.sms.dto.org;

import com.caizi.edu.sms.entity.SmsOrg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-保存机构")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoSaveOrg implements Serializable {

    private static final long serialVersionUID = 3397252397797250081L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "组织节点名称")
    private String name;

    @ApiModelProperty(value = "上级节点id")
    private Long pid;

    @ApiModelProperty(value = "机构类型")
    private Integer orgType;

    @ApiModelProperty(value = "学段")
    private String period;

    @ApiModelProperty(value = "备注")
    private String remark;

    public static void convertToSmsOrg(DtoSaveOrg dto, SmsOrg smsOrg) {
        if (dto == null) return;
        if (smsOrg == null) return;
        smsOrg.setName(dto.getName());
        smsOrg.setPid(dto.getPid());
        smsOrg.setOrgType(dto.getOrgType());
        smsOrg.setPeriod(dto.getPeriod());
        smsOrg.setRemark(dto.getRemark());
    }
}
