package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-模板指标项树")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOTempItem implements Serializable {

    private static final long serialVersionUID = 2172278360048880353L;
    @ApiModelProperty(value = "模板指标项ID")
    private Long id;

    @ApiModelProperty(value = "模板ID")
    private Long tempId;

    @ApiModelProperty(value = "基础指标ID")
    private Long baseItemId;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "基础指标项父ID")
    private Long baseItemPid;

    @ApiModelProperty(value = "层级")
    private Integer levelNum;

    @ApiModelProperty(value = "指标特征")
    private Integer feature;

}
