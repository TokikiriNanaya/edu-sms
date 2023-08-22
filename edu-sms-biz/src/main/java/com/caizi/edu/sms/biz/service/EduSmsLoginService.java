package com.caizi.edu.sms.biz.service;

import com.antnest.mscore.token.TokenResponse;
import com.antnest.uam.dto.ModuleAndPermissionSimpleDTO;
import com.antnest.uim.dto.LoginBaseDTO;
import com.antnest.uim.dto.RandomImageDTO;
import com.caizi.edu.sms.dto.login.*;

import java.util.List;

/**
 * <p>
 * 业务登录 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
public interface EduSmsLoginService {

    void doInitBizUserPwd(Long bizUserId);

    RandomImageDTO getRandomImage();

    VOUserNameCheckResult doCheckLoginNameForUpdatePwd(String loginName);

    void doSendSmsForUpdatePwd(String randomNameKey);

    void doCheckSmsForUpdatePwd(String randomNameKey, String smsCode);

    void updatePwd(String randomNameKey, String newPwd1, String newPwd2);

    void updateCurrAccountPwd(String oldPwd, String newPwd1, String newPwd2);

    TokenResponse eduSmsLogin(LoginBaseDTO loginBaseDTO);

    TokenResponse eduSmsLoginForWx(DtoLoginForWx dtoLoginForWx);

    VOLoginInfo getLoginInfo();

    List<VOLoginRole> getLoginRoleList();

    void updateLastLoginRole(DtoUpdateLastLoginRole dto);

    List<ModuleAndPermissionSimpleDTO> getLastLoginRoleMenuList();

    VOMsgInfo getMsgInfo();

    TokenResponse refreshToken(TokenResponse oldToken);

    void doCheckCurrUserPwd(String pwd);

    /*
     * ========================================以下都是内部接口========================================
     */
    TokenResponse ssoLogin(Long accountId);
}
