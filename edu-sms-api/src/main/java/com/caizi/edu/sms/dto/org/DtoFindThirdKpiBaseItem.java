package com.caizi.edu.sms.dto.org;

import com.antnest.mscore.page.Pageable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "传入参数-查询三级选项列表")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindThirdKpiBaseItem implements Serializable {

    private static final long serialVersionUID = 6156908403846062428L;
    @ApiModelProperty(value = "指标项ID")
    private Long selectItemId;

    @ApiModelProperty(value = "指标特征")
    private Integer feature;

    @ApiModelProperty(value = "指标名称，模糊")
    private String name;

    @ApiModelProperty(value = "分页查询信息")
    private Pageable pageable;
}
