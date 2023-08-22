package com.caizi.edu.sms.api;

import com.antnest.mscore.token.TokenResponse;
import com.antnest.uam.dto.ModuleAndPermissionSimpleDTO;
import com.antnest.uim.dto.LoginBaseDTO;
import com.antnest.uim.dto.RandomImageDTO;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.login.*;
import com.caizi.edu.sms.hystrix.EduSmsLoginFallBack;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@Api(tags = {"登录及鉴权特殊业务处理"})
@FeignClient(value = ApplicationInfo.ANTNEST_SERVICE_NAME, fallback = EduSmsLoginFallBack.class, path = EduSmsLoginApi._PATH)
public interface EduSmsLoginApi {
    String _PATH = ApplicationInfo.ANTNEST_BASE_PATH + "/edu_insp_login";

    @ApiOperation(value = "重置回默认密码")
    @RequestMapping(value = "/doInitBizUserPwd", method = RequestMethod.POST)
    void doInitBizUserPwd(@RequestParam(value = "bizUserId") Long bizUserId);

    @ApiOperation(value = "获取登录验证码")
    @RequestMapping(value = "/getRandomImage", method = RequestMethod.POST)
    RandomImageDTO getRandomImage();

    @ApiOperation(value = "校验用户名（修改密码）")
    @RequestMapping(value = "/doCheckLoginNameForUpdatePwd", method = RequestMethod.POST)
    VOUserNameCheckResult doCheckLoginNameForUpdatePwd(@RequestParam(value = "loginName") String loginName);

    @ApiOperation(value = "发送校验短信（修改密码）")
    @RequestMapping(value = "/doSendSmsForUpdatePwd", method = RequestMethod.POST)
    void doSendSmsForUpdatePwd(@RequestParam(value = "randomNameKey") String randomNameKey);

    @ApiOperation(value = "校验短信码（修改密码）")
    @RequestMapping(value = "/doCheckSmsForUpdatePwd", method = RequestMethod.POST)
    void doCheckSmsForUpdatePwd(@RequestParam(value = "randomNameKey") String randomNameKey,
                                @RequestParam(value = "smsCode") String smsCode);

    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    void updatePwd(@RequestParam(value = "randomNameKey") String randomNameKey,
                   @RequestParam(value = "newPwd1") String newPwd1,
                   @RequestParam(value = "newPwd2") String newPwd2);

    @ApiOperation(value = "修改当前登录账号的密码")
    @RequestMapping(value = "/updateCurrAccountPwd", method = RequestMethod.POST)
    void updateCurrAccountPwd(@RequestParam(value = "oldPwd") String oldPwd,
                              @RequestParam(value = "newPwd1") String newPwd1,
                              @RequestParam(value = "newPwd2") String newPwd2);

    @ApiOperation(value = "[EDU-SMS]账号密码登录,登录成功返回token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginBaseDTO", value = "登录对象", paramType = "body", dataType = "LoginBaseDTO", dataTypeClass = LoginBaseDTO.class, required = true)
    })
    @RequestMapping(value = "/eduSmsLogin", method = RequestMethod.POST)
    TokenResponse eduSmsLogin(@Valid @RequestBody LoginBaseDTO loginBaseDTO);

    @ApiOperation(value = "[EDU-SMS]微信账号密码登录,登录成功返回token")
    @RequestMapping(value = "/eduSmsLoginForWx", method = RequestMethod.POST)
    TokenResponse eduSmsLoginForWx(@Valid @RequestBody DtoLoginForWx dtoLoginForWx);

    @ApiOperation(value = "获取登录账号信息")
    @RequestMapping(value = "/getLoginInfo", method = RequestMethod.POST)
    VOLoginInfo getLoginInfo();

    @ApiOperation(value = "登录用户可切换身份列表")
    @RequestMapping(value = "/getLoginRoleList", method = RequestMethod.POST)
    List<VOLoginRole> getLoginRoleList();

    @ApiOperation(value = "更新用户最近一次登录使用身份")
    @RequestMapping(value = "/updateLastLoginRole", method = RequestMethod.POST)
    void updateLastLoginRole(@RequestBody DtoUpdateLastLoginRole dto);

    @ApiOperation(value = "查询当前身份授权菜单列表")
    @RequestMapping(value = "/getLastLoginRoleMenuList", method = RequestMethod.POST)
    List<ModuleAndPermissionSimpleDTO> getLastLoginRoleMenuList();

    @ApiOperation(value = "获取消息通知")
    @RequestMapping(value = "/getMsgInfo", method = RequestMethod.POST)
    VOMsgInfo getMsgInfo();

    @ApiOperation(value = "刷新TOKEN,刷新成功返回token")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    TokenResponse refreshToken(@Valid @RequestBody TokenResponse oldToken);

    @ApiOperation(value = "查询移动端二维码")
    @ResponseBody
    @RequestMapping(value = "/getAppQrCode", method = RequestMethod.GET)
    void getAppQrCode(HttpServletResponse response);

    @ApiOperation(value = "验证登录用户密码")
    @RequestMapping(value = "/doCheckCurrUserPwd", method = RequestMethod.POST)
    void doCheckCurrUserPwd(@RequestParam(value = "pwd") String pwd);
}

