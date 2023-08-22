package com.caizi.edu.sms.entity;

import com.antnest.mscore.base.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 模板指标项
 *
 * @author 张培文
 * @date 2023/5/9 11:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_kpi_temp_item")
@ApiModel(value = "KpiTempItem对象", description = "模板指标项")
public class KpiTempItem extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板ID")
    private Long tempId;

    @ApiModelProperty(value = "基础指标ID")
    private Long baseItemId;

    @ApiModelProperty(value = "基础指标父节点ID")
    private Long baseItemPid;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    public static final String TEMP_ID = "temp_id";

    public static final String BASE_ITEM_ID = "base_item_id";

    public static final String BASE_ITEM_PID = "base_item_pid";

    public static final String SORT_NUM = "sort_num";
}
