package com.caizi.edu.sms.biz.service.impl;

import com.antnest.amc.api.RoleApi;
import com.antnest.amc.entity.Role;
import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.antnest.mscore.config.prop.AntnestAppProperties;
import com.antnest.mscore.exception.BizException;
import com.antnest.mscore.page.Pageable;
import com.antnest.mscore.tx.Tx;
import com.antnest.uam.api.AuthApi;
import com.antnest.uam.dto.AccountAuthRolesDTO;
import com.antnest.uam.dto.input.RoleAccountsDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caizi.edu.sms.biz.mapper.SmsOrgMapper;
import com.caizi.edu.sms.biz.service.*;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.org.*;
import com.caizi.edu.sms.dto.user.DtoFindPageCondition;
import com.caizi.edu.sms.entity.*;
import com.caizi.edu.sms.enums.FixedRoleEnum;
import com.caizi.edu.sms.enums.OrgTypeEnum;
import com.caizi.edu.sms.enums.PeriodEnum;
import com.caizi.edu.sms.enums.SyncBigDataPlatEnum;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 综合机构表 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Service
public class SmsOrgServiceImpl extends AntnestBaseServiceImpl<SmsOrgMapper, SmsOrg> implements SmsOrgService {

    @Resource
    private RoleApi roleApi;

    @Resource
    private AuthApi uamAuthApi;

    @Resource
    private AntnestAppProperties appProperties;

    @Resource
    private OrgAccountService orgAccountService;

    @Resource
    private BizUserAccountService bizUserAccountService;

    @Resource
    private BizUserService bizUserService;

    @Resource
    private OrgSchoolService orgSchoolService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public List<SmsOrg> findOrgTree(DtoFindOrgTree dtoFindOrgTree) {
        /*
         根据查询条件筛选出直接匹配结果
         */
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        if (dtoFindOrgTree != null) {
            String orgName = dtoFindOrgTree.getOrgName();
            if (!StringUtils.isEmpty(orgName)) {
                queryWrapper.like(SmsOrg.NAME, orgName);
            }
        }
        List<SmsOrg> searchList = baseMapper.selectList(queryWrapper);
        if (searchList == null || searchList.size() == 0) return new ArrayList<>();
        /*
         递归查询父节点并排序
         */
        return this.findParentNodeAndSort(searchList);
    }

    @Override
    public IPage<BizUser> findOrgUserList(DtoFindOrgUser dtoFindOrgUser) {
        if (dtoFindOrgUser == null) throw new BizException("传入的查询对象为空！");
        Long orgId = dtoFindOrgUser.getOrgId();
        if (orgId == null) throw new BizException("传入的机构ID为空！");
        Pageable pageable = dtoFindOrgUser.getPageable();
        if (pageable == null) throw new BizException("传入的分页查询条件为空！");
        /*
         查询组织下关联的账号集合
         */
        List<OrgAccount> orgAccList = orgAccountService.findByOrgIdAndRoleCode(orgId, dtoFindOrgUser.getBizUserType());
        if (orgAccList == null || orgAccList.size() == 0) return new Page<>();
        /*
         根据组织关联账号集合，过滤出用户ID集合
         */
        List<Long> accIdList = orgAccList.stream().map(OrgAccount::getAccountId).collect(Collectors.toList());
        List<BizUserAccount> userAccList = bizUserAccountService.findByAccountIdList(accIdList);
        if (userAccList == null || userAccList.size() == 0) return new Page<>();
        /*
         根据其它查询条件，筛选用户列表
         */
        List<Long> userIdList = userAccList.stream().map(BizUserAccount::getBizUserId).collect(Collectors.toList());
        DtoFindPageCondition condition = new DtoFindPageCondition();
        condition.setUserIdList(userIdList);
        condition.setKeyword(dtoFindOrgUser.getKeyword());
        condition.setPageable(pageable);
        return bizUserService.findByCondition(condition);
    }

    @Tx
    @Override
    public SmsOrg saveOrg(DtoSaveOrg dtoSaveOrg) {
        /*
         入参校验：
             1)组织节点名称
             2)上级节点ID
             3)机构类型
         */
        if (dtoSaveOrg == null) throw new BizException("传入的对象为空！");
        String name = dtoSaveOrg.getName();
        if (StringUtils.isEmpty(name)) throw new BizException("名称不能为空！");
        Long pid = dtoSaveOrg.getPid();
        if (pid == null) throw new BizException("上级节点ID不能为空！");
        Integer orgType = dtoSaveOrg.getOrgType();
        if (orgType == null) throw new BizException("机构类型不能为空！");
        // 其它字段
        Long id = dtoSaveOrg.getId();
        /*
         需校验唯一性：当前父节点下唯一
         */
        if (!this.doCheckNameOnly(id, name)) throw new BizException("已存在名为【" + name + "】的机构，保存失败");
        /*
         修改其父节点的叶子标识
         */
        SmsOrg parentOrg = super.getById(pid);
        if (pid != SmsOrg.ROOT_PID && parentOrg == null) {
            throw new BizException("未查询到机构的父节点信息，更新失败！");
        } else if (parentOrg != null) {
            parentOrg.setLeafFlag(false);
            super.updateById(parentOrg);
        }
        /*
         若是新增，排序号需根据当前父节点下的直接子节点数+1
         */
        SmsOrg smsOrg;
        if (id == null) {// 新增
            smsOrg = new SmsOrg();
            DtoSaveOrg.convertToSmsOrg(dtoSaveOrg, smsOrg);
            List<SmsOrg> childOrgList = this.findDirectChildOrgList(pid);
            smsOrg.setSortNum(childOrgList.size() + 1);
            smsOrg.setLeafFlag(true);
            if (pid != SmsOrg.ROOT_PID) {
                smsOrg.setParentPathFull(parentOrg.getParentPathFull() + "/" + parentOrg.getName());
            } else {
                smsOrg.setParentPathFull("");
            }
            super.save(smsOrg);
        } else {// 修改
            smsOrg = super.getById(id);
            if (smsOrg == null) throw new BizException("未查询到机构信息，更新失败！");
            DtoSaveOrg.convertToSmsOrg(dtoSaveOrg, smsOrg);
            if (pid == SmsOrg.ROOT_PID) {// 根节点
                smsOrg.setParentPathFull("");
            } else if (pid == SmsOrg.ROOT_ID) {// 根节点下第一层
                smsOrg.setParentPathFull(parentOrg.getName());
            } else {// 根节点下其它层
                smsOrg.setParentPathFull(parentOrg.getParentPathFull() + "/" + parentOrg.getName());
            }
            super.updateById(smsOrg);
        }
        return smsOrg;
    }

