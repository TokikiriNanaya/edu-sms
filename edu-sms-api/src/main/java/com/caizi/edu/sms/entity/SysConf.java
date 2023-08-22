package com.caizi.edu.sms.entity;

import com.antnest.mscore.base.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_sys_conf")
@ApiModel(value = "SysConf对象", description = "系统配置表")
public class SysConf extends AbstractBaseEntity {

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "微信公众号appId")
    private String wxAppId;


    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统名称")
    private String name;

    @ApiModelProperty(value = "版权信息")
    private String copyright;

    @ApiModelProperty(value = "备案信息")
    private String keepRecord;

    @ApiModelProperty(value = "状态 0:禁用 1:启用")
    private Boolean status;

    @ApiModelProperty(value = "月报上传时间开始")
    private String monthReportBegin;

    @ApiModelProperty(value = "月报上传时间结束")
    private String monthReportEnd;

    @ApiModelProperty(value = "月报上传状态")
    private Boolean monthReportStatus;

    @ApiModelProperty(value = "app首页名称")
    private String appMainPageName;

    @ApiModelProperty(value = "app列表名称")
    private String appListName;

    @ApiModelProperty(value = "app扩展显示")
    private String appTaskShow;

    @ApiModelProperty(value = "同步大数据平台配置")
    private Integer syncBigDataPlat;


    public static final String NAME = "name";

    public static final String COPYRIGHT = "copyright";

    public static final String KEEP_RECORD = "keep_record";

    public static final String STATUS = "status";

    public static final String MONTH_REPORT_BEGIN = "month_report_begin";

    public static final String MONTH_REPORT_END = "month_report_end";

    public static final String MONTH_REPORT_STATUS = "month_report_status";

    public static final String APP_MAIN_PAGE_NAME = "app_main_page_name";

    public static final String APP_LIST_NAME = "app_list_name";

    public static final String APP_TASK_SHOW = "app_task_show";

    public static final String SYNC_BIG_DATA_PLAT = "sync_big_data_plat";

}
