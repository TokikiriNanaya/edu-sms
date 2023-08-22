package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-查询学校归属科室")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VODeptOfSchool implements Serializable {

    private static final long serialVersionUID = 2606878734996188944L;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "所属科室ID")
    private Long deptId;

    @ApiModelProperty(value = "所属科室名称")
    private String deptName;
}