    @Tx
    @Override
    public void removeOrg(Long orgId) {
        if (orgId == null) throw new BizException("未传入机构ID，删除失败！");
        SmsOrg currOrg = super.getById(orgId);
        if (currOrg == null) throw new BizException("未查询到机构信息，删除失败！");
        /*
         1)如果是根节点，不允许删除
         */
        if (orgId == SmsOrg.ROOT_ID) throw new BizException("当前节点是根节点，不允许删除");
        /*
         2)如果拥有子机构，不允许删除
         */
        List<SmsOrg> childList = this.findDirectChildOrgList(orgId);
        if (childList.size() > 0) throw new BizException("当前节点拥有子机构，不允许删除！");
        /*
         3）如果是叶子节点，机构下有用户数据，不允许删除
         */
        List<OrgAccount> accList = orgAccountService.findByOrgIdAndRoleCode(orgId, null);
        if (accList.size() > 0) throw new BizException("当前节点下尚有用户数据，不允许删除！");
        /*
         校验通过，删除
         */
        UpdateWrapper<SmsOrg> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(SmsOrg.ID, orgId);
        baseMapper.delete(updateWrapper);
        /*
         如果其父节点下已无子节点，修改叶子标识
         */
        this.updateParentOrgLeafFlag(currOrg);
        /*
         该节点后续的节点，排序号-1
         */
        int currSortNum = currOrg.getSortNum();
        long pid = currOrg.getPid();
        this.updateAfterOrgSortNum(pid, currSortNum);
    }

    @Override
    public void removeOrgBatch(List<Long> orgIdList) {
        if (orgIdList == null || orgIdList.size() == 0) throw new BizException("未传入机构ID集合，删除失败！");
        List<SmsOrg> currOrgList = super.getByIds(orgIdList);
        if (currOrgList == null || currOrgList.size() == 0) throw new BizException("未查询到任何一个机构信息，删除失败！");
        List<Long> searchOrgIdList = currOrgList.stream().map(SmsOrg::getId).collect(Collectors.toList());
        Map<Long, SmsOrg> searchOrgIdMap = currOrgList.stream().collect(Collectors.toMap(SmsOrg::getId, smsOrg -> smsOrg));// key：机构ID
        /*
         1)如果是根节点，不允许删除
         */
        for (SmsOrg currOrg : currOrgList) {
            if (currOrg == null) continue;
            if (currOrg.getId() == SmsOrg.ROOT_ID) throw new BizException("【" + currOrg.getName() + "】是根节点，不允许删除");
        }
        /*
         2)如果拥有子机构，不允许删除
         */
        List<SmsOrg> childList = this.findDirectChildOrgList(searchOrgIdList);
        if (childList.size() > 0) {
            for (SmsOrg childOrg : childList) {
                if (childOrg == null) continue;
                SmsOrg pOrg = searchOrgIdMap.get(childOrg.getPid());
                if (pOrg == null) continue;
                throw new BizException("【" + pOrg.getName() + "】拥有子机构，不允许删除！");
            }
        }
        /*
         3）如果是叶子节点，机构下有用户数据，不允许删除
         */
        List<OrgAccount> accList = orgAccountService.findByOrgIdListAndRoleCode(searchOrgIdList, null);
        if (accList.size() > 0) {
            for (OrgAccount orgAccount : accList) {
                if (orgAccount == null) continue;
                SmsOrg mainOrg = searchOrgIdMap.get(orgAccount.getOrgId());
                if (mainOrg == null) continue;
                throw new BizException("【" + mainOrg.getName() + "】尚有用户数据，不允许删除！");
            }
        }

        if (searchOrgIdList.size() > 0) {
            /*
             校验通过，删除
             */
            super.removeByIds(searchOrgIdList);

            for (SmsOrg currOrg : currOrgList) {
                /*
                 如果其父节点下已无子节点，修改叶子标识
                 */
                this.updateParentOrgLeafFlag(currOrg);
                /*
                 该节点后续的节点，排序号-1
                 */
                int currSortNum = currOrg.getSortNum();
                long pid = currOrg.getPid();
                this.updateAfterOrgSortNum(pid, currSortNum);
            }

        }

    }

