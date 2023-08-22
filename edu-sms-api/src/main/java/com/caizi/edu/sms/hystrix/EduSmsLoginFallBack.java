package com.caizi.edu.sms.hystrix;

import com.antnest.mscore.token.TokenResponse;
import com.antnest.uam.dto.ModuleAndPermissionSimpleDTO;
import com.antnest.uim.dto.LoginBaseDTO;
import com.antnest.uim.dto.RandomImageDTO;
import com.caizi.edu.sms.api.EduSmsLoginApi;
import com.caizi.edu.sms.dto.login.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(EduSmsLoginApi._PATH)
public class EduSmsLoginFallBack implements EduSmsLoginApi {

    @Override
    public void doInitBizUserPwd(@RequestParam(value = "bizUserId") Long bizUserId) {

    }

    @Override
    public RandomImageDTO getRandomImage() {
        return null;
    }

    @Override
    public VOUserNameCheckResult doCheckLoginNameForUpdatePwd(@RequestParam(value = "userName") String loginName) {
        return null;
    }

    @Override
    public void doSendSmsForUpdatePwd(@RequestParam(value = "randomNameKey") String randomNameKey) {

    }

    @Override
    public void doCheckSmsForUpdatePwd(@RequestParam(value = "randomNameKey") String randomNameKey,
                                       @RequestParam(value = "smsCode") String smsCode) {

    }

    @Override
    public void updatePwd(@RequestParam(value = "randomNameKey") String randomNameKey,
                          @RequestParam(value = "newPwd1") String newPwd1,
                          @RequestParam(value = "newPwd2") String newPwd2) {

    }

    @Override
    public void updateCurrAccountPwd(@RequestParam(value = "oldPwd") String oldPwd,
                                     @RequestParam(value = "newPwd1") String newPwd1,
                                     @RequestParam(value = "newPwd2") String newPwd2) {

    }

    @Override
    public TokenResponse eduSmsLogin(@Valid @RequestBody LoginBaseDTO loginBaseDTO) {
        return null;
    }

    @Override
    public TokenResponse eduSmsLoginForWx(@Valid @RequestBody DtoLoginForWx dtoLoginForWx) {
        return null;
    }

    @Override
    public VOLoginInfo getLoginInfo() {
        return null;
    }

    @Override
    public List<VOLoginRole> getLoginRoleList() {
        return null;
    }

    @Override
    public void updateLastLoginRole(@RequestBody DtoUpdateLastLoginRole dto) {

    }

    @Override
    public List<ModuleAndPermissionSimpleDTO> getLastLoginRoleMenuList() {
        return null;
    }

    @Override
    public VOMsgInfo getMsgInfo() {
        return null;
    }

    @Override
    public TokenResponse refreshToken(@Valid @RequestBody TokenResponse oldToken) {
        return null;
    }

    @Override
    public void getAppQrCode(HttpServletResponse response) {

    }

    @Override
    public void doCheckCurrUserPwd(@RequestParam(value = "pwd") String pwd) {

    }
}

