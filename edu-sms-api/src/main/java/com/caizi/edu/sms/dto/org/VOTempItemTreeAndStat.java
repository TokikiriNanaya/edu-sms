package com.caizi.edu.sms.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "视图-模板指标项树及统计信息")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOTempItemTreeAndStat implements Serializable {

    private static final long serialVersionUID = 584251475786365076L;

    @ApiModelProperty(value = "模板指标项树")
    private List<VOTempItem> mainData;

    @ApiModelProperty(value = "一级指标统计量")
    private Integer firstLevel;

    @ApiModelProperty(value = "二级指标统计量")
    private Integer secondLevel;

    @ApiModelProperty(value = "三级指标统计量")
    private Integer thirdLevel;
}
