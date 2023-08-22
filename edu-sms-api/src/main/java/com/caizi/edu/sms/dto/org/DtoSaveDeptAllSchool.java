package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "传入参数-保存科室分配学校")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoSaveDeptAllSchool implements Serializable {

    private static final long serialVersionUID = 8554321380228546116L;

    @ApiModelProperty(value = "科室ID")
    private Long deptId;

    @ApiModelProperty(value = "学校ID")
    private List<Long> schoolIdList;
}
