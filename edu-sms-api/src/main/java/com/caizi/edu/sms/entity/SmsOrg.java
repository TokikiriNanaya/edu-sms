package com.caizi.edu.sms.entity;

import com.antnest.mscore.base.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 综合机构表
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_org")
@ApiModel(value = "Org对象", description = "综合机构表")
public class SmsOrg extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    public static final long ROOT_PID = 0L;
    public static final long ROOT_ID = 1L;
    public static final String SAME_NAME_SPLIT = "_";

    @ApiModelProperty(value = "组织节点名称")
    private String name;

    @ApiModelProperty(value = "上级节点id")
    private Long pid;

    @ApiModelProperty(value = "上级节点全路径名称")
    private String parentPathFull;

    @ApiModelProperty(value = "机构类型")
    private Integer orgType;

    @ApiModelProperty(value = "学段")
    private String period;

    @ApiModelProperty(value = "是否叶子节点 0:不是 1:是")
    private Boolean leafFlag;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;


    public static final String NAME = "name";

    public static final String PID = "pid";

    public static final String PARENT_PATH_FULL = "parent_path_full";

    public static final String ORG_TYPE = "org_type";

    public static final String PERIOD = "period";

    public static final String LEAF_FLAG = "leaf_flag";

    public static final String SORT_NUM = "sort_num";

}
