package com.caizi.edu.sms.api;

import com.antnest.mscore.base.controller.AntnestSimpleBaseApi;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.org.*;
import com.caizi.edu.sms.entity.BizUser;
import com.caizi.edu.sms.entity.SmsOrg;
import com.caizi.edu.sms.hystrix.SmsOrgFallBack;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

@Api(tags = {"综合机构表"})
@FeignClient(value = ApplicationInfo.ANTNEST_SERVICE_NAME, fallback = SmsOrgFallBack.class, path = SmsOrgApi._PATH)
public interface SmsOrgApi extends AntnestSimpleBaseApi<SmsOrg> {
    String _PATH = ApplicationInfo.ANTNEST_BASE_PATH + "/sms_org";

    @ApiOperation(value = "查询机构树")
    @RequestMapping(value = "/findOrgTree", method = RequestMethod.POST)
    List<SmsOrg> findOrgTree(@RequestBody DtoFindOrgTree dtoFindOrgTree);

    @ApiOperation(value = "查询机构用户列表")
    @RequestMapping(value = "/findOrgUserList", method = RequestMethod.POST)
    IPage<BizUser> findOrgUserList(@RequestBody DtoFindOrgUser dtoFindOrgUser);

    @ApiOperation(value = "保存机构信息")
    @RequestMapping(value = "/saveOrg", method = RequestMethod.POST)
    SmsOrg saveOrg(@RequestBody DtoSaveOrg dtoSaveOrg);

    @ApiOperation(value = "删除机构")
    @RequestMapping(value = "/removeOrg", method = RequestMethod.POST)
    void removeOrg(@RequestParam(value = "orgId") Long orgId);

    @ApiOperation(value = "删除机构（批量）")
    @RequestMapping(value = "/removeOrgBatch", method = RequestMethod.POST)
    void removeOrgBatch(@RequestBody List<Long> orgIdList);

    @ApiOperation(value = "添加组织用户（批量）")
    @RequestMapping(value = "/saveOrgUserBatch", method = RequestMethod.POST)
    void saveOrgUserBatch(@RequestBody DtoSaveOrgUserBatch dtoSaveOrgUserBatch);

    @ApiOperation(value = "移除组织用户（批量）")
    @RequestMapping(value = "/removeOrgUserBatch", method = RequestMethod.POST)
    void removeOrgUserBatch(@RequestBody DtoRemoveOrgUserBatch dtoRemoveOrgUserBatch);


    /*===================================科室学校分配===================================*/
    @ApiOperation(value = "查询科室及分配学校")
    @RequestMapping(value = "/findDeptListAndSchoolStat", method = RequestMethod.POST)
    IPage<VODeptListAndSchoolStat> findDeptListAndSchoolStat(@RequestBody DtoFindDeptListAndSchoolStat dto);

    @ApiOperation(value = "查询可选学校树")
    @RequestMapping(value = "/findSchoolTree", method = RequestMethod.POST)
    List<SmsOrg> findSchoolTree(@RequestBody DtoFindSchoolTree dto);

    @ApiOperation(value = "查询指定节点下的所有学校列表")
    @RequestMapping(value = "/findSchoolListOfParentNode", method = RequestMethod.POST)
    List<SmsOrg> findSchoolListOfParentNode(@RequestParam(value = "orgId") Long orgId);

    @ApiOperation(value = "查询科室已分配学校列表")
    @RequestMapping(value = "/findSchoolListOfDept", method = RequestMethod.POST)
    IPage<VOSchoolListOfDept> findSchoolListOfDept(@RequestBody DtoFindSchoolListOfDept dto);

    @ApiOperation(value = "保存分配学校列表")
    @RequestMapping(value = "/saveDeptAllSchool", method = RequestMethod.POST)
    void saveDeptAllSchool(@RequestBody DtoSaveDeptAllSchool dto);

    /*===================================主业务所需接口===================================*/
    @ApiOperation(value = "查询所有科室列表")
    @RequestMapping(value = "/findAllDept", method = RequestMethod.POST)
    List<SmsOrg> findAllDept();

    @ApiOperation(value = "查询所有学校列表")
    @RequestMapping(value = "/findAllSchool", method = RequestMethod.POST)
    List<SmsOrg> findAllSchool();

    @ApiOperation(value = "查询学校归属科室")
    @RequestMapping(value = "/findDeptOfSchool", method = RequestMethod.POST)
    List<VODeptOfSchool> findDeptOfSchool(@RequestBody List<Long> schoolIdList);
}