    @Tx
    @Override
    public void saveOrgUserBatch(DtoSaveOrgUserBatch dtoSaveOrgUserBatch) {
        /*
         入参校验
         */
        if (dtoSaveOrgUserBatch == null) throw new BizException("传入的对象为空！");
        Long orgId = dtoSaveOrgUserBatch.getOrgId();
        if (orgId == null) throw new BizException("传入的机构ID为空！");
        List<Long> bizUserIdList = dtoSaveOrgUserBatch.getBizUserIdList();
        if (bizUserIdList == null || bizUserIdList.size() == 0) throw new BizException("传入的用户ID集合为空！");
        /*
         1)如果机构既不是根节点、也不是叶子节点，不能添加用户
         */
        SmsOrg smsOrg = super.getById(orgId);
        if (smsOrg == null) throw new BizException("未查询到机构信息，添加用户失败！");
        if (smsOrg.getId() != SmsOrg.ROOT_ID && !smsOrg.getLeafFlag()) throw new BizException("当前节点不能添加用户！");
        /*
         2)如果机构是根节点，则添加的是“区管理员”
         3)如果机构类型是“自定义机构”，则添加的是“科室人员”
         4)如果机构类型是“学校”，则添加的是“校管理员”或“校责任人”
         */
        String roleCode;
        Integer sortNum = null;
        if (smsOrg.getId() == SmsOrg.ROOT_ID) {
            roleCode = FixedRoleEnum.AREA_ADMIN.getRoleCode();
            sortNum = OrgAccount.SORT_NUM_AREA_ADMIN;
        } else if (smsOrg.getOrgType() == OrgTypeEnum.CUSTOM.getCode()) {
            roleCode = FixedRoleEnum.DEPT_USER.getRoleCode();
            sortNum = OrgAccount.SORT_NUM_DEPT_USER;
        } else if (smsOrg.getOrgType() == OrgTypeEnum.SCHOOL.getCode()) {
            String bizUserType = dtoSaveOrgUserBatch.getBizUserType();
            if (bizUserType == null) throw new BizException("学校用户，必须指定是校管理员还是校责任人！");
            if (bizUserType.equals(FixedRoleEnum.SCHOOL_ADMIN.getRoleCode())) {
                sortNum = OrgAccount.SORT_NUM_SCHOOL_ADMIN;
            }
            if (bizUserType.equals(FixedRoleEnum.SCHOOL_USER.getRoleCode())) {
                sortNum = OrgAccount.SORT_NUM_SCHOOL_USER;
            }
            if (sortNum == null) throw new BizException("学校用户，只能设置为校管理员或者校责任人！");
            roleCode = bizUserType;
        } else {
            throw new BizException("未匹配到用户的角色，请联系运维管理员！");
        }
        /*
         根据业务用户ID，查询出对应的账号集合
         */
        List<BizUserAccount> userAccList = bizUserAccountService.findByUserIdList(bizUserIdList);
        if (userAccList == null || userAccList.size() < bizUserIdList.size())
            throw new BizException("未匹配到用户的账号，请联系运维管理员！");
        /*
         保存前，先删除历史关联记录
         */
        DtoRemoveOrgUserBatch dtoRemoveOrgUserBatch = new DtoRemoveOrgUserBatch();
        dtoRemoveOrgUserBatch.setOrgId(orgId);
        dtoRemoveOrgUserBatch.setBizUserIdList(bizUserIdList);
        dtoRemoveOrgUserBatch.setBizUserType(roleCode);
        this.removeOrgUserBatch(dtoRemoveOrgUserBatch);
        /*
         保存关联账号信息
         */
        List<OrgAccount> saveList = new ArrayList<>();
        Set<Long> accIdSet = new HashSet<>();
        List<Long> accountIds = new ArrayList<>();
        for (BizUserAccount bizUserAccount : userAccList) {
            if (bizUserAccount == null) continue;
            Long accountId = bizUserAccount.getAccountId();
            if (accIdSet.contains(accountId)) continue;
            accIdSet.add(accountId);
            OrgAccount orgAccount = new OrgAccount();
            orgAccount.setOrgId(orgId);
            orgAccount.setAccountId(accountId);
            orgAccount.setRoleCode(roleCode);
            orgAccount.setSortNum(sortNum);
            saveList.add(orgAccount);
            accountIds.add(accountId);
        }
        orgAccountService.saveBatch(saveList);
        /*
         查询固定角色ID
         */
        Role role = roleApi.getRoleByCode(appProperties.getSystemId(), roleCode);
        if (role == null) throw new BizException("未获取到学校角色，注册账号失败，请联系系统管理员");
        /*
         绑定账号角色
         */
        RoleAccountsDTO roleAccountsDTO = new RoleAccountsDTO();
        roleAccountsDTO.setAccountIds(accountIds);
        roleAccountsDTO.setRoleId(role.getId());
        uamAuthApi.saveRoleAccounts(roleAccountsDTO);
    }

    @Tx
    @Override
    public void removeOrgUserBatch(DtoRemoveOrgUserBatch dtoRemoveOrgUserBatch) {
        /*
         入参校验
         */
        if (dtoRemoveOrgUserBatch == null) throw new BizException("传入的对象为空！");
        Long orgId = dtoRemoveOrgUserBatch.getOrgId();
        if (orgId == null) throw new BizException("传入的所属机构ID为空！");
        List<Long> bizUserIdList = dtoRemoveOrgUserBatch.getBizUserIdList();
        if (bizUserIdList == null || bizUserIdList.size() == 0) throw new BizException("传入的用户ID集合为空！");
        /*
         如果是学校类型，必须指定删除哪一个角色的关联
         */
        String bizUserType = dtoRemoveOrgUserBatch.getBizUserType();
        SmsOrg smsOrg = super.getById(orgId);
        if (smsOrg == null) throw new BizException("未查询到机构信息，移除用户失败！");
        if (smsOrg.getOrgType() == OrgTypeEnum.SCHOOL.getCode() && StringUtils.isEmpty(bizUserType))
            throw new BizException("移除的学校账号，必须指定移除哪个身份！");
        /*
         根据业务用户ID，查询出对应的账号集合
         */
        List<BizUserAccount> userAccList = bizUserAccountService.findByUserIdList(bizUserIdList);
        if (userAccList == null || userAccList.size() == 0) return;
        List<Long> accountIdList = userAccList.stream().map(BizUserAccount::getAccountId).collect(Collectors.toList());
        /*
         查找出用户账号对应的角色，并删除
         */
        String roleCode;
        if (smsOrg.getId() == SmsOrg.ROOT_ID) {
            roleCode = FixedRoleEnum.AREA_ADMIN.getRoleCode();
        } else if (smsOrg.getOrgType() == OrgTypeEnum.CUSTOM.getCode()) {
            roleCode = FixedRoleEnum.DEPT_USER.getRoleCode();
        } else if (smsOrg.getOrgType() == OrgTypeEnum.SCHOOL.getCode()) {
            roleCode = bizUserType;
        } else {
            throw new BizException("未匹配到用户的角色，请联系运维管理员！");
        }
        List<Long> accAuthIdList = new ArrayList<>();
        for (Long accountId : accountIdList) {
            List<AccountAuthRolesDTO> accountAllRoleList = uamAuthApi.getAccountAllRoles(accountId, ApplicationInfo.APPLICATION_CODE);
            if (accountAllRoleList == null || accountAllRoleList.size() == 0) continue;
            for (AccountAuthRolesDTO rolesDTO : accountAllRoleList) {
                if (rolesDTO == null) continue;
                if (rolesDTO.getCode() == null) continue;
                if (rolesDTO.getCode().equals(roleCode)) {
                    accAuthIdList.add(rolesDTO.getId());
                }
            }
        }
        if (accAuthIdList.size() > 0) {
            uamAuthApi.deleteAccountAuth(accAuthIdList);
        }
        /*
         移除该机构下，对应的用户账号
         */
        orgAccountService.removeByOrgIdAndAccIdList(orgId, accountIdList, bizUserType);
    }

