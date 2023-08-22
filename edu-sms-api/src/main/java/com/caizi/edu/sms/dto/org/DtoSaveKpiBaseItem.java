package com.caizi.edu.sms.dto.org;

import com.caizi.edu.sms.entity.KpiBaseItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-保存基础指标项")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoSaveKpiBaseItem implements Serializable {

    private static final long serialVersionUID = 565108011453022735L;

    @ApiModelProperty(value = "指标项ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "父节点ID")
    private Long pid;

    @ApiModelProperty(value = "指标特征")
    private Integer feature;

    public static void convertToKpiBaseItem(DtoSaveKpiBaseItem dto, KpiBaseItem kpiBaseItem) {
        if (dto == null) return;
        if (kpiBaseItem == null) return;
        kpiBaseItem.setId(dto.getId());
        kpiBaseItem.setName(dto.getName());
        kpiBaseItem.setCode(dto.getCode());
        kpiBaseItem.setPid(dto.getPid());
        kpiBaseItem.setFeature(dto.getFeature());
    }

}
