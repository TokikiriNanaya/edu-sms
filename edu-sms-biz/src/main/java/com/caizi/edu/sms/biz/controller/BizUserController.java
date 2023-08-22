package com.caizi.edu.sms.biz.controller;

import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.controller.impl.AntnestSimpleBaseController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.api.BizUserApi;
import com.caizi.edu.sms.biz.service.BizUserService;
import com.caizi.edu.sms.dto.user.DtoChangeUserStatusBatch;
import com.caizi.edu.sms.dto.user.DtoFindUser;
import com.caizi.edu.sms.dto.user.DtoSaveUser;
import com.caizi.edu.sms.dto.user.VOUserOrgMain;
import com.caizi.edu.sms.entity.BizUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 业务用户表 前端控制器
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@RestController
@RequestMapping(BizUserApi._PATH)
public class BizUserController extends AntnestSimpleBaseController<BizUserService, BizUser> implements BizUserApi {

    @Override
    public FileObject getUserCover() {
        return baseService.getUserCover();
    }

    @Override
    public void doUploadUserCover(@RequestParam(value = "fileObjectId") Long fileObjectId) {
        baseService.doUploadUserCover(fileObjectId);
    }

    @Override
    public IPage<BizUser> findUserListForArea(@RequestBody DtoFindUser dtoFindUser) {
        return baseService.findUserListForArea(dtoFindUser);
    }

    @Override
    public IPage<BizUser> findUserListForSchool(@RequestBody DtoFindUser dtoFindUser) {
        return baseService.findUserListForSchool(dtoFindUser);
    }

    @Override
    public List<VOUserOrgMain> findUserOrgInfoBatch(@RequestBody List<Long> bizUserIdList) {
        return baseService.findUserOrgInfoBatch(bizUserIdList);
    }

    @Override
    public BizUser getUserByPhoneOrIdNumber(@RequestParam(value = "phone", required = false) String phone,
                                            @RequestParam(value = "idNumber", required = false) String idNumber) {
        return baseService.getUserByPhoneOrIdNumber(phone, idNumber);
    }

    @Override
    public BizUser saveUserAndAccount(@RequestBody DtoSaveUser dtoSaveUser) {
        return baseService.saveUserAndAccount(dtoSaveUser);
    }

    @Override
    public BizUser saveSchoolUserAndAccount(@RequestBody DtoSaveUser dtoSaveUser) {
        return baseService.saveSchoolUserAndAccount(dtoSaveUser);
    }

    @Override
    public void doChangeUserStatusBatch(@RequestBody DtoChangeUserStatusBatch dtoChangeUserStatusBatch) {
        baseService.doChangeUserStatusBatch(dtoChangeUserStatusBatch);
    }

    @Override
    public void removeUser(@RequestParam(value = "bizUserId") Long bizUserId) {
        baseService.removeUser(bizUserId);
    }
}

