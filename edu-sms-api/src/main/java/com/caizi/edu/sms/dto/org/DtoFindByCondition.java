package com.caizi.edu.sms.dto.org;

import com.antnest.mscore.page.Pageable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "传入参数-通用查询条件封装")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoFindByCondition implements Serializable {

    private static final long serialVersionUID = 3502764608942469309L;

    @ApiModelProperty(value = "机构ID查询范围")
    private List<Long> orgIdList;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "学段")
    private Integer period;

    @ApiModelProperty(value = "分页查询信息")
    private Pageable pageable;
}
