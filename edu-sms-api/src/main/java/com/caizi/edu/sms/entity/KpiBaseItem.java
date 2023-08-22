package com.caizi.edu.sms.entity;

import com.antnest.mscore.base.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 基础指标项
 *
 * @author Kiriya
 * @date 2023/5/9 10:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_kpi_base_item")
@ApiModel(value = "KpiBaseItem对象", description = "基础指标项")
public class KpiBaseItem extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "父节点ID")
    private Long pid;

    @ApiModelProperty(value = "上级节点全路径名称")
    private String parentPathFull;

    @ApiModelProperty(value = "层级")
    private Integer levelNum;

    @ApiModelProperty(value = "指标特征")
    private Integer feature;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    public static final String NAME = "name";

    public static final String CODE = "code";

    public static final String PID = "pid";

    public static final String PARENT_PATH_FULL = "parent_path_full";

    public static final String LEVEL_NUM = "level_num";

    public static final String FEATURE = "feature";

    public static final String SORT_NUM = "sort_num";


}
