package com.caizi.edu.sms.biz.service.impl;

import com.antnest.amc.api.ApplicationApi;
import com.antnest.amc.api.RoleApi;
import com.antnest.amc.entity.Application;
import com.antnest.amc.entity.Role;
import com.antnest.mscore.config.prop.AntnestAppProperties;
import com.antnest.mscore.config.prop.AntnestWxProperties;
import com.antnest.mscore.context.RequestContextHolder;
import com.antnest.mscore.exception.BizCode;
import com.antnest.mscore.exception.BizException;
import com.antnest.mscore.token.Token;
import com.antnest.mscore.token.TokenParser;
import com.antnest.mscore.token.TokenResponse;
import com.antnest.mscore.util.AssertUtil;
import com.antnest.mscore.util.CipherUtil;
import com.antnest.uam.api.AuthV2Api;
import com.antnest.uam.api.OrganizationAppApi;
import com.antnest.uam.dto.ModuleAndPermissionSimpleDTO;
import com.antnest.uam.dto.OrganizationAppShowDTO;
import com.antnest.uim.api.AccountApi;
import com.antnest.uim.api.AuthApi;
import com.antnest.uim.api.OrganizationMemberApi;
import com.antnest.uim.dto.LoginBaseDTO;
import com.antnest.uim.dto.LoginDTO;
import com.antnest.uim.dto.RandomImageDTO;
import com.antnest.uim.dto.WxRegisterV2DTO;
import com.antnest.uim.dto.account.request.AccountOldPWDRequestDTO;
import com.antnest.uim.dto.account.request.AccountResetTargetPWDRequestDTO;
import com.antnest.uim.entity.Account;
import com.antnest.uim.entity.OrganizationMember;
import com.antnest.uim.enums.AccountBizType;
import com.antnest.uim.enums.AccountState;
import com.caizi.edu.sms.biz.service.*;
import com.caizi.edu.sms.biz.wx.CommHttpClient;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.login.*;
import com.caizi.edu.sms.entity.BizUser;
import com.caizi.edu.sms.entity.OrgAccount;
import com.caizi.edu.sms.entity.SmsOrg;
import com.caizi.edu.sms.entity.SysConf;
import com.caizi.edu.sms.enums.BizUserStatusEnum;
import com.caizi.edu.sms.enums.FixedRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.antnest.mscore.constant.HeaderConstant.ANTNEST_HEADER_TOKEN;

