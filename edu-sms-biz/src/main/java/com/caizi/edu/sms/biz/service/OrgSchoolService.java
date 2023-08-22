package com.caizi.edu.sms.biz.service;

import com.antnest.mscore.base.service.AntnestBaseService;
import com.caizi.edu.sms.entity.OrgSchool;

import java.util.List;

/**
 * <p>
 * 科室学校分配 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
public interface OrgSchoolService extends AntnestBaseService<OrgSchool> {

    List<OrgSchool> findByDeptId(Long deptId);

    List<OrgSchool> findBySchoolIdList(List<Long> schoolIdList);

    void removeByDeptId(Long deptId);

    void removeBySchoolIdList(List<Long> schoolIdList);

    OrgSchool findBySchoolId(Long schoolId);
}
