package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-查询学校树")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindSchoolTree implements Serializable {

    private static final long serialVersionUID = 7220085089430682280L;

    @ApiModelProperty(value = "学段、学校名称，模糊")
    private String keyword;
}
