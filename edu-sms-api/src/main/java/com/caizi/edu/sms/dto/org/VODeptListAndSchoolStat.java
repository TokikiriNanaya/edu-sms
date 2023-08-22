package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-查询科室及分配学校量")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VODeptListAndSchoolStat implements Serializable {

    private static final long serialVersionUID = -3446786893337583411L;

    @ApiModelProperty(value = "科室ID")
    private Long deptId;

    @ApiModelProperty(value = "科室名称")
    private String deptName;

    @ApiModelProperty(value = "分配学校数量")
    private int schoolNum;

    @ApiModelProperty(value = "分配状态")
    private boolean schoolStatus;
}
