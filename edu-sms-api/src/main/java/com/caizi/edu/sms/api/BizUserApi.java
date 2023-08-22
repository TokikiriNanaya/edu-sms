package com.caizi.edu.sms.api;

import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.controller.AntnestSimpleBaseApi;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.user.DtoChangeUserStatusBatch;
import com.caizi.edu.sms.dto.user.DtoFindUser;
import com.caizi.edu.sms.dto.user.DtoSaveUser;
import com.caizi.edu.sms.dto.user.VOUserOrgMain;
import com.caizi.edu.sms.entity.BizUser;
import com.caizi.edu.sms.hystrix.BizUserFallBack;
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
 * 业务用户表 接口信息
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */

@Api(tags = {"业务用户表"})
@FeignClient(value = ApplicationInfo.ANTNEST_SERVICE_NAME, fallback = BizUserFallBack.class, path = BizUserApi._PATH)
public interface BizUserApi extends AntnestSimpleBaseApi<BizUser> {
    String _PATH = ApplicationInfo.ANTNEST_BASE_PATH + "/sms_biz_user";

    @ApiOperation(value = "查询个人封面")
    @RequestMapping(value = "/getUserCover", method = RequestMethod.POST)
    FileObject getUserCover();

    @ApiOperation(value = "上传个人封面")
    @RequestMapping(value = "/doUploadUserCover", method = RequestMethod.POST)
    void doUploadUserCover(@RequestParam(value = "fileObjectId") Long fileObjectId);

    @ApiOperation(value = "查询用户列表（区管理员）")
    @RequestMapping(value = "/findUserListForArea", method = RequestMethod.POST)
    IPage<BizUser> findUserListForArea(@RequestBody DtoFindUser dtoFindUser);

    @ApiOperation(value = "查询用户列表（校管理员）")
    @RequestMapping(value = "/findUserListForSchool", method = RequestMethod.POST)
    IPage<BizUser> findUserListForSchool(@RequestBody DtoFindUser dtoFindUser);

    @ApiOperation(value = "查询用户所属机构（批量）")
    @RequestMapping(value = "/findUserOrgInfoBatch", method = RequestMethod.POST)
    List<VOUserOrgMain> findUserOrgInfoBatch(@RequestBody List<Long> bizUserIdList);

    @ApiOperation(value = "查询用户（根据电话号码或身份证号精确匹配）")
    @RequestMapping(value = "/getUserByPhoneOrIdNumber", method = RequestMethod.POST)
    BizUser getUserByPhoneOrIdNumber(@RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "idNumber", required = false) String idNumber);

    @ApiOperation(value = "保存用户及账号信息")
    @RequestMapping(value = "/saveUserAndAccount", method = RequestMethod.POST)
    BizUser saveUserAndAccount(@RequestBody DtoSaveUser dtoSaveUser);

    @ApiOperation(value = "保存学校用户及账号信息")
    @RequestMapping(value = "/saveSchoolUserAndAccount", method = RequestMethod.POST)
    BizUser saveSchoolUserAndAccount(@RequestBody DtoSaveUser dtoSaveUser);

    @ApiOperation(value = "批量启用/禁用用户")
    @RequestMapping(value = "/doChangeUserStatusBatch", method = RequestMethod.POST)
    void doChangeUserStatusBatch(@RequestBody DtoChangeUserStatusBatch dtoChangeUserStatusBatch);

    @ApiOperation(value = "删除用户")
    @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
    void removeUser(@RequestParam(value = "bizUserId") Long bizUserId);
}

