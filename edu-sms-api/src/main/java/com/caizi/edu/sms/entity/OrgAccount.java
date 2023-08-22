package com.caizi.edu.sms.entity;

import com.antnest.mscore.base.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机构账号表
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_org_account")
@ApiModel(value = "OrgAccount对象", description = "机构账号表")
public class OrgAccount extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    public static final int SORT_NUM_OPER_ADMIN = 1;
    public static final int SORT_NUM_AREA_ADMIN = 2;
    public static final int SORT_NUM_DEPT_USER = 3;
    public static final int SORT_NUM_SCHOOL_ADMIN = 4;
    public static final int SORT_NUM_SCHOOL_USER = 5;

    @ApiModelProperty(value = "所属机构id")
    private Long orgId;

    @ApiModelProperty(value = "账号id")
    private Long accountId;

    @ApiModelProperty(value = "在当前机构下的角色身份")
    private String roleCode;

    @ApiModelProperty(value = "身份优先级排序号")
    private Integer sortNum;


    public static final String ORG_ID = "org_id";

    public static final String ACCOUNT_ID = "account_id";

    public static final String ROLE_CODE = "role_code";

    public static final String SORT_NUM = "sort_num";


    public static Map<Long, List<OrgAccount>> getAccountListOfOrgMap(List<OrgAccount> inList) {
        Map<Long, List<OrgAccount>> taskSchoolMap = new HashMap<>();// key：检查任务ID，value：参检学校集合
        if (inList == null || inList.size() == 0) return taskSchoolMap;
        for (OrgAccount taskSchool : inList) {
            if (taskSchool == null) continue;
            Long orgId = taskSchool.getOrgId();
            List<OrgAccount> orgAccountList = taskSchoolMap.get(orgId);
            if (orgAccountList == null) {
                orgAccountList = new ArrayList<>();
            }
            orgAccountList.add(taskSchool);
            taskSchoolMap.put(orgId, orgAccountList);
        }
        return taskSchoolMap;
    }
}