    @Override
    public IPage<VODeptListAndSchoolStat> findDeptListAndSchoolStat(DtoFindDeptListAndSchoolStat dto) {
        /*
         入参校验
         */
        if (dto == null) throw new BizException("传入的对象为空！");
        Pageable pageable = dto.getPageable();
        if (pageable == null) throw new BizException("传入的分页查询条件为空！");
        /*
         查询所有科室分配学校集合
         */
        List<OrgSchool> orgSchoolList = orgSchoolService.list();
        Map<Long, Integer> orgSchoolNumMap = new HashMap<>();
        if (orgSchoolList != null && orgSchoolList.size() > 0) {
            for (OrgSchool orgSchool : orgSchoolList) {
                if (orgSchool == null) continue;
                Integer schoolNum = orgSchoolNumMap.get(orgSchool.getOrgId());
                if (schoolNum == null) schoolNum = 0;
                schoolNum++;
                orgSchoolNumMap.put(orgSchool.getOrgId(), schoolNum);
            }
        }
        /*
         查询科室结果
         */
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.ORG_TYPE, OrgTypeEnum.CUSTOM.getCode());
        queryWrapper.eq(SmsOrg.LEAF_FLAG, true);
        // 科室名称
        String deptName = dto.getDeptName();
        if (!StringUtils.isEmpty(deptName)) {
            queryWrapper.like(SmsOrg.NAME, deptName);
        }
        // 根据分配情况筛选过滤
        Boolean hasSchoolStatus = dto.getHasSchoolStatus();
        if (hasSchoolStatus != null) {
            if (hasSchoolStatus) { // 已分配
                if (orgSchoolList != null && orgSchoolList.size() > 0) {
                    queryWrapper.in(SmsOrg.ID, orgSchoolList.stream().map(OrgSchool::getOrgId).collect(Collectors.toList()));
                } else {
                    return new Page<>();
                }
            } else { // 未分配
                if (orgSchoolList != null && orgSchoolList.size() > 0) {
                    queryWrapper.notIn(SmsOrg.ID, orgSchoolList.stream().map(OrgSchool::getOrgId).collect(Collectors.toList()));
                }
            }
        }
        IPage<SmsOrg> page = super.getPage(pageable);
        IPage<SmsOrg> resultPage = baseMapper.selectPage(page, queryWrapper);
        if (resultPage.getRecords() == null || resultPage.getRecords().size() == 0) return new Page<>();
        /*
         组装视图结果
         */
        List<VODeptListAndSchoolStat> voList = new ArrayList<>();
        List<SmsOrg> resultList = resultPage.getRecords();
        for (SmsOrg smsOrg : resultList) {
            VODeptListAndSchoolStat vo = new VODeptListAndSchoolStat();
            vo.setDeptId(smsOrg.getId());
            vo.setDeptName(smsOrg.getName());
            Integer schoolNum = orgSchoolNumMap.get(smsOrg.getId());
            if (schoolNum != null && schoolNum > 0) {
                vo.setSchoolNum(schoolNum);
                vo.setSchoolStatus(true);
            } else {
                vo.setSchoolNum(0);
                vo.setSchoolStatus(false);
            }
            voList.add(vo);
        }
        IPage<VODeptListAndSchoolStat> voPage = new Page<>();
        voPage.setRecords(voList);
        voPage.setPages(resultPage.getPages());
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        return voPage;
    }

    @Override
    public List<SmsOrg> findSchoolTree(DtoFindSchoolTree dto) {
        /*
         查询科室结果
         */
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.ORG_TYPE, OrgTypeEnum.SCHOOL.getCode());
        // 学校名称
        if (dto != null) {
            String keyword = dto.getKeyword();
            if (!StringUtils.isEmpty(keyword)) {
                int periodCode = PeriodEnum.getCodeByDesc(keyword);
                queryWrapper.and(wrapper -> wrapper.like(SmsOrg.NAME, keyword).or().eq(SmsOrg.PERIOD, periodCode));
            }
        }
        List<SmsOrg> schoolList = baseMapper.selectList(queryWrapper);
        if (schoolList == null || schoolList.size() == 0) return new ArrayList<>();
        /*
         递归查询父节点并排序
         */
        return this.findParentNodeAndSort(schoolList);
    }

    @Override
    public List<SmsOrg> findSchoolListOfParentNode(Long orgId) {
        /*
         查询所有机构集合
         */
        List<SmsOrg> allList = super.list();
        if (allList == null || allList.size() == 0) return new ArrayList<>();
        /*
         去重，并组装成树状
         */
        Map<Long, Map<Long, SmsOrg>> childMap = getChildMap(allList);
        /*
         递归找出所有子节点，并排序
         */
        List<SmsOrg> childList = new ArrayList<>();
        Map<Long, SmsOrg> firstChildMap = childMap.get(orgId);// 获取第一层子节点集合
        if (firstChildMap != null && firstChildMap.size() > 0) {
            // 将第一层重新排序
            List<SmsOrg> firstChildList = new ArrayList<>(firstChildMap.values());
            this.doSortOrg(firstChildList);
            // 开始递归找子节点，并排序
            List<SmsOrg> rootList = new ArrayList<>(firstChildList);
            this.convertOrgTree(childList, rootList, childMap);
        }
        /*
         剔除不是学校的节点
         */
        List<SmsOrg> resultList = new ArrayList<>();
        if (childList.size() > 0) {
            for (SmsOrg smsOrg : childList) {
                if (smsOrg == null) continue;
                if (smsOrg.getOrgType() == OrgTypeEnum.SCHOOL.getCode()) resultList.add(smsOrg);
            }
        }
        return resultList;
    }

    @Override
    public IPage<VOSchoolListOfDept> findSchoolListOfDept(DtoFindSchoolListOfDept dto) {
        /*
         入参校验
         */
        if (dto == null) throw new BizException("传入的对象为空！");
        Long deptId = dto.getDeptId();
        if (deptId == null) throw new BizException("传入的科室ID为空！");
        Pageable pageable = dto.getPageable();
        if (pageable == null) throw new BizException("传入的分页查询条件为空！");
        /*
         查询当前科室信息
         */
        SmsOrg dept = this.getById(deptId);
        if (dept == null) throw new BizException("未查询到当前科室信息！");
        /*
         查询科室分配学校集合
         */
        List<OrgSchool> orgSchoolList = orgSchoolService.findByDeptId(deptId);
        if (orgSchoolList == null || orgSchoolList.size() == 0) return new Page<>();
        List<Long> schoolIdList = orgSchoolList.stream().map(OrgSchool::getSchoolId).collect(Collectors.toList());
        /*
         根据条件查询学校
         */
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(SmsOrg.ID, schoolIdList);
        String schoolName = dto.getSchoolName();
        if (!StringUtils.isEmpty(schoolName)) {
            queryWrapper.like(SmsOrg.NAME, schoolName);
        }
        Integer period = dto.getPeriod();
        if (period != null) {
            queryWrapper.like(SmsOrg.PERIOD, period);
        }
        IPage<SmsOrg> page = super.getPage(pageable);
        IPage<SmsOrg> orgPage = baseMapper.selectPage(page, queryWrapper);
        if (orgPage.getRecords() == null || orgPage.getRecords().size() == 0) return new Page<>();
        List<SmsOrg> orgList = orgPage.getRecords();
        /*
         根据获取到的学校，获取第一个校管理员信息
         */
        List<Long> orgIdList = orgList.stream().map(SmsOrg::getId).collect(Collectors.toList());
        List<OrgAccount> orgAccountList = orgAccountService.findByOrgIdListAndRoleCode(orgIdList, FixedRoleEnum.SCHOOL_ADMIN.getRoleCode());
        Map<Long, Long> orgAccountMap = new HashMap<>();// key：组织机构ID，value：首个校管理员的账号ID
        Map<Long, BizUser> userMap = new HashMap<>();// key：账号ID
        if (orgAccountList != null && orgAccountList.size() > 0) {
            // 获取机构账号映射
            List<Long> accountIdList = new ArrayList<>();
            for (OrgAccount orgAccount : orgAccountList) {
                if (orgAccount == null) continue;
                if (orgAccountMap.get(orgAccount.getOrgId()) != null) continue;
                orgAccountMap.put(orgAccount.getOrgId(), orgAccount.getAccountId());
                accountIdList.add(orgAccount.getAccountId());
            }
            // 获取账号用户映射
            userMap = bizUserService.findByAccountIdList(accountIdList);
        }
        /*
         组合结果
         */
        List<VOSchoolListOfDept> voList = new ArrayList<>();
        for (SmsOrg smsOrg : orgList) {
            if (smsOrg == null) continue;
            Long bizUserId = orgAccountMap.get(smsOrg.getId());
            BizUser bizUser = null;
            if (bizUserId != null) {
                bizUser = userMap.get(bizUserId);
            }
            VOSchoolListOfDept vo = VOSchoolListOfDept.getSchoolVO(smsOrg, bizUser, dept);
            if (vo != null) voList.add(vo);
        }
        IPage<VOSchoolListOfDept> voPage = new Page<>();
        voPage.setRecords(voList);
        voPage.setPages(orgPage.getPages());
        voPage.setCurrent(orgPage.getCurrent());
        voPage.setSize(orgPage.getSize());
        voPage.setTotal(orgPage.getTotal());
        return voPage;
    }

    @Override
    public void saveDeptAllSchool(DtoSaveDeptAllSchool dto) {
        /*
         入参校验
         */
        if (dto == null) throw new BizException("传入的对象为空！");
        Long deptId = dto.getDeptId();
        if (deptId == null) throw new BizException("传入的科室ID为空！");
        /*
         如果入参为空，认为是删除该科室下所有管辖学校
         */
        List<Long> schoolIdList = dto.getSchoolIdList();
        if (schoolIdList == null || schoolIdList.size() == 0) {
            orgSchoolService.removeByDeptId(deptId);
            return;
        }
        /*
         校验科室和学校的有效性
         */
        SmsOrg dept = super.getById(deptId);
        if (dept == null) throw new BizException("未查询到科室信息，分配失败！");
        if (dept.getOrgType() != OrgTypeEnum.CUSTOM.getCode()) throw new BizException("只有科室才能分配学校！");
        if (!dept.getLeafFlag()) throw new BizException("只有叶子节点的科室才能分配学校！");
        List<SmsOrg> schoolList = super.getByIds(schoolIdList);
        if (schoolList == null || schoolList.size() == 0) throw new BizException("所有的学校信息都未查询到，分配失败！");
        Map<Long, SmsOrg> schoolMap = schoolList.stream().collect(Collectors.toMap(SmsOrg::getId, smsOrg -> smsOrg));// key：机构ID
        int i = 1;
        List<OrgSchool> orgSchoolList = new ArrayList<>();
        for (Long schoolId : schoolIdList) {
            SmsOrg smsOrg = schoolMap.get(schoolId);
            if (smsOrg == null) throw new BizException("未查询到ID为【" + schoolId + "】的学校信息，分配失败！");
            if (smsOrg.getOrgType() != OrgTypeEnum.SCHOOL.getCode())
                throw new BizException("【" + smsOrg.getName() + "】不是学校！");
            OrgSchool orgSchool = new OrgSchool();
            orgSchool.setOrgId(deptId);
            orgSchool.setSchoolId(schoolId);
            orgSchool.setSortNum(i);
            orgSchoolList.add(orgSchool);
            i++;
        }
        /*
         校验通过，先删除科室所有原关联学校记录、以及相关学校的历史关联记录
         */
        orgSchoolService.removeByDeptId(deptId);
        orgSchoolService.removeBySchoolIdList(schoolIdList);
        /*
         保存新记录
         */
        orgSchoolService.saveBatch(orgSchoolList);
    }

    @Override
    public List<SmsOrg> findAllDept() {
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.ORG_TYPE, OrgTypeEnum.CUSTOM.getCode());
        queryWrapper.eq(SmsOrg.LEAF_FLAG, true);
        queryWrapper.orderByAsc(SmsOrg.PID, SmsOrg.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SmsOrg> findAllSchool() {
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.ORG_TYPE, OrgTypeEnum.SCHOOL.getCode());
        queryWrapper.eq(SmsOrg.LEAF_FLAG, true);
        queryWrapper.orderByAsc(SmsOrg.PID, SmsOrg.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<VODeptOfSchool> findDeptOfSchool(List<Long> schoolIdList) {
        if (schoolIdList == null || schoolIdList.size() == 0) return new ArrayList<>();
        /*
         查询关联科室
         */
        List<OrgSchool> orgSchoolList = orgSchoolService.findBySchoolIdList(schoolIdList);
        if (orgSchoolList == null || orgSchoolList.size() == 0) return new ArrayList<>();
        List<Long> deptIdList = orgSchoolList.stream().map(OrgSchool::getOrgId).collect(Collectors.toList());
        /*
         查询科室
         */
        Map<Long, SmsOrg> deptMap = this.getOrgMap(deptIdList);
        List<VODeptOfSchool> resultList = new ArrayList<>();
        for (OrgSchool orgSchool : orgSchoolList) {
            SmsOrg dept = deptMap.get(orgSchool.getOrgId());
            if (dept == null) continue;
            VODeptOfSchool voDeptOfSchool = new VODeptOfSchool();
            voDeptOfSchool.setSchoolId(orgSchool.getSchoolId());
            voDeptOfSchool.setDeptId(dept.getId());
            voDeptOfSchool.setDeptName(dept.getName());
            resultList.add(voDeptOfSchool);
        }
        return resultList;
    }


    /*
     * ========================================以下都是内部接口========================================
     */
    @Override
    public List<SmsOrg> findByAccountId(Long accountId) {
        if (accountId == null) return new ArrayList<>();
        List<OrgAccount> orgAccountList = orgAccountService.findByAccountId(accountId);
        if (orgAccountList == null || orgAccountList.size() == 0) return new ArrayList<>();
        List<Long> smsOrgIdList = orgAccountList.stream().map(OrgAccount::getOrgId).collect(Collectors.toList());
        return super.getByIds(smsOrgIdList);
    }

    @Override
    public List<SmsOrg> findDirectChildOrgList(Long pid) {
        if (pid == null) return new ArrayList<>();
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.PID, pid);
        queryWrapper.orderByAsc(SmsOrg.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SmsOrg> findDirectChildOrgList(List<Long> pidList) {
        if (pidList == null || pidList.size() == 0) return new ArrayList<>();
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(SmsOrg.PID, pidList);
        queryWrapper.orderByAsc(SmsOrg.PID, SmsOrg.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Map<Long, List<SmsOrg>> findByAccountIdList(List<Long> accountIdList) {
        /*
         查询账号关联机构
         */
        List<OrgAccount> orgAccountList = orgAccountService.findByAccountIdList(accountIdList);
        if (orgAccountList == null || orgAccountList.size() == 0) return new HashMap<>();
        List<Long> orgIdList = orgAccountList.stream().map(OrgAccount::getOrgId).collect(Collectors.toList());
        /*
         查询所有相关机构信息
         */
        Map<Long, SmsOrg> smsOrgMap = this.getOrgMap(orgIdList);
        /*
         封装结果
         */
        Map<Long, List<SmsOrg>> resultMap = new HashMap<>();// key：账号ID
        for (OrgAccount orgAccount : orgAccountList) {
            if (orgAccount == null) continue;
            List<SmsOrg> currOrgList = resultMap.get(orgAccount.getAccountId());
            if (currOrgList == null) currOrgList = new ArrayList<>();
            SmsOrg smsOrg = smsOrgMap.get(orgAccount.getOrgId());
            if (smsOrg != null) currOrgList.add(smsOrg);
            resultMap.put(orgAccount.getAccountId(), currOrgList);
        }
        return resultMap;
    }

    @Override
    public SmsOrg getByOrgName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.NAME, name);
        List<SmsOrg> orgList = baseMapper.selectList(queryWrapper);
        return orgList != null && orgList.size() > 0 ? orgList.get(0) : null;
    }

    @Override
    public SmsOrg getByPidAndOrgName(Long pid, String name) {
        if (pid == null) return null;
        if (StringUtils.isEmpty(name)) return null;
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.PID, pid);
        queryWrapper.eq(SmsOrg.NAME, name);
        List<SmsOrg> orgList = baseMapper.selectList(queryWrapper);
        return orgList != null && orgList.size() > 0 ? orgList.get(0) : null;
    }

    @Override
    public SmsOrg saveOrgForBigDataPlat(SmsOrg smsOrg, int syncFlag) {
        // 其它字段
        Long id = smsOrg.getId();
        Long pid = smsOrg.getPid();

        /*
         修改其父节点的叶子标识
         */
        SmsOrg parentOrg = super.getById(pid);
        if (pid != SmsOrg.ROOT_PID && parentOrg == null) {
            throw new BizException("未查询到机构的父节点信息，更新失败！");
        } else if (parentOrg != null) {
            parentOrg.setLeafFlag(false);
            super.updateById(parentOrg);
        }
        /*
         设置路径
         */
        if (pid == SmsOrg.ROOT_PID) {// 根节点
            smsOrg.setParentPathFull("");
        } else if (pid == SmsOrg.ROOT_ID) {// 根节点下第一层
            smsOrg.setParentPathFull(parentOrg.getName());
        } else {// 根节点下其它层
            smsOrg.setParentPathFull(parentOrg.getParentPathFull() + "/" + parentOrg.getName());
        }
        /*
         若是新增，排序号需根据当前父节点下的直接子节点数+1
         */
        if (id == null) {// 新增
            List<SmsOrg> childOrgList = this.findDirectChildOrgList(pid);
            smsOrg.setSortNum(childOrgList.size() + 1);
            smsOrg.setLeafFlag(true);
            super.save(smsOrg);
        } else {// 修改
            if (syncFlag == SyncBigDataPlatEnum.CLOSE.getCode() || syncFlag == SyncBigDataPlatEnum.OPEN_PASS.getCode()) {
                return smsOrg;// 不修改，直接跳过
            }
            if (syncFlag == SyncBigDataPlatEnum.OPEN_OVERWRITE.getCode()) {
                super.updateById(smsOrg);
            }
            if (syncFlag == SyncBigDataPlatEnum.OPEN_HOLD.getCode()) {
                String name = smsOrg.getName();
                int maxIndex = this.getSameNameMaxIndex(name);
                maxIndex++;
                smsOrg.setName(name + SmsOrg.SAME_NAME_SPLIT + maxIndex);
                super.updateById(smsOrg);
            }
        }
        return smsOrg;
    }

    @Override
    public List<SmsOrg> findAllCustomOrg() {
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.ORG_TYPE, OrgTypeEnum.CUSTOM.getCode());
        queryWrapper.orderByAsc(SmsOrg.PID, SmsOrg.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<SmsOrg> findByCondition(DtoFindByCondition dto) {
        /*
         入参校验
         */
        if (dto == null) throw new BizException("传入的对象为空！");
        Pageable pageable = dto.getPageable();
        if (pageable == null) throw new BizException("传入的分页查询条件为空！");
        /*
         根据条件查询学校
         */
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        List<Long> orgIdList = dto.getOrgIdList();
        if (orgIdList != null && orgIdList.size() > 0) {
            queryWrapper.in(SmsOrg.ID, orgIdList);
        }
        String orgName = dto.getOrgName();
        if (!StringUtils.isEmpty(orgName)) {
            queryWrapper.like(SmsOrg.NAME, orgName);
        }
        Integer period = dto.getPeriod();
        if (period != null) {
            queryWrapper.like(SmsOrg.PERIOD, period);
        }
        IPage<SmsOrg> page = super.getPage(dto.getPageable());
        return baseMapper.selectPage(page, queryWrapper);
    }


    /*
     * ========================================以下都是私有方法========================================
     */
    private boolean doCheckNameOnly(Long orgId, String orgName) {
        // 传入的名称不能为空，否则直接判定为不重复（因为没有）
        if (StringUtils.isEmpty(orgName)) return true;
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        if (orgId != null) {
            queryWrapper.ne(SmsOrg.ID, orgId);
        }
        queryWrapper.eq(SmsOrg.NAME, orgName);
        List<SmsOrg> noteList = baseMapper.selectList(queryWrapper);
        return noteList == null || noteList.size() <= 0;// true：是唯一的；false:已被占用
    }

    private List<SmsOrg> doCheckNameOnlyBatch(List<String> nameList) {
        if (nameList == null || nameList.size() == 0) return new ArrayList<>();
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(SmsOrg.NAME, nameList);
        return baseMapper.selectList(queryWrapper);
    }

    private void findOrgParent(List<SmsOrg> pOrgList, List<SmsOrg> currList, Map<Long, SmsOrg> allMap) {
        List<SmsOrg> newList = new ArrayList<>();
        for (SmsOrg smsOrg : currList) {
            if (smsOrg == null) continue;
            SmsOrg pOrg = allMap.get(smsOrg.getPid());
            if (pOrg != null) newList.add(pOrg);
        }
        if (newList.size() > 0) {
            pOrgList.addAll(newList);
            this.findOrgParent(pOrgList, newList, allMap);
        }
    }

    private void convertOrgTree(List<SmsOrg> finalResultList, List<SmsOrg> currList, Map<Long, Map<Long, SmsOrg>> resultMap) {
        if (currList == null || currList.size() == 0) return;
        /*
         冒泡排序
         */
        doSortOrg(currList);
        /*
         当前排序后结果，加入总集合
         */
        finalResultList.addAll(currList);
        /*
         对子节点继续排序，并加入总集合
         */
        for (SmsOrg currOrg : currList) {
            if (currOrg == null) continue;
            Map<Long, SmsOrg> childMap = resultMap.get(currOrg.getId());// 获取当前节点的直接子节点集合
            if (childMap != null && childMap.size() > 0) {
                List<SmsOrg> childList = new ArrayList<>(childMap.values());
                this.convertOrgTree(finalResultList, childList, resultMap);
            }
        }
    }

    private void doSortOrg(List<SmsOrg> currList) {
        for (int i = 0; i < currList.size() - 1; i++) {
            for (int j = 1; j < currList.size() - i; j++) {
                SmsOrg a;
                if (currList.get(j - 1).getSortNum() > currList.get(j).getSortNum()) {
                    a = currList.get(j - 1);
                    currList.set((j - 1), currList.get(j));
                    currList.set(j, a);
                }
            }
        }
    }

    private Map<Long, Map<Long, SmsOrg>> getChildMap(List<SmsOrg> currList) {
        Map<Long, Map<Long, SmsOrg>> childMap = new HashMap<>();// 一层key：父节点ID；二层key：组织机构ID，用于去重
        for (SmsOrg smsOrg : currList) {
            if (smsOrg == null) continue;
            Long id = smsOrg.getId();
            Long pid = smsOrg.getPid();
            Map<Long, SmsOrg> currMap = childMap.get(pid);
            if (currMap == null) {
                currMap = new HashMap<>();
            }
            currMap.put(id, smsOrg);// 加入集合；如果机构ID相同，则覆盖
            childMap.put(pid, currMap);
        }
        return childMap;
    }

    private List<SmsOrg> findParentNodeAndSort(List<SmsOrg> searchList) {
        if (searchList == null || searchList.size() == 0) return new ArrayList<>();
        /*
         查询出所有机构集合
         */
        List<SmsOrg> allList = super.list();
        Map<Long, SmsOrg> allMap = allList.stream().collect(Collectors.toMap(SmsOrg::getId, smsOrg -> smsOrg));// key：机构ID
        /*
         递归找出所有父节点
         */
        List<SmsOrg> pOrgList = new ArrayList<>(searchList);
        this.findOrgParent(pOrgList, searchList, allMap);
        /*
         去重，并组装成树状
         */
        Map<Long, Map<Long, SmsOrg>> resultMap = getChildMap(pOrgList);
        /*
         从第一层开始取节点，并重新排序
         */
        Map<Long, SmsOrg> rootMap = resultMap.get(SmsOrg.ROOT_PID);
        List<SmsOrg> finalResultList = new ArrayList<>();
        if (rootMap != null && rootMap.size() > 0) {
            List<SmsOrg> rootList = new ArrayList<>(rootMap.values());
            this.convertOrgTree(finalResultList, rootList, resultMap);
        }
        return finalResultList;
    }

    private void updateParentOrgLeafFlag(SmsOrg currOrg) {
        if (currOrg == null) return;
        SmsOrg parentOrg = super.getById(currOrg.getPid());
        if (parentOrg == null) throw new BizException("未查询到机构信息，删除失败！");
        List<SmsOrg> brotherList = this.findDirectChildOrgList(parentOrg.getId());
        if (brotherList == null || brotherList.size() == 0) {
            parentOrg.setLeafFlag(true);
            super.updateById(parentOrg);
        }
    }

    private void updateAfterOrgSortNum(long pid, int currSortNum) {
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SmsOrg.PID, pid);
        queryWrapper.gt(SmsOrg.SORT_NUM, currSortNum);
        List<SmsOrg> afterList = baseMapper.selectList(queryWrapper);
        if (afterList != null && afterList.size() > 0) {
            List<SmsOrg> updateList = new ArrayList<>();
            for (SmsOrg smsOrg : afterList) {
                if (smsOrg == null) continue;
                smsOrg.setSortNum(smsOrg.getSortNum() - 1);
                updateList.add(smsOrg);
            }
            if (updateList.size() > 0) {
                super.updateBatchById(updateList);
            }
        }
    }

    private Map<Long, SmsOrg> getOrgMap(List<Long> orgIdList) {
        List<SmsOrg> smsOrgList = super.getByIds(orgIdList);
        Map<Long, SmsOrg> smsOrgMap = new HashMap<>();
        if (smsOrgList != null && smsOrgList.size() > 0) {
            smsOrgMap = smsOrgList.stream().collect(Collectors.toMap(SmsOrg::getId, smsOrg -> smsOrg));// key：机构ID
        }
        return smsOrgMap;
    }

    private int getSameNameMaxIndex(String name) {
        int maxIndex = 0;
        QueryWrapper<SmsOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(SmsOrg.NAME, name + SmsOrg.SAME_NAME_SPLIT);
        queryWrapper.orderByDesc(SmsOrg.NAME);
        List<SmsOrg> smsOrgList = baseMapper.selectList(queryWrapper);
        if (smsOrgList != null && smsOrgList.size() > 0) {
            SmsOrg smsOrg = smsOrgList.get(0);
            String[] nameSplit = smsOrg.getName().split(SmsOrg.SAME_NAME_SPLIT);
            return Integer.parseInt(nameSplit[1]);
        }
        return maxIndex;
    }

    private List<SmsOrg> doImportSmsOrg(List<String> descMsgList, Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        List<SmsOrg> smsOrgList = new ArrayList<>();
        if (sheet != null) {
            Set<String> nameSet = new HashSet<>();
            /*
             查询当前已有的所有自定义节点，用于匹配导入数据的父节点名称是否存在
             */
            List<SmsOrg> customList = this.findAllCustomOrg();
            Map<String, SmsOrg> customMap = new HashMap<>();
            if (customList != null && customList.size() > 0) {
                customMap = customList.stream().collect(Collectors.toMap(SmsOrg::getName, smsOrg -> smsOrg));// key：机构名称
            }

            /*
             1. 校验【机构基本信息采集表】sheet文本内容
             */
            for (int r = 3; r <= sheet.getLastRowNum(); r++) {
                Row currRow = sheet.getRow(r);
                if (currRow == null) break;
                // 如果名称为空，则认为读取结束！
                Cell currCell = currRow.getCell(1);
                String name = currCell.getStringCellValue();// *学校名称
                if (StringUtils.isEmpty(name)) {
                    break;
                }

                SmsOrg smsOrg = new SmsOrg();

                // *机构名称
                if (StringUtils.isEmpty(name)) {
                    descMsgList.add("第" + (r + 1) + "行的【机构名称】为空");
                } else if (nameSet.contains(name)) {
                    descMsgList.add("第" + (r + 1) + "行的【机构名称】在表格中出现过两次以上");
                }
                smsOrg.setName(name);
                nameSet.add(name);

                // *上级机构名称
                currCell = currRow.getCell(2);
                String parentPathFull = currCell.getStringCellValue();
                if (StringUtils.isEmpty(parentPathFull)) {
                    descMsgList.add("第" + (r + 1) + "行的【上级机构名称】为空");
                } else {
                    String[] splitStrs = parentPathFull.split("/");
                    boolean checkResult = true;
                    String parentName = splitStrs[splitStrs.length - 1];
                    if (StringUtils.isEmpty(parentName)) {
                        descMsgList.add("第" + (r + 1) + "行的【上级机构名称】用/分隔后，最后一层的内容为空");
                        checkResult = false;
                    }
                    SmsOrg parentOrg = customMap.get(parentName);
                    if (parentOrg == null) {
                        descMsgList.add("第" + (r + 1) + "行的【上级机构名称】未匹配到当前系统中已有的自定义类型机构");
                        checkResult = false;
                    }
                    if (checkResult) {
                        smsOrg.setPid(parentOrg.getId());
                    }
                }

                // *机构类型
                currCell = currRow.getCell(3);
                String orgTypeStr = currCell.getStringCellValue();
                if (StringUtils.isEmpty(orgTypeStr)) {
                    descMsgList.add("第" + (r + 1) + "行的【机构类型】为空");
                }
                Integer orgType = OrgTypeEnum.getCodeByDesc(orgTypeStr);
                if (orgType == null) {
                    descMsgList.add("第" + (r + 1) + "行的【机构类型】不是有效的数据");
                }
                smsOrg.setOrgType(orgType);

                /*
                 学段
                 */
                if (orgType == OrgTypeEnum.SCHOOL.getCode()) {
                    Set<Integer> periodSet = new TreeSet<>();
                    // *学段1
                    this.getPeriodSet(descMsgList, r, currRow, periodSet, 4, 1);
                    // *学段2
                    this.getPeriodSet(descMsgList, r, currRow, periodSet, 5, 2);
                    // *学段3
                    this.getPeriodSet(descMsgList, r, currRow, periodSet, 6, 3);
                    // *学段4
                    this.getPeriodSet(descMsgList, r, currRow, periodSet, 7, 4);
                    // *学段5
                    this.getPeriodSet(descMsgList, r, currRow, periodSet, 8, 5);
                    // *学段6
                    this.getPeriodSet(descMsgList, r, currRow, periodSet, 9, 6);
                    if (periodSet.size() > 0) {
                        Iterator<Integer> it = periodSet.iterator();
                        int i = 0;
                        StringBuilder sb = new StringBuilder();
                        while (it.hasNext()) {
                            Integer period = it.next();
                            sb.append(period);
                            i++;
                            if (i < periodSet.size()) {
                                sb.append(",");
                            }
                        }
                        smsOrg.setPeriod(sb.toString());
                    }
                }
                smsOrgList.add(smsOrg);
            }

            /*
             2. 唯一性校验
             */
            if (smsOrgList.size() > 0) {
                List<String> nameList = new ArrayList<>();
                for (SmsOrg smsOrg : smsOrgList) {
                    nameList.add(smsOrg.getName());
                }
                // 机构名称
                List<SmsOrg> checkList = this.doCheckNameOnlyBatch(nameList);
                if (checkList != null && checkList.size() > 0) {
                    StringBuilder sb = new StringBuilder("以下【机构名称】已存在，导入失败：");
                    for (SmsOrg check : checkList) {
                        sb.append("【").append(check.getName()).append("】");
                    }
                    descMsgList.add(sb.toString());
                }
            }
        } else {
            String failMsg = "上传的excel文件，sheet为空，导入数据失败！";
            descMsgList.add(failMsg);
        }
        return smsOrgList;
    }

    private void getPeriodSet(List<String> descMsgList, int r, Row currRow, Set<Integer> periodSet, int cellNum, int periodNum) {
        Cell currCell;
        currCell = currRow.getCell(cellNum);
        if (currCell == null) return;
        String periodStr1 = currCell.getStringCellValue();
        if (!StringUtils.isEmpty(periodStr1)) {
            int period = PeriodEnum.getCodeByDesc(periodStr1);
            if (period == -1) {
                descMsgList.add("第" + (r + 1) + "行的【学段" + periodNum + "】不是有效的数据");
            } else {
                periodSet.add(period);
            }
        }
    }
}
