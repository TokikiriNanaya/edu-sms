package com.caizi.edu.sms.dto.org;

import com.caizi.edu.sms.entity.BizUser;
import com.caizi.edu.sms.entity.SmsOrg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-查询科室已分配学校列表")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOSchoolListOfDept implements Serializable {

    private static final long serialVersionUID = -8237783226682572759L;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    @ApiModelProperty(value = "学段")
    private String period;

    @ApiModelProperty(value = "校管理员ID")
    private Long schoolAdminId;

    @ApiModelProperty(value = "校管理员姓名")
    private String schoolAdminName;

    @ApiModelProperty(value = "校管理员电话")
    private String schoolAdminPhone;

    @ApiModelProperty(value = "包片科室ID")
    private Long deptId;

    @ApiModelProperty(value = "包片科室名称")
    private String deptName;


    public static VOSchoolListOfDept getSchoolVO(SmsOrg school, BizUser bizUser, SmsOrg dept) {
        if (school == null) return null;
        VOSchoolListOfDept vo = new VOSchoolListOfDept();
        vo.setSchoolId(school.getId());
        vo.setSchoolName(school.getName());
        vo.setPeriod(school.getPeriod());
        if (bizUser != null) {
            vo.setSchoolAdminId(bizUser.getId());
            vo.setSchoolAdminName(bizUser.getName());
            vo.setSchoolAdminPhone(bizUser.getPhone());
        }
        if (dept != null) {
            vo.setDeptId(dept.getId());
            vo.setDeptName(dept.getName());
        }
        return vo;
    }
}
