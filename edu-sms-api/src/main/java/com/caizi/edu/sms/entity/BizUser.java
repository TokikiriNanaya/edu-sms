package com.caizi.edu.sms.entity;

import com.antnest.mscore.base.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务用户表
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_biz_user")
@ApiModel(value = "BizUser对象", description = "业务用户表")
public class BizUser extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "证件类型")
    private Integer idType;

    @ApiModelProperty(value = "证件号码")
    private String idNumber;

    @ApiModelProperty(value = "用户状态")
    private Integer status;


    public static final String NAME = "name";

    public static final String PHONE = "phone";

    public static final String ID_TYPE = "id_type";

    public static final String ID_NUMBER = "id_number";

    public static final String STATUS = "status";


    public static Map<String, BizUser> getByPhone(List<BizUser> inList) {
        Map<String, BizUser> outMap = new HashMap<>();// key：检查任务ID，value：参检学校集合
        if (inList == null || inList.size() == 0) return outMap;
        for (BizUser bizUser : inList) {
            if (bizUser == null) continue;
            outMap.put(bizUser.getPhone(), bizUser);
        }
        return outMap;
    }
}
