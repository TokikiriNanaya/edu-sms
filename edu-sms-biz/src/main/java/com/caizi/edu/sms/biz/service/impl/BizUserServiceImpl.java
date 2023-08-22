package com.caizi.edu.sms.biz.service.impl;

import com.antnest.foss.api.FileObjectApi;
import com.antnest.foss.dto.request.FileBizSingleRequestDTO;
import com.antnest.foss.dto.request.FileObjectBatchSaveRequestDTO;
import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.antnest.mscore.config.prop.AntnestAppProperties;
import com.antnest.mscore.context.RequestContextHolder;
import com.antnest.mscore.exception.BizException;
import com.antnest.mscore.page.Pageable;
import com.antnest.mscore.tx.Tx;
import com.antnest.uam.api.AuthApi;
import com.antnest.uam.dto.AccountAuthRolesDTO;
import com.antnest.uim.api.AccountApi;
import com.antnest.uim.dto.account.request.AccountResetTargetPWDRequestDTO;
import com.antnest.uim.dto.account.request.AccountSystemAndOrgRegisterDTO;
import com.antnest.uim.entity.Account;
import com.antnest.uim.enums.AccountType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caizi.edu.sms.biz.mapper.BizUserMapper;
import com.caizi.edu.sms.biz.service.*;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.login.VOLoginInfo;
import com.caizi.edu.sms.dto.org.DtoSaveOrgUserBatch;
import com.caizi.edu.sms.dto.user.*;
import com.caizi.edu.sms.entity.BizUser;
import com.caizi.edu.sms.entity.BizUserAccount;
import com.caizi.edu.sms.entity.OrgAccount;
import com.caizi.edu.sms.entity.SmsOrg;
import com.caizi.edu.sms.enums.BizUserStatusEnum;
import com.caizi.edu.sms.enums.FileObjectBizEnum;
import com.caizi.edu.sms.enums.FixedRoleEnum;
import com.caizi.edu.sms.enums.IdTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务用户表 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Slf4j
@Service
public class BizUserServiceImpl extends AntnestBaseServiceImpl<BizUserMapper, BizUser> implements BizUserService {

    @Resource
    FileObjectApi fileObjectApi;

    @Resource
    private AuthApi uamAuthApi;

    @Resource
    private AccountApi accountApi;

    @Resource
    private AntnestAppProperties appProperties;

    @Resource
    private BizUserAccountService bizUserAccountService;

    @Resource
    private OrgAccountService orgAccountService;

    @Resource
    private EduSmsLoginService eduSmsLoginService;

    @Resource
    private SmsOrgService smsOrgService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public FileObject getUserCover() {
        Long accountId = RequestContextHolder.getAccountId();
        if (accountId == null) throw new BizException(10, "未获取到登录账号信息");
        FileBizSingleRequestDTO requestDTO = new FileBizSingleRequestDTO();
        requestDTO.setBizObject(FileObjectBizEnum.BIZUSER_COVER.getCode());
        requestDTO.setBizObjectId(accountId);
        List<FileObject> fileObjectList = fileObjectApi.listOfBizObject(requestDTO);
        return fileObjectList != null && fileObjectList.size() > 0 ? fileObjectList.get(0) : null;
    }

    @Override
    public void doUploadUserCover(Long fileObjectId) {
        Long accountId = RequestContextHolder.getAccountId();
        if (accountId == null) throw new BizException(10, "未获取到登录账号信息");
        /*
         删除老附件
         */
        FileBizSingleRequestDTO delDTO = new FileBizSingleRequestDTO();
        delDTO.setBizObject(FileObjectBizEnum.BIZUSER_COVER.getCode());
        delDTO.setBizObjectId(accountId);
        fileObjectApi.delBizObjectFile(delDTO);
        /*
         上传附件
         */
        List<FileObject> fileObjectList = new ArrayList<>();
        FileObject fileObject = new FileObject();
        fileObject.setId(fileObjectId);
        fileObject.setBizType(10);
        fileObject.setBizObjectId(accountId);
        fileObject.setBizObject(FileObjectBizEnum.BIZUSER_COVER.getCode());
        fileObject.setBizObjectName(accountId + "的个人封面");
        fileObjectList.add(fileObject);
        FileObjectBatchSaveRequestDTO requestDTO = new FileObjectBatchSaveRequestDTO();
        requestDTO.setFileObjects(fileObjectList);
        fileObjectApi.submitSaveBatch(requestDTO);
    }

