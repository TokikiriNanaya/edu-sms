package com.caizi.edu.sms.entity;

import com.antnest.mscore.base.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 指标模板
 *
 * @author 张培文
 * @date 2023/5/9 11:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_kpi_temp_main")
@ApiModel(value = "KpiTempMain对象", description = "指标模板")
public class KpiTempMain extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    public static final String NAME = "name";

    public static final String SORT_NUM = "sort_num";

}
