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
 * 科室学校分配
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_org_school")
@ApiModel(value = "OrgSchool对象", description = "科室学校分配")
public class OrgSchool extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "科室机构id")
    private Long orgId;

    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;


    public static final String ORG_ID = "org_id";

    public static final String SCHOOL_ID = "school_id";

    public static final String SORT_NUM = "sort_num";


    public static Map<Long, OrgSchool> getBySchoolId(List<OrgSchool> inList) {
        Map<Long, OrgSchool> outMap = new HashMap<>();
        if (inList == null || inList.size() == 0) return outMap;
        for (OrgSchool orgSchool : inList) {
            if (orgSchool == null) continue;
            outMap.put(orgSchool.getSchoolId(), orgSchool);
        }
        return outMap;
    }
}