    @Override
    public IPage<BizUser> findUserListForArea(DtoFindUser dtoFindUser) {
        /*
         入参校验
         */
        if (dtoFindUser == null) throw new BizException("传入的查询对象为空！");
        Pageable pageable = dtoFindUser.getPageable();
        if (pageable == null) throw new BizException("传入的分页查询条件为空！");
        /*
         如果传入了查询条件所属机构或用户角色，则限定查询用户范围
         */
        Long orgId = dtoFindUser.getOrgId();
        String bizUserType = dtoFindUser.getBizUserType();
        List<Long> userIdList = null;
        if (orgId != null || !StringUtils.isEmpty(bizUserType)) {
            /*
             查询组织下关联的账号集合
             */
            List<OrgAccount> orgAccList = orgAccountService.findByOrgIdAndRoleCode(orgId, bizUserType);
            if (orgAccList == null || orgAccList.size() == 0) return new Page<>();
            /*
             根据组织关联账号集合，过滤出用户ID集合
             */
            List<Long> accIdList = orgAccList.stream().map(OrgAccount::getAccountId).collect(Collectors.toList());
            List<BizUserAccount> userAccList = bizUserAccountService.findByAccountIdList(accIdList);
            if (userAccList == null || userAccList.size() == 0) return new Page<>();
            userIdList = userAccList.stream().map(BizUserAccount::getBizUserId).collect(Collectors.toList());
        }
        /*
         查询结果
         */
        DtoFindPageCondition condition = new DtoFindPageCondition();
        condition.setUserIdList(userIdList);
        condition.setKeyword(dtoFindUser.getBizUserName());
        condition.setBizUserStatus(dtoFindUser.getBizUserStatus());
        condition.setPageable(pageable);
        return this.findByCondition(condition);
    }

    @Override
    public IPage<BizUser> findUserListForSchool(DtoFindUser dtoFindUser) {
        /*
         限定查询范围只能当前账号所属学校
         */
        VOLoginInfo loginInfoVO = eduSmsLoginService.getLoginInfo();
        Long lastOrgId = loginInfoVO.getLastOrgId();
        if (lastOrgId == null) throw new BizException("无法获取到登录用户的当前使用身份，查询用户列表失败！");
        dtoFindUser.setOrgId(lastOrgId);
        return this.findUserListForArea(dtoFindUser);
    }

    @Override
    public List<VOUserOrgMain> findUserOrgInfoBatch(List<Long> bizUserIdList) {
        if (bizUserIdList == null || bizUserIdList.size() == 0) return new ArrayList<>();
        /*
         查询用户关联账号
         */
        List<BizUserAccount> bizUserAccountList = bizUserAccountService.findByUserIdList(bizUserIdList);
        if (bizUserAccountList == null || bizUserAccountList.size() == 0) return new ArrayList<>();
        List<Long> accountIdList = bizUserAccountList.stream().map(BizUserAccount::getAccountId).collect(Collectors.toList());
        /*
         查询每个账号所属的组织机构集合
         */
        Map<Long, List<SmsOrg>> accOrgMap = smsOrgService.findByAccountIdList(accountIdList);// key：账号ID
        /*
         组装结果集合
         */
        List<VOUserOrgMain> voMainList = new ArrayList<>();
        for (BizUserAccount bizUserAccount : bizUserAccountList) {
            VOUserOrgMain voMain = new VOUserOrgMain();
            voMain.setBizUserId(bizUserAccount.getBizUserId());
            List<SmsOrg> smsOrgList = accOrgMap.get(bizUserAccount.getAccountId());
            if (smsOrgList != null && smsOrgList.size() > 0) {
                List<VOUserOrgInfo> voList = new ArrayList<>();
                for (SmsOrg smsOrg : smsOrgList) {
                    VOUserOrgInfo vo = new VOUserOrgInfo();
                    vo.setOrgId(smsOrg.getId());
                    vo.setOrgName(smsOrg.getName());
                    voList.add(vo);
                }
                voMain.setUserOrgList(voList);
            } else {
                voMain.setUserOrgList(new ArrayList<>());
            }
            voMainList.add(voMain);
        }
        return voMainList;
    }