/**
 * <p>
 * 业务登录 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Slf4j
@Service
public class EduSmsLoginServiceImpl implements EduSmsLoginService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private AuthApi uimAuthApi;

    @Resource
    private AuthV2Api authV2Api;

    @Resource
    private AntnestAppProperties appProperties;

    @Resource
    private ApplicationApi applicationApi;

    @Resource
    private OrganizationAppApi organizationAppApi;

    @Resource
    private AccountApi accountApi;

    @Resource
    private BizUserService bizUserService;

    @Resource
    private OrgAccountService orgAccountService;

    @Resource
    private SysConfService sysConfService;

    @Resource
    private RoleApi roleApi;

    @Resource
    private SmsOrgService smsOrgService;

    @Resource
    private OrganizationMemberApi organizationMemberApi;

    @Resource
    private CommHttpClient commHttpClient;

    @Resource
    private AntnestWxProperties antnestWxProperties;


    @Override
    public void doInitBizUserPwd(Long bizUserId) {
        if (bizUserId == null) throw new BizException("未传入待修改密码的业务用户ID，修改失败，请联系运维管理员！");
        BizUser bizUser = bizUserService.getById(bizUserId);
        if (bizUser == null) throw new BizException("根据ID未查询到业务用户信息，修改失败，请联系运维管理员");
        List<Account> oldAccountList = accountApi.getByLoginName(bizUser.getPhone());// 已通过swagger验证过，该接口是精准匹配
        if (oldAccountList == null || oldAccountList.size() == 0) throw new BizException("未查询到账号信息，请联系运维管理员");
        AccountResetTargetPWDRequestDTO dto = new AccountResetTargetPWDRequestDTO();
        dto.setAccountId(oldAccountList.get(0).getId());
        dto.setPassword(ApplicationInfo.INIT_PASSWORD);
        accountApi.reset2TargetPassword(dto);
    }

    @Override
    public RandomImageDTO getRandomImage() {
        return uimAuthApi.getRandomImage();
    }

    @Override
    public VOUserNameCheckResult doCheckLoginNameForUpdatePwd(String loginName) {
        if (StringUtils.isEmpty(loginName)) throw new BizException("待验证的用户名为空！");
        RequestContextHolder.bind(appProperties.getOrganizationAccountId());
        BizUser bizUser = bizUserService.getByLoginName(loginName);
        if (bizUser == null) throw new BizException(10, "无效的用户名！");
        String userPhone = bizUser.getPhone();
        if (userPhone == null) throw new BizException("未获取到用户手机号，请联系运维管理员！");
        if (userPhone.length() < 7) throw new BizException("用户手机号位数不正确，请联系运维管理员！");
        //写入redis
        String key = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(VOUserNameCheckResult.REDIS_KEY + key, userPhone, 60 * 10, TimeUnit.SECONDS);
        VOUserNameCheckResult result = new VOUserNameCheckResult();
        result.setRandomNameKey(key);
        result.setPhoneStr(userPhone.substring(0, 3) + "****" + userPhone.substring(userPhone.length() - 4));
        return result;
    }

    @Override
    public void doSendSmsForUpdatePwd(String randomNameKey) {
        String userPhone = checkRandomNameKey(randomNameKey);

        // TODO 发送短信验证码

    }

    @Override
    public void doCheckSmsForUpdatePwd(String randomNameKey, String smsCode) {
        checkRandomNameKey(randomNameKey);
        if (StringUtils.isEmpty(smsCode)) throw new BizException("未填写短信验证码！");
        // TODO 校验短信验证码

        // 校验成功，写入redis
        redisTemplate.opsForValue().set(VOUserNameCheckResult.REDIS_KEY_SMS_HAS_CHECK + randomNameKey,
                VOUserNameCheckResult.SMS_CHECK_PASS, 60 * 10, TimeUnit.SECONDS);
    }

    @Override
    public void updatePwd(String randomNameKey, String newPwd1, String newPwd2) {
        if (StringUtils.isEmpty(newPwd1)) throw new BizException("未填写新密码！");
        if (StringUtils.isEmpty(newPwd2)) throw new BizException("请填写确认密码！");
        if (!newPwd1.equals(newPwd2)) throw new BizException("两次填写的密码不一致！");
        /*
         校验短信是否验证通过
         */
        Object smsCheckObj = redisTemplate.opsForValue().get(VOUserNameCheckResult.REDIS_KEY_SMS_HAS_CHECK + randomNameKey);
        if (smsCheckObj == null) throw new BizException(10, "尚未校验短信码！");
        String smsCheck = smsCheckObj.toString();
        if (StringUtils.isEmpty(smsCheck)) throw new BizException(10, "尚未校验短信码！");
        if (!VOUserNameCheckResult.SMS_CHECK_PASS.equals(smsCheck)) throw new BizException(20, "短信码校验结果为不通过，不能修改密码！");
        /*
         根据手机号码查询业务用户对象
         */
        String userPhone = checkRandomNameKey(randomNameKey);
        RequestContextHolder.bind(appProperties.getOrganizationAccountId());
        BizUser bizUser = bizUserService.getByLoginName(userPhone);
        if (bizUser == null) throw new BizException(30, "用户信息无效！");
        /*
         更改关联账号密码
         */
        bizUserService.updateAccountPwd(userPhone, newPwd1);
    }

    @Override
    public void updateCurrAccountPwd(String oldPwd, String newPwd1, String newPwd2) {
        Long accountId = RequestContextHolder.getAccountId();
        if (accountId == null) throw new BizException(10, "未获取到登录账号信息，请重新登录");
        AccountOldPWDRequestDTO accountOldPWDRequestDTO = new AccountOldPWDRequestDTO();
        accountOldPWDRequestDTO.setAccountId(accountId);
        accountOldPWDRequestDTO.setOldPassword(oldPwd);
        accountOldPWDRequestDTO.setPassword(newPwd1);
        accountOldPWDRequestDTO.setPassword2(newPwd2);
        accountApi.updateSelfPasswordOnly(accountOldPWDRequestDTO);
    }

    @Override
    public TokenResponse eduSmsLogin(LoginBaseDTO loginBaseDTO) {
        Map<String, String> headersMap = RequestContextHolder.getHeadersMap();
        if (headersMap.containsKey(ANTNEST_HEADER_TOKEN)) {
            log.debug("检测到登录时带有TOKEN信息,删除携带TOKEN的Header");
            headersMap.remove(ANTNEST_HEADER_TOKEN);
        }

        //检查应用代码是否正确
        String bizCode = loginBaseDTO.getBizCode();
        Application entryApp = applicationApi.getByCode(bizCode);
        AssertUtil.notNull(entryApp, "应用代码参数错误");

        //有可能使用身份证号登录，需要转化成账号
        RequestContextHolder.bind(appProperties.getOrganizationAccountId());
        String loginName = loginBaseDTO.getUserName();
        BizUser bizUser = bizUserService.getByLoginName(loginName);
        if (bizUser != null) {
            loginName = bizUser.getPhone();
            // 如果账号禁用，不允许登录
            if (BizUserStatusEnum.DISABLE.getCode() == bizUser.getStatus()) {
                throw new BizException("当前登录用户已被禁用!");
            }
        }

        //执行登录操作
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setRandom(loginBaseDTO.getRandom());
        loginDTO.setRandomKey(loginBaseDTO.getRandomKey());
        loginDTO.setUserName(loginName);
        loginDTO.setPassword(loginBaseDTO.getPassword());
        loginDTO.setBizCode(bizCode);
        loginDTO.setSystemId(appProperties.getSystemId());
        loginDTO.setOrganizationAccountId(appProperties.getOrganizationAccountId());
        TokenResponse login = uimAuthApi.login(loginDTO);

        //检查是否开通了此入口应用
        Token token = TokenParser.parseToken(login.getToken());
        Long orgAccountId = token.getOrgAccountId();
        boolean open = checkOrganizationOpenApp(entryApp, orgAccountId);
        if (!open) {
            throw new BizException("您所在的组织账号未开通应用[" + entryApp.getName() + "],或者不在服务期限内!");
        }

        // 如果系统关闭，只有区管理员、运维人员可以登录
        Long accountId = token.getAccountId();
        this.doCheckSysStatus(accountId);

        return login;
    }

    @Override
    public TokenResponse eduSmsLoginForWx(DtoLoginForWx dtoLoginForWx) {
        Map<String, String> headersMap = RequestContextHolder.getHeadersMap();
        if (headersMap.containsKey(ANTNEST_HEADER_TOKEN)) {
            log.debug("检测到登录时带有TOKEN信息,删除携带TOKEN的Header");
            headersMap.remove(ANTNEST_HEADER_TOKEN);
        }

        //检查应用代码是否正确
        String bizCode = dtoLoginForWx.getBizCode();
        Application entryApp = applicationApi.getByCode(bizCode);
        AssertUtil.notNull(entryApp, "应用代码参数错误");

        //有可能使用身份证号登录，需要转化成账号
        RequestContextHolder.bind(appProperties.getOrganizationAccountId());
        String loginName = dtoLoginForWx.getUserName();
        BizUser bizUser = bizUserService.getByLoginName(loginName);
        if (bizUser != null) {
            loginName = bizUser.getPhone();
        }

        //执行登录操作
        WxRegisterV2DTO loginDTO = new WxRegisterV2DTO();
        loginDTO.setRandom(dtoLoginForWx.getRandom());
        loginDTO.setRandomKey(dtoLoginForWx.getRandomKey());
        loginDTO.setUserName(loginName);
        loginDTO.setPassword(dtoLoginForWx.getPassword());
        loginDTO.setBizCode(bizCode);
        loginDTO.setOpenId(dtoLoginForWx.getOpenId());
        loginDTO.setEntry(dtoLoginForWx.getEntry());
        TokenResponse login = uimAuthApi.loginFromWxV2(loginDTO);

        //检查是否开通了此入口应用
        Token token = TokenParser.parseToken(login.getToken());
        Long orgAccountId = token.getOrgAccountId();
        boolean open = checkOrganizationOpenApp(entryApp, orgAccountId);
        if (!open) {
            throw new BizException("您所在的组织账号未开通应用[" + entryApp.getName() + "],或者不在服务期限内!");
        }

        // 如果系统关闭，只有区管理员、运维人员可以登录
        Long accountId = token.getAccountId();
        this.doCheckSysStatus(accountId);

        return login;
    }

    @Override
    public VOLoginInfo getLoginInfo() {
        Long accountId = RequestContextHolder.getAccountId();
        if (accountId == null) throw new BizException(10, "未获取到登录账号信息");
        /*
         获取账号基本信息：中文名
         */
        Account account = accountApi.get(accountId);
        if (account == null) throw new BizException(10, "未获取到登录账号信息");
        VOLoginInfo loginInfoVO = new VOLoginInfo();
        loginInfoVO.setCnName(account.getCnName());
        /*
         获取账号基本信息：业务用户ID
         */
        List<Long> accountIdList = new ArrayList<>();
        accountIdList.add(accountId);
        Map<Long, BizUser> bizUserMap = bizUserService.findByAccountIdList(accountIdList);
        if (bizUserMap != null && bizUserMap.size() > 0) {
            BizUser bizUser = bizUserMap.get(accountId);
            if (bizUser != null) {
                loginInfoVO.setUserId(bizUser.getId());
            }
        }
        /*
         获取上一次登录信息
         */
        Object lastRoleCodeObj = redisTemplate.opsForValue().get(VOLoginInfo.REDIS_USER_LAST_ROLE_CODE + accountId);
        if (lastRoleCodeObj != null) loginInfoVO.setLastRoleCode(lastRoleCodeObj.toString());
        Object lastOrgIdObj = redisTemplate.opsForValue().get(VOLoginInfo.REDIS_USER_LAST_ORG_ID + accountId);
        if (lastOrgIdObj != null) loginInfoVO.setLastOrgId(Long.parseLong(lastOrgIdObj.toString()));
        return loginInfoVO;
    }

    @Override
    public List<VOLoginRole> getLoginRoleList() {
        Long accountId = RequestContextHolder.getAccountId();
        if (accountId == null) throw new BizException(10, "未获取到登录账号信息");
        /*
         获取用户所属组织机构信息
         */
        List<OrgAccount> orgAccountList = orgAccountService.findByAccountId(accountId);
        List<VOLoginRole> resultList = new ArrayList<>();
        if (orgAccountList != null && orgAccountList.size() > 0) {
            // 获取系统状态
            SysConf sysConf = sysConfService.getSysConf();
            // 获取角色全集
            List<Role> roleList = roleApi.getAllRolesByOrgAccount(appProperties.getOrganizationAccountId(), appProperties.getSystemId());
            Map<String, Role> roleMap = new HashMap<>();
            if (roleList != null && roleList.size() > 0) {
                roleMap = roleList.stream().collect(Collectors.toMap(Role::getCode, role -> role));
            }
            // 获取机构全集
            List<SmsOrg> smsOrgList = smsOrgService.list();
            Map<Long, SmsOrg> smsOrgMap = new HashMap<>();
            if (smsOrgList != null && smsOrgList.size() > 0) {
                smsOrgMap = smsOrgList.stream().collect(Collectors.toMap(SmsOrg::getId, smsOrg -> smsOrg));
            }
            // 组装结果
            for (OrgAccount orgAccount : orgAccountList) {
                VOLoginRole voLoginRole = new VOLoginRole();
                // 角色编码
                voLoginRole.setRoleCode(orgAccount.getRoleCode());
                // 角色名称
                Role role = roleMap.get(orgAccount.getRoleCode());
                if (role != null) voLoginRole.setRoleName(role.getName());
                // 机构ID
                voLoginRole.setOrgId(orgAccount.getOrgId());
                // 机构名称
                SmsOrg smsOrg = smsOrgMap.get(orgAccount.getOrgId());
                if (smsOrg != null) voLoginRole.setOrgName(smsOrg.getName());
                // 排序号
                voLoginRole.setSortNum(orgAccount.getSortNum());
                // 如果系统关闭，只有区管理员、运维人员身份可以使用
                if (sysConf == null || !sysConf.getStatus()) {
                    if (voLoginRole.getRoleCode().equals(FixedRoleEnum.AREA_ADMIN.getRoleCode()) ||
                            voLoginRole.getRoleCode().equals(FixedRoleEnum.OPER_ADMIN.getRoleCode())) {
                        resultList.add(voLoginRole);
                    }
                } else {
                    resultList.add(voLoginRole);
                }
            }
        }
        return resultList;
    }

    @Override
    public void updateLastLoginRole(DtoUpdateLastLoginRole dto) {
        Long accountId = RequestContextHolder.getAccountId();
        if (accountId == null) throw new BizException(10, "未获取到登录账号信息");
        if (dto == null) throw new BizException("传入对象为空！");
        Long orgId = dto.getOrgId();
        if (orgId == null) throw new BizException("传入的所属机构ID为空，更新用户最近一次登录使用身份失败！");
        String roleCode = dto.getRoleCode();
        if (roleCode == null) throw new BizException("传入的角色编码为空，更新用户最近一次登录使用身份失败！");
        redisTemplate.opsForValue().set(VOLoginInfo.REDIS_USER_LAST_ROLE_CODE + accountId, roleCode);
        redisTemplate.opsForValue().set(VOLoginInfo.REDIS_USER_LAST_ORG_ID + accountId, orgId + "");
    }

    @Override
    public List<ModuleAndPermissionSimpleDTO> getLastLoginRoleMenuList() {
        VOLoginInfo voLoginInfo = this.getLoginInfo();
        if (voLoginInfo == null) return new ArrayList<>();
        String lastRoleCode = voLoginInfo.getLastRoleCode();
        if (StringUtils.isEmpty(lastRoleCode)) return new ArrayList<>();
        /*
         获取当前登录身份所使用角色的ID
         */
        List<Role> roleList = roleApi.getAllRolesByOrgAccount(appProperties.getOrganizationAccountId(), appProperties.getSystemId());
        Long roleId = null;
        if (roleList != null && roleList.size() > 0) {
            for (Role role : roleList) {
                if (role == null) continue;
                if (role.getCode().equals(lastRoleCode)) {
                    roleId = role.getId();
                    break;
                }
            }
        }
        if (roleId == null) return new ArrayList<>();
        /*
         调用框架接口：加载角色对应的菜单列表数据[租户角色授权]
         */
        return authV2Api.getApplicationModuleList(roleId, RequestContextHolder.getApplicationId());
    }

    @Override
    public VOMsgInfo getMsgInfo() {
        // TODO 获取登录用户消息通知
        return null;
    }

    @Override
    public TokenResponse refreshToken(TokenResponse oldToken) {
        return uimAuthApi.refreshToken(oldToken);
    }

    @Override
    public void doCheckCurrUserPwd(String pwd) {
        Long accountId = RequestContextHolder.getAccountId();
        if (accountId == null) throw new BizException(10, "未获取到登录账号信息");
        Account account = accountApi.get(accountId);
        if (account == null) throw new BizException(10, "未获取到登录账号信息");
        String cipherKey = account.getCipherKey();
        String inputPass = CipherUtil.doEncrypt(pwd, cipherKey);
        if (!Objects.equals(inputPass, account.getAccessSecret())) {
            throw new BizException(20, "用户名或者密码错误");
        }
    }


    /*
     * ========================================以下都是内部接口========================================
     */
    @Override
    public TokenResponse ssoLogin(Long accountId) {
        // 如果系统关闭，只有区管理员、运维人员可以登录
        this.doCheckSysStatus(accountId);
        // 尝试登录
        Account account = accountApi.get(accountId);
        Integer bizType = account.getBizType();
        if (bizType == null) {
            throw new BizException(-40, "账号没有业务类型数据");
        }
        AccountBizType.valueOfCode(bizType);
        TokenResponse accountToken = getAccountToken(account, account.getSystemId());
        accountToken.setCode(bizType);
        return accountToken;
    }


    /*
     * ========================================以下都是私有方法========================================
     */

    /**
     * 检查租户是否已经开通了此应用
     *
     * @param app 应用
     * @return 是否开通，开通：true;没有开通：false
     */
    private boolean checkOrganizationOpenApp(Application app, Long orgAccountId) {
        LocalDateTime now = LocalDateTime.now();
        List<OrganizationAppShowDTO> shows = organizationAppApi.getByEntryApplicationId(app.getId(), orgAccountId);
        Optional<OrganizationAppShowDTO> openApp = shows.stream()
                .filter(OrganizationAppShowDTO::isOpen)
                .filter(x -> now.isAfter(x.getStartTime()) && now.isBefore(x.getEndTime()))
                .findAny();
        boolean open = openApp.isPresent();
        log.info("租户：{}，是否开通了应用：{}，查询到的开通结果：{}", orgAccountId, app.getId(), open);
        return open;
    }

    private String checkRandomNameKey(String randomNameKey) {
        if (StringUtils.isEmpty(randomNameKey)) throw new BizException(90, "临时key无效！");
        Object userPhoneObj = redisTemplate.opsForValue().get(VOUserNameCheckResult.REDIS_KEY + randomNameKey);
        if (userPhoneObj == null) throw new BizException(90, "临时key无效！");
        String userPhone = userPhoneObj.toString();
        if (StringUtils.isEmpty(userPhone)) throw new BizException(90, "临时key无效！");
        return userPhone;
    }

    private TokenResponse getAccountToken(Account account, Long systemId) {
        //检查应用代码是否正确
        Application entryApp = applicationApi.getByCode(ApplicationInfo.APPLICATION_CODE);
        AssertUtil.notNull(entryApp, "应用代码参数错误");

        //检查账号是否属于此机构
        List<OrganizationMember> organizationMembers = organizationMemberApi.getByAccounts(Collections.singletonList(account.getId()));
        if (CollectionUtils.isEmpty(organizationMembers)) {
            throw new BizException("账号没有加入组织");
        }
        OrganizationMember organizationMember = organizationMembers.get(0);
        if (null == organizationMember) {
            throw new BizException("账号与组织不匹配");
        }

        //检查账号状态是否正确
        AccountState accountState = AccountState.valueOfCode(account.getState());
        if (!Objects.equals(accountState, AccountState.NORMAL)) {
            String msg = String.format("账号不能登录,原因[%s]", accountState.getName());
            throw new BizException(msg);
        }
        String name = org.apache.commons.lang3.StringUtils.isEmpty(account.getCnName()) ? "" : account.getCnName();
        TokenResponse token = TokenParser
                .getToken(organizationMember.getOrgAccountId(), organizationMember
                        .getAccountId(), name, systemId, entryApp.getId());
        //是否初始密码
        //密码是否已经过期
        int changeDays = account.getPassChangePeriod() * account.getPassChangeUnit();
        boolean needChangePass = account.getPassLastModify().plusDays(changeDays).isBefore(LocalDateTime.now());
        if (account.getInitPass() || needChangePass) {
            token.setCode(BizCode.ERROR_NEED_CHANGE_PASS.value());
        }
        return token;
    }

    private void doCheckSysStatus(Long accountId) {
        SysConf sysConf = sysConfService.getSysConf();
        if (sysConf == null || !sysConf.getStatus()) {
            List<OrgAccount> orgAccountList = orgAccountService.findByAccountId(accountId);
            if (orgAccountList == null || orgAccountList.size() == 0)
                throw new BizException(-30, "系统已关闭，只有区管理员、运维人员可以登录");
            boolean isAdmin = false;
            for (OrgAccount orgAccount : orgAccountList) {
                if (orgAccount == null) continue;
                if (orgAccount.getOrgId() == SmsOrg.ROOT_ID) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) throw new BizException(-30, "系统已关闭，只有区管理员、运维人员可以登录");
        }
    }

}
