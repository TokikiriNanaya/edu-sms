package com.caizi.edu.sms.biz.controller;

import com.antnest.mscore.base.controller.impl.AntnestSimpleBaseController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.api.SmsOrgApi;
import com.caizi.edu.sms.biz.service.SmsOrgService;
import com.caizi.edu.sms.dto.org.*;
import com.caizi.edu.sms.entity.BizUser;
import com.caizi.edu.sms.entity.SmsOrg;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 综合机构表 前端控制器
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@RestController
@RequestMapping(SmsOrgApi._PATH)
public class SmsOrgController extends AntnestSimpleBaseController<SmsOrgService, SmsOrg> implements SmsOrgApi {

    @Override
    public List<SmsOrg> findOrgTree(@RequestBody DtoFindOrgTree dtoFindOrgTree) {
        return baseService.findOrgTree(dtoFindOrgTree);
    }

    @Override
    public IPage<BizUser> findOrgUserList(@RequestBody DtoFindOrgUser dtoFindOrgUser) {
        return baseService.findOrgUserList(dtoFindOrgUser);
    }

    @Override
    public SmsOrg saveOrg(@RequestBody DtoSaveOrg dtoSaveOrg) {
        return baseService.saveOrg(dtoSaveOrg);
    }

    @Override
    public void removeOrg(@RequestParam(value = "orgId") Long orgId) {
        baseService.removeOrg(orgId);
    }

    @Override
    public void removeOrgBatch(@RequestBody List<Long> orgIdList) {
        baseService.removeOrgBatch(orgIdList);
    }

    @Override
    public void saveOrgUserBatch(@RequestBody DtoSaveOrgUserBatch dtoSaveOrgUserBatch) {
        baseService.saveOrgUserBatch(dtoSaveOrgUserBatch);
    }

    @Override
    public void removeOrgUserBatch(@RequestBody DtoRemoveOrgUserBatch dtoRemoveOrgUserBatch) {
        baseService.removeOrgUserBatch(dtoRemoveOrgUserBatch);
    }

    @Override
    public IPage<VODeptListAndSchoolStat> findDeptListAndSchoolStat(@RequestBody DtoFindDeptListAndSchoolStat dto) {
        return baseService.findDeptListAndSchoolStat(dto);
    }

    @Override
    public List<SmsOrg> findSchoolTree(@RequestBody DtoFindSchoolTree dto) {
        return baseService.findSchoolTree(dto);
    }

    @Override
    public List<SmsOrg> findSchoolListOfParentNode(@RequestParam(value = "orgId") Long orgId) {
        return baseService.findSchoolListOfParentNode(orgId);
    }

    @Override
    public IPage<VOSchoolListOfDept> findSchoolListOfDept(@RequestBody DtoFindSchoolListOfDept dto) {
        return baseService.findSchoolListOfDept(dto);
    }

    @Override
    public void saveDeptAllSchool(@RequestBody DtoSaveDeptAllSchool dto) {
        baseService.saveDeptAllSchool(dto);
    }

    @Override
    public List<SmsOrg> findAllDept() {
        return baseService.findAllDept();
    }

    @Override
    public List<SmsOrg> findAllSchool() {
        return baseService.findAllSchool();
    }

    @Override
    public List<VODeptOfSchool> findDeptOfSchool(@RequestBody List<Long> schoolIdList) {
        return baseService.findDeptOfSchool(schoolIdList);
    }
}