    @Override
    public BizUser getUserByPhoneOrIdNumber(String phone, String idNumber) {
        if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(idNumber)) throw new BizException("电话号码或身份证号必须输入其中一项！");
        QueryWrapper<BizUser> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(phone)) {
            queryWrapper.eq(BizUser.PHONE, phone);
        }
        if (!StringUtils.isEmpty(idNumber)) {
            queryWrapper.eq(BizUser.ID_NUMBER, idNumber);
        }
        List<BizUser> userList = baseMapper.selectList(queryWrapper);
        return userList != null && userList.size() > 0 ? userList.get(0) : null;
    }

    @Tx
    @Override
    public BizUser saveUserAndAccount(DtoSaveUser dtoSaveUser) {
        /*
         必传项：
            1)姓名
            2)电话
            3)证件类型
            4)证件号码
            用户状态
         */
        if (dtoSaveUser == null) throw new BizException("传入的对象为空！");
        String name = dtoSaveUser.getName();
        if (StringUtils.isEmpty(name)) throw new BizException("传入的姓名为空！");
        String phone = dtoSaveUser.getPhone();
        if (StringUtils.isEmpty(phone)) throw new BizException("传入的电话为空！");
        Integer idType = dtoSaveUser.getIdType();
        if (idType == null) throw new BizException("传入的证件类型为空！");
        String idNumber = dtoSaveUser.getIdNumber();
        if (idNumber == null) throw new BizException("传入的证件号码为空！");
        // 其它字段
        Long userId = dtoSaveUser.getId();
        /*
         需校验唯一性：电话、证件号
         */
        if (!this.doCheckPhoneOnly(userId, phone)) throw new BizException("填写的【电话号码】已被使用，保存失败");
        if (!this.doCheckIdNumberOnly(userId, idNumber)) throw new BizException("填写的【证件号码】已被使用，保存失败");
        /*
         保存业务用户信息、以及关联账户信息
         */
        BizUser bizUser;
        if (userId == null) {// 新增
            bizUser = new BizUser();
            DtoSaveUser.convertToBizUser(dtoSaveUser, bizUser);
            bizUser.setStatus(BizUserStatusEnum.ENABLE.getCode());
            super.save(bizUser);
            // 根据电话号码，自动生成系统账号
            this.saveAccountByBizUser(bizUser, ApplicationInfo.INIT_PASSWORD);
        } else {// 修改
            bizUser = super.getById(userId);
            if (bizUser == null) throw new BizException("未查询到用户信息，更新失败！");
            DtoSaveUser.convertToBizUser(dtoSaveUser, bizUser);
            super.updateById(bizUser);
            // 连同更新账号信息
            this.updateAccountByBizUser(bizUser);
        }
        return bizUser;
    }

    @Tx
    @Override
    public BizUser saveSchoolUserAndAccount(DtoSaveUser dtoSaveUser) {
        /*
         校验当前登录身份是否是“校管理员”
         */
        VOLoginInfo voLoginInfo = eduSmsLoginService.getLoginInfo();
        String lastRoleCode = voLoginInfo.getLastRoleCode();
        if (StringUtils.isEmpty(lastRoleCode)) throw new BizException("未查询到登录用户的身份，保存失败！");
        Long lastOrgId = voLoginInfo.getLastOrgId();
        if (lastOrgId == null) throw new BizException("未查询到登录用户的所属机构，保存失败！");
        if (!lastRoleCode.equals(FixedRoleEnum.SCHOOL_ADMIN.getRoleCode()))
            throw new BizException("只有校管理员能添加本校用户，保存失败！");
        /*
         创建用户信息及账号
         */
        BizUser bizUser = this.saveUserAndAccount(dtoSaveUser);
        /*
         如果是新增，将账号加入该学校
         */
        if (dtoSaveUser.getId() == null && bizUser != null) {
            DtoSaveOrgUserBatch dtoSaveOrgUserBatch = new DtoSaveOrgUserBatch();
            dtoSaveOrgUserBatch.setOrgId(lastOrgId);
            dtoSaveOrgUserBatch.setBizUserType(FixedRoleEnum.SCHOOL_USER.getRoleCode());
            List<Long> bizUserIdList = new ArrayList<>();
            bizUserIdList.add(bizUser.getId());
            dtoSaveOrgUserBatch.setBizUserIdList(bizUserIdList);
            smsOrgService.saveOrgUserBatch(dtoSaveOrgUserBatch);
        }
        return bizUser;
    }

    @Tx
    @Override
    public void doChangeUserStatusBatch(DtoChangeUserStatusBatch dtoChangeUserStatusBatch) {
        if (dtoChangeUserStatusBatch == null) throw new BizException("传入的对象为空！");
        Integer operFlag = dtoChangeUserStatusBatch.getOperFlag();
        if (operFlag == null) throw new BizException("传入的操作标识为空！");
        List<Long> bizUserIdList = dtoChangeUserStatusBatch.getBizUserIdList();
        if (bizUserIdList == null) throw new BizException("传入的用户ID集合为空！");
        if (!BizUserStatusEnum.containCode(operFlag)) throw new BizException("传入的操作标识是无效值！");
        UpdateWrapper<BizUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(BizUser.STATUS, operFlag);
        updateWrapper.in(BizUser.ID, bizUserIdList);
        super.update(updateWrapper);
    }

    @Tx
    @Override
    public void removeUser(Long bizUserId) {
        /*
         删除用户
         */
        super.removeById(bizUserId);
        /*
         根据业务用户ID，查询出对应的账号集合
         */
        List<BizUserAccount> userAccList = bizUserAccountService.findByUserId(bizUserId);
        if (userAccList == null || userAccList.size() == 0) return;
        List<Long> accountIdList = userAccList.stream().map(BizUserAccount::getAccountId).collect(Collectors.toList());
        /*
         查找出用户账号对应的角色，并删除
         */
        List<Long> accAuthIdList = new ArrayList<>();
        for (Long accountId : accountIdList) {
            List<AccountAuthRolesDTO> accountAllRoleList = uamAuthApi.getAccountAllRoles(accountId, ApplicationInfo.APPLICATION_CODE);
            if (accountAllRoleList == null || accountAllRoleList.size() == 0) continue;
            accAuthIdList.addAll(accountAllRoleList.stream().map(AccountAuthRolesDTO::getId).collect(Collectors.toList()));
        }
        if (accAuthIdList.size() > 0) {
            uamAuthApi.deleteAccountAuth(accAuthIdList);
        }
        /*
         删除账号
         */
        accountApi.deleteByIds(accountIdList);
    }


    /*
     * ========================================以下都是内部接口========================================
     */
    @Override
    public BizUser getByLoginName(String loginName) {
        if (StringUtils.isEmpty(loginName)) return null;
        /*
         先根据电话号码匹配一次
         */
        QueryWrapper<BizUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BizUser.PHONE, loginName);
        List<BizUser> bizUserList = baseMapper.selectList(queryWrapper);
        if (bizUserList != null && bizUserList.size() > 0) return bizUserList.get(0);
        /*
         再根据身份证号码匹配一次
         */
        QueryWrapper<BizUser> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq(BizUser.ID_TYPE, IdTypeEnum.RESIDENT.getCode());
        queryWrapper2.eq(BizUser.ID_NUMBER, loginName);
        List<BizUser> bizUserList2 = baseMapper.selectList(queryWrapper2);
        if (bizUserList2 != null && bizUserList2.size() > 0) {
            return bizUserList2.get(0);
        }
        return null;
    }

    @Override
    public void updateAccountPwd(String phoneNum, String pwd) {
        if (StringUtils.isEmpty(phoneNum)) throw new BizException("传入电话号码为空！");
        if (StringUtils.isEmpty(pwd)) throw new BizException("传入密码为空！");
        List<Account> oldAccountList = accountApi.getByLoginName(phoneNum);
        if (oldAccountList == null || oldAccountList.size() == 0)
            throw new BizException("未查询到电话号码为【" + phoneNum + "】的账号！");
        /*
         更新为输入密码
         */
        Account account = oldAccountList.get(0);
        AccountResetTargetPWDRequestDTO dto = new AccountResetTargetPWDRequestDTO();
        dto.setAccountId(account.getId());
        dto.setPassword(pwd);
        accountApi.reset2TargetPassword(dto);
    }

    @Override
    public Long saveAccountByBizUser(BizUser bizUser, String pwd) {
        if (bizUser == null) throw new BizException("传入对象为空！");
        if (StringUtils.isEmpty(pwd)) throw new BizException("传入密码为空！");
        Long bizUserId = bizUser.getId();
        if (bizUserId == null) throw new BizException("传入对象ID为空！");
        String phone = bizUser.getPhone();
        if (StringUtils.isEmpty(phone)) throw new BizException("传入电话号码为空！");
        String name = bizUser.getName();
        if (StringUtils.isEmpty(phone)) throw new BizException("传入电话号码为空！");
        /*
         先查询是否已存在该手机号注册的账号
         */
        Long accountId;
        List<Account> oldAccountList = accountApi.getByLoginName(phone);
        if (oldAccountList != null && oldAccountList.size() > 0) {
            Account oldAccount = oldAccountList.get(0);
            log.info("账号表中已存在相同电话号码【" + phone + "】的用户账号：" + oldAccount);
            // 暂时只获取ID，不去更新已存在账号的中文名等信息，有利于运维时追踪是否变更过绑定人
            accountId = oldAccount.getId();
        } else {
            /*
             创建账号
             */
            AccountSystemAndOrgRegisterDTO registerDTO = new AccountSystemAndOrgRegisterDTO();
            registerDTO.setLoginName(phone);
            registerDTO.setCnName(name);
            registerDTO.setNickName(name);
            registerDTO.setOrgAccountId(appProperties.getOrganizationAccountId());
            registerDTO.setPhone(phone);
            registerDTO.setSystemId(appProperties.getSystemId());
            registerDTO.setType(AccountType.PERSON.getCode());
            accountId = accountApi.registerAccountToSystemAndOrg(registerDTO);
            /*
             更新为输入密码
             */
            AccountResetTargetPWDRequestDTO dto = new AccountResetTargetPWDRequestDTO();
            dto.setAccountId(accountId);
            dto.setPassword(pwd);
            accountApi.reset2TargetPassword(dto);
        }
        /*
         重新绑定业务用户与账号关联信息
         */
        bizUserAccountService.removeByBizUserId(bizUserId);
        bizUserAccountService.removeByAccountId(accountId);
        BizUserAccount bizUserAccount = new BizUserAccount();
        bizUserAccount.setBizUserId(bizUserId);
        bizUserAccount.setAccountId(accountId);
        bizUserAccountService.save(bizUserAccount);
        return accountId;
    }

    @Override
    public void updateAccountByBizUser(BizUser bizUser) {
        if (bizUser == null) throw new BizException("传入对象为空！");
        String phone = bizUser.getPhone();
        if (StringUtils.isEmpty(phone)) throw new BizException("传入电话号码为空！");
        String name = bizUser.getName();
        if (StringUtils.isEmpty(phone)) throw new BizException("传入电话号码为空！");
        /*
         先查询是否已存在该手机号注册的账号
         */
        List<Account> oldAccountList = accountApi.getByLoginName(phone);
        if (oldAccountList == null || oldAccountList.size() == 0) throw new BizException("更改用户账号信息失败，未查询到用户对应的账号！");
        // 更新账号信息
        for (Account account : oldAccountList) {
            if (account == null) continue;
            if (!account.getPhone().equals(phone)) continue;
            account.setCnName(name);
            account.setNickName(name);
            accountApi.update(account);
        }
    }

    @Override
    public IPage<BizUser> findByCondition(DtoFindPageCondition condition) {
        /*
         1. 入参检查
         */
        if (condition == null) throw new BizException("查询失败，传入的查询条件对象为空，请联系运维管理员");
        Pageable pageable = condition.getPageable();
        if (pageable == null) throw new BizException("查询失败，传入的分页查询条件为空，请联系运维管理员");
        /*
         2. 查询条件封装
         */
        QueryWrapper<BizUser> queryWrapper = new QueryWrapper<>();
        List<Long> userIdList = condition.getUserIdList();
        if (userIdList != null && userIdList.size() > 0) {
            queryWrapper.in(BizUser.ID, userIdList);
        }
        String keyword = condition.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.and(wrapper -> wrapper.like(BizUser.NAME, keyword).or().like(BizUser.PHONE, keyword).or().like(BizUser.ID_NUMBER, keyword));
        }
        Integer bizUserStatus = condition.getBizUserStatus();
        if (bizUserStatus != null) {
            queryWrapper.eq(BizUser.STATUS, bizUserStatus);
        }
        IPage<BizUser> page = super.getPage(pageable);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Map<Long, BizUser> findByAccountIdList(List<Long> accountIdList) {
        if (accountIdList == null || accountIdList.size() == 0) return new HashMap<>();
        /*
         查找关联关系
         */
        List<BizUserAccount> bizUserAccountList = bizUserAccountService.findByAccountIdList(accountIdList);
        if (bizUserAccountList == null || bizUserAccountList.size() == 0) return new HashMap<>();
        /*
         获取用户集合，并转为MAP
         */
        List<Long> userIdList = bizUserAccountList.stream().map(BizUserAccount::getBizUserId).collect(Collectors.toList());
        List<BizUser> userList = super.getByIds(userIdList);
        Map<Long, BizUser> userMap = new HashMap<>();// key：用户ID
        if (userList != null && userList.size() > 0) {
            userMap = userList.stream().collect(Collectors.toMap(BizUser::getId, user -> user));
        }
        /*
         组装结果集
         */
        Map<Long, BizUser> resultMap = new HashMap<>();// key：账号ID
        for (BizUserAccount bizUserAccount : bizUserAccountList) {
            if (bizUserAccount == null) continue;
            if (resultMap.get(bizUserAccount.getAccountId()) != null) continue;
            BizUser bizUser = userMap.get(bizUserAccount.getBizUserId());
            if (bizUser != null) resultMap.put(bizUserAccount.getAccountId(), bizUser);
        }
        return resultMap;
    }


    /*
     * ========================================以下都是私有方法========================================
     */
    private boolean doCheckPhoneOnly(Long userId, String phone) {
        // 传入的电话号码不能为空，否则直接判定为不重复（因为没有）
        if (StringUtils.isEmpty(phone)) return true;
        QueryWrapper<BizUser> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.ne(BizUser.ID, userId);
        }
        queryWrapper.eq(BizUser.PHONE, phone);
        List<BizUser> userList = baseMapper.selectList(queryWrapper);
        return userList == null || userList.size() <= 0;// true：是唯一的；false:已被占用
    }

    private List<BizUser> doCheckIdNumberOnlyBatch(List<String> idNumberList) {
        if (idNumberList == null || idNumberList.size() == 0) return new ArrayList<>();
        QueryWrapper<BizUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(BizUser.ID_NUMBER, idNumberList);
        return baseMapper.selectList(queryWrapper);
    }

    private List<BizUser> doCheckPhoneOnlyBatch(List<String> phoneList) {
        if (phoneList == null || phoneList.size() == 0) return new ArrayList<>();
        QueryWrapper<BizUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(BizUser.PHONE, phoneList);
        return baseMapper.selectList(queryWrapper);
    }

    private boolean doCheckIdNumberOnly(Long userId, String idNumber) {
        // 传入的证件号码不能为空，否则直接判定为不重复（因为没有）
        if (StringUtils.isEmpty(idNumber)) return true;
        QueryWrapper<BizUser> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.ne(BizUser.ID, userId);
        }
        queryWrapper.eq(BizUser.ID_NUMBER, idNumber);
        List<BizUser> userList = baseMapper.selectList(queryWrapper);
        return userList == null || userList.size() <= 0;// true：是唯一的；false:已被占用
    }

    private List<BizUser> doImportBizUser(List<String> descMsgList, Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        List<BizUser> bizUserList = new ArrayList<>();
        if (sheet != null) {
            Set<String> idNumberSet = new HashSet<>();
            Set<String> nameSet = new HashSet<>();
            Set<String> phoneSet = new HashSet<>();

            /*
             1. 校验【用户信息采集表】sheet文本内容
             */
            for (int r = 3; r <= sheet.getLastRowNum(); r++) {
                Row currRow = sheet.getRow(r);
                if (currRow == null) break;
                // 如果姓名为空，则认为读取结束！
                Cell currCell = currRow.getCell(1);
                String name = currCell.getStringCellValue();// *学校名称
                if (StringUtils.isEmpty(name)) {
                    break;
                }

                BizUser bizUser = new BizUser();

                // *姓名
                if (StringUtils.isEmpty(name)) {
                    descMsgList.add("第" + (r + 1) + "行的【姓名】为空");
                } else if (nameSet.contains(name)) {
                    descMsgList.add("第" + (r + 1) + "行的【姓名】在表格中出现过两次以上");
                }
                bizUser.setName(name);
                nameSet.add(name);

                // *电话
                currCell = currRow.getCell(2);
                currCell.setCellType(CellType.STRING);
                String phoneNum = currCell.getStringCellValue();
                if (StringUtils.isEmpty(phoneNum)) {
                    descMsgList.add("第" + (r + 1) + "行的【手机号】为空");
                } else if (phoneSet.contains(phoneNum)) {
                    descMsgList.add("第" + (r + 1) + "行的【手机号】在表格中出现过两次以上");
                }
                bizUser.setPhone(phoneNum);
                phoneSet.add(phoneNum);

                // *证件类型
                currCell = currRow.getCell(3);
                String idTypeStr = currCell.getStringCellValue();
                if (StringUtils.isEmpty(idTypeStr)) {
                    descMsgList.add("第" + (r + 1) + "行的【证件类型】为空");
                }
                Integer idType = IdTypeEnum.getCodeByDesc(idTypeStr);
                if (idType == null) {
                    descMsgList.add("第" + (r + 1) + "行的【证件类型】不是有效的数据");
                }
                bizUser.setIdType(idType);

                // *证件号码
                currCell = currRow.getCell(4);
                currCell.setCellType(CellType.STRING);
                String idNumber = currCell.getStringCellValue();
                if (StringUtils.isEmpty(idNumber)) {
                    descMsgList.add("第" + (r + 1) + "行的【证件号码】为空");
                } else if (idNumberSet.contains(idNumber)) {
                    descMsgList.add("第" + (r + 1) + "行的【证件号码】在表格中出现过两次以上");
                }
                bizUser.setIdNumber(idNumber);
                idNumberSet.add(idNumber);

                bizUser.setStatus(BizUserStatusEnum.ENABLE.getCode());
                bizUserList.add(bizUser);
            }

            /*
             2. 唯一性校验
             */
            if (bizUserList.size() > 0) {
                List<String> idNumberList = new ArrayList<>();
                List<String> phoneNumList = new ArrayList<>();
                for (BizUser bizUser : bizUserList) {
                    idNumberList.add(bizUser.getIdNumber());
                    phoneNumList.add(bizUser.getPhone());
                }
                // 证件号码
                List<BizUser> checkList = this.doCheckIdNumberOnlyBatch(idNumberList);
                if (checkList != null && checkList.size() > 0) {
                    StringBuilder sb = new StringBuilder("以下【证件号码】已被注册使用，导入失败：");
                    for (BizUser check : checkList) {
                        sb.append("【").append(check.getIdNumber()).append("】");
                    }
                    descMsgList.add(sb.toString());
                }
                // 手机号
                checkList = this.doCheckPhoneOnlyBatch(phoneNumList);
                if (checkList != null && checkList.size() > 0) {
                    StringBuilder sb = new StringBuilder("以下【手机号】已被注册使用，导入失败：");
                    for (BizUser check : checkList) {
                        sb.append("【").append(check.getPhone()).append("】");
                    }
                    descMsgList.add(sb.toString());
                }
            }
        } else {
            String failMsg = "上传的excel文件，sheet为空，导入数据失败！";
            descMsgList.add(failMsg);
        }
        return bizUserList;
    }

    private List<BizUser> doImportBizUserForSchool(List<String> descMsgList, Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        List<BizUser> bizUserList = new ArrayList<>();
        if (sheet != null) {
            Set<String> phoneSet = new HashSet<>();

            /*
             1. 校验【用户信息采集表】sheet文本内容
             */
            for (int r = 3; r <= sheet.getLastRowNum(); r++) {
                Row currRow = sheet.getRow(r);
                if (currRow == null) break;
                // 如果姓名为空，则认为读取结束！
                Cell currCell = currRow.getCell(1);
                String name = currCell.getStringCellValue();
                if (StringUtils.isEmpty(name)) {
                    break;
                }

                BizUser bizUser = new BizUser();

                // *姓名
                if (StringUtils.isEmpty(name)) {
                    descMsgList.add("第" + (r + 1) + "行的【姓名】为空");
                }
                bizUser.setName(name);

                // *电话
                currCell = currRow.getCell(2);
                currCell.setCellType(CellType.STRING);
                String phoneNum = currCell.getStringCellValue();
                if (StringUtils.isEmpty(phoneNum)) {
                    descMsgList.add("第" + (r + 1) + "行的【手机号】为空");
                } else if (phoneSet.contains(phoneNum)) {
                    descMsgList.add("第" + (r + 1) + "行的【手机号】在表格中出现过两次以上");
                }
                bizUser.setPhone(phoneNum);
                phoneSet.add(phoneNum);

                bizUserList.add(bizUser);
            }
        } else {
            String failMsg = "上传的excel文件，sheet为空，导入数据失败！";
            descMsgList.add(failMsg);
        }
        return bizUserList;
    }
}
