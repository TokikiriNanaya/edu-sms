package com.caizi.edu.sms.biz.controller;

import com.antnest.mscore.exception.BizException;
import com.antnest.mscore.token.TokenResponse;
import com.antnest.uam.dto.ModuleAndPermissionSimpleDTO;
import com.antnest.uim.dto.LoginBaseDTO;
import com.antnest.uim.dto.RandomImageDTO;
import com.caizi.edu.sms.api.EduSmsLoginApi;
import com.caizi.edu.sms.biz.conf.YmlEduSmsConfig;
import com.caizi.edu.sms.biz.service.EduSmsLoginService;
import com.caizi.edu.sms.biz.utils.FileBytesUtil;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.login.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(EduSmsLoginApi._PATH)
public class EduSmsLoginController implements EduSmsLoginApi {

    @Resource
    private YmlEduSmsConfig ymlEduSmsConfig;

    @Resource
    private EduSmsLoginService eduSmsLoginService;


    @Override
    public void doInitBizUserPwd(@RequestParam(value = "bizUserId") Long bizUserId) {
        eduSmsLoginService.doInitBizUserPwd(bizUserId);
    }

    @Override
    public RandomImageDTO getRandomImage() {
        return eduSmsLoginService.getRandomImage();
    }

    @Override
    public VOUserNameCheckResult doCheckLoginNameForUpdatePwd(@RequestParam(value = "userName") String loginName) {
        return eduSmsLoginService.doCheckLoginNameForUpdatePwd(loginName);
    }

    @Override
    public void doSendSmsForUpdatePwd(@RequestParam(value = "randomNameKey") String randomNameKey) {
        eduSmsLoginService.doSendSmsForUpdatePwd(randomNameKey);
    }

    @Override
    public void doCheckSmsForUpdatePwd(@RequestParam(value = "randomNameKey") String randomNameKey,
                                       @RequestParam(value = "smsCode") String smsCode) {
        eduSmsLoginService.doCheckSmsForUpdatePwd(randomNameKey, smsCode);
    }

    @Override
    public void updatePwd(@RequestParam(value = "randomNameKey") String randomNameKey,
                          @RequestParam(value = "newPwd1") String newPwd1,
                          @RequestParam(value = "newPwd2") String newPwd2) {
        eduSmsLoginService.updatePwd(randomNameKey, newPwd1, newPwd2);
    }

    @Override
    public void updateCurrAccountPwd(@RequestParam(value = "oldPwd") String oldPwd,
                                     @RequestParam(value = "newPwd1") String newPwd1,
                                     @RequestParam(value = "newPwd2") String newPwd2) {
        eduSmsLoginService.updateCurrAccountPwd(oldPwd, newPwd1, newPwd2);
    }

    @Override
    public TokenResponse eduSmsLogin(@Valid @RequestBody LoginBaseDTO loginBaseDTO) {
        return eduSmsLoginService.eduSmsLogin(loginBaseDTO);
    }

    @Override
    public TokenResponse eduSmsLoginForWx(@Valid @RequestBody DtoLoginForWx dtoLoginForWx) {
        return eduSmsLoginService.eduSmsLoginForWx(dtoLoginForWx);
    }

    @Override
    public VOLoginInfo getLoginInfo() {
        return eduSmsLoginService.getLoginInfo();
    }

    @Override
    public List<VOLoginRole> getLoginRoleList() {
        return eduSmsLoginService.getLoginRoleList();
    }

    @Override
    public void updateLastLoginRole(@RequestBody DtoUpdateLastLoginRole dto) {
        eduSmsLoginService.updateLastLoginRole(dto);
    }

    @Override
    public List<ModuleAndPermissionSimpleDTO> getLastLoginRoleMenuList() {
        return eduSmsLoginService.getLastLoginRoleMenuList();
    }

    @Override
    public VOMsgInfo getMsgInfo() {
        return eduSmsLoginService.getMsgInfo();
    }

    @Override
    public TokenResponse refreshToken(@Valid @RequestBody TokenResponse oldToken) {
        return eduSmsLoginService.refreshToken(oldToken);
    }

    @Override
    public void getAppQrCode(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(ApplicationInfo.APP_QR_CODE_NAME, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String fullPath = ymlEduSmsConfig.getTempFileRoot() + File.separator + ApplicationInfo.APP_QR_CODE_NAME;
        log.info("========>开始下载二维码文件：{}，", fullPath);
        byte[] fileData = FileBytesUtil.getBytesByFile(fullPath);
        if (fileData == null || fileData.length <= 0) throw new BizException("加载模板文件失败，请联系运维管理员！");
        try {
            response.getOutputStream().write(fileData);
            log.info("========>下载完毕！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doCheckCurrUserPwd(@RequestParam(value = "pwd") String pwd) {
        eduSmsLoginService.doCheckCurrUserPwd(pwd);
    }
}

