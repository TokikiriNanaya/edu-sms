package com.caizi.edu.sms.hystrix;

import com.antnest.mscore.base.controller.impl.AntnestBaseFallback;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.api.SmsOrgApi;
import com.caizi.edu.sms.dto.org.*;
import com.caizi.edu.sms.entity.BizUser;
import com.caizi.edu.sms.entity.SmsOrg;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 综合机构表 接口信息
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */


@Component
public class SmsOrgFallBack extends AntnestBaseFallback<SmsOrg> implements SmsOrgApi {

    @Override
    public List<SmsOrg> findOrgTree(@RequestBody DtoFindOrgTree dtoFindOrgTree) {
        return null;
    }

    @Override
    public IPage<BizUser> findOrgUserList(@RequestBody DtoFindOrgUser dtoFindOrgUser) {
        return null;
    }

    @Override
    public SmsOrg saveOrg(@RequestBody DtoSaveOrg dtoSaveOrg) {
        return null;
    }

    @Override
    public void removeOrg(@RequestParam(value = "orgId") Long orgId) {

    }

    @Override
    public void removeOrgBatch(@RequestBody List<Long> orgIdList) {

    }

    @Override
    public void saveOrgUserBatch(@RequestBody DtoSaveOrgUserBatch dtoSaveOrgUserBatch) {

    }

    @Override
    public void removeOrgUserBatch(@RequestBody DtoRemoveOrgUserBatch dtoRemoveOrgUserBatch) {

    }

    @Override
    public IPage<VODeptListAndSchoolStat> findDeptListAndSchoolStat(@RequestBody DtoFindDeptListAndSchoolStat dto) {
        return null;
    }

    @Override
    public List<SmsOrg> findSchoolTree(@RequestBody DtoFindSchoolTree dto) {
        return null;
    }

    @Override
    public List<SmsOrg> findSchoolListOfParentNode(@RequestParam(value = "orgId") Long orgId) {
        return null;
    }

    @Override
    public IPage<VOSchoolListOfDept> findSchoolListOfDept(@RequestBody DtoFindSchoolListOfDept dto) {
        return null;
    }

    @Override
    public void saveDeptAllSchool(@RequestBody DtoSaveDeptAllSchool dto) {

    }

    @Override
    public List<SmsOrg> findAllDept() {
        return null;
    }

    @Override
    public List<SmsOrg> findAllSchool() {
        return null;
    }

    @Override
    public List<VODeptOfSchool> findDeptOfSchool(@RequestBody List<Long> schoolIdList) {
        return null;
    }
}

