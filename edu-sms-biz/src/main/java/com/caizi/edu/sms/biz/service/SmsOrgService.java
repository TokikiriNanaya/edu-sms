package com.caizi.edu.sms.biz.service;

import com.antnest.mscore.base.service.AntnestBaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.dto.org.*;
import com.caizi.edu.sms.entity.BizUser;
import com.caizi.edu.sms.entity.SmsOrg;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 综合机构表 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
public interface SmsOrgService extends AntnestBaseService<SmsOrg> {

    List<SmsOrg> findOrgTree(DtoFindOrgTree dtoFindOrgTree);

    IPage<BizUser> findOrgUserList(DtoFindOrgUser dtoFindOrgUser);

    SmsOrg saveOrg(DtoSaveOrg dtoSaveOrg);

    void removeOrg(Long orgId);

    void removeOrgBatch(List<Long> orgIdList);

    void saveOrgUserBatch(DtoSaveOrgUserBatch dtoSaveOrgUserBatch);

    void removeOrgUserBatch(DtoRemoveOrgUserBatch dtoRemoveOrgUserBatch);

    IPage<VODeptListAndSchoolStat> findDeptListAndSchoolStat(DtoFindDeptListAndSchoolStat dto);

    List<SmsOrg> findSchoolTree(DtoFindSchoolTree dto);

    List<SmsOrg> findSchoolListOfParentNode(Long orgId);

    IPage<VOSchoolListOfDept> findSchoolListOfDept(DtoFindSchoolListOfDept dto);

    void saveDeptAllSchool(DtoSaveDeptAllSchool dto);

    List<SmsOrg> findAllDept();

    List<SmsOrg> findAllSchool();

    List<VODeptOfSchool> findDeptOfSchool(List<Long> schoolIdList);


    /*
     * ========================================以下都是内部接口========================================
     */
    List<SmsOrg> findByAccountId(Long accountId);

    List<SmsOrg> findDirectChildOrgList(Long pid);

    List<SmsOrg> findDirectChildOrgList(List<Long> pidList);

    Map<Long, List<SmsOrg>> findByAccountIdList(List<Long> accountIdList);

    SmsOrg getByOrgName(String name);

    SmsOrg getByPidAndOrgName(Long pid, String name);

    SmsOrg saveOrgForBigDataPlat(SmsOrg smsOrg, int syncFlag);

    List<SmsOrg> findAllCustomOrg();

    IPage<SmsOrg> findByCondition(DtoFindByCondition dto);
}
