package com.caizi.edu.sms.hystrix;

import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.controller.impl.AntnestBaseFallback;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.api.BizUserApi;
import com.caizi.edu.sms.dto.user.DtoChangeUserStatusBatch;
import com.caizi.edu.sms.dto.user.DtoFindUser;
import com.caizi.edu.sms.dto.user.DtoSaveUser;
import com.caizi.edu.sms.dto.user.VOUserOrgMain;
import com.caizi.edu.sms.entity.BizUser;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
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


@Component
public class BizUserFallBack extends AntnestBaseFallback<BizUser> implements BizUserApi {

    @Override
    public FileObject getUserCover() {
        return null;
    }

    @Override
    public void doUploadUserCover(@RequestParam(value = "fileObjectId") Long fileObjectId) {

    }

    @Override
    public IPage<BizUser> findUserListForArea(@RequestBody DtoFindUser dtoFindUser) {
        return null;
    }

    @Override
    public IPage<BizUser> findUserListForSchool(@RequestBody DtoFindUser dtoFindUser) {
        return null;
    }

    @Override
    public List<VOUserOrgMain> findUserOrgInfoBatch(@RequestBody List<Long> bizUserIdList) {
        return null;
    }

    @Override
    public BizUser getUserByPhoneOrIdNumber(@RequestParam(value = "phone", required = false) String phone,
                                            @RequestParam(value = "idNumber", required = false) String idNumber) {
        return null;
    }

    @Override
    public BizUser saveUserAndAccount(@RequestBody DtoSaveUser dtoSaveUser) {
        return null;
    }

    @Override
    public BizUser saveSchoolUserAndAccount(@RequestBody DtoSaveUser dtoSaveUser) {
        return null;
    }

    @Override
    public void doChangeUserStatusBatch(@RequestBody DtoChangeUserStatusBatch dtoChangeUserStatusBatch) {

    }

    @Override
    public void removeUser(@RequestParam(value = "bizUserId") Long bizUserId) {

    }
}

