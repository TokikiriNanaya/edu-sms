package com.caizi.edu.sms.dto.org;

import com.antnest.mscore.page.Pageable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-查询科室已分配学校列表")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindSchoolListOfDept implements Serializable {

    private static final long serialVersionUID = -6109677300773832778L;

    @ApiModelProperty(value = "科室ID")
    private Long deptId;

    @ApiModelProperty(value = "学校名称，模糊")
    private String schoolName;

    @ApiModelProperty(value = "学段")
    private Integer period;

    @ApiModelProperty(value = "分页查询信息")
    private Pageable pageable;
}
