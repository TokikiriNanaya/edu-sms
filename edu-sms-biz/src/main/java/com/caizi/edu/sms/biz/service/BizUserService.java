package com.caizi.edu.sms.biz.service;

import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.service.AntnestBaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.dto.user.*;
import com.caizi.edu.sms.entity.BizUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务用户表 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
public interface BizUserService extends AntnestBaseService<BizUser> {

    FileObject getUserCover();

    void doUploadUserCover(Long fileObjectId);

    IPage<BizUser> findUserListForArea(DtoFindUser dtoFindUser);

    IPage<BizUser> findUserListForSchool(DtoFindUser dtoFindUser);

    List<VOUserOrgMain> findUserOrgInfoBatch(List<Long> bizUserIdList);

    BizUser getUserByPhoneOrIdNumber(String phone, String idNumber);

    BizUser saveUserAndAccount(DtoSaveUser dtoSaveUser);

    BizUser saveSchoolUserAndAccount(DtoSaveUser dtoSaveUser);

    void doChangeUserStatusBatch(DtoChangeUserStatusBatch dtoChangeUserStatusBatch);

    void removeUser(Long bizUserId);


    /*
     * ========================================以下都是内部接口========================================
     */
    BizUser getByLoginName(String loginName);

    void updateAccountPwd(String phoneNum, String pwd);

    Long saveAccountByBizUser(BizUser bizUser, String pwd);

    void updateAccountByBizUser(BizUser bizUser);

    IPage<BizUser> findByCondition(DtoFindPageCondition condition);

    Map<Long, BizUser> findByAccountIdList(List<Long> accountIdList);
}
