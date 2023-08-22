package com.caizi.edu.sms.dto.org;

import com.antnest.mscore.page.Pageable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-查询科室及分配学校量")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindDeptListAndSchoolStat implements Serializable {

    private static final long serialVersionUID = -6528285588461081232L;

    @ApiModelProperty(value = "分配状态")
    private Boolean hasSchoolStatus;

    @ApiModelProperty(value = "科室名称，模糊")
    private String deptName;

    @ApiModelProperty(value = "分页查询信息")
    private Pageable pageable;
}
