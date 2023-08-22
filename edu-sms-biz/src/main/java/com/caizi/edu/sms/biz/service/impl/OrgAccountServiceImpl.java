package com.caizi.edu.sms.biz.service.impl;

import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.caizi.edu.sms.biz.mapper.OrgAccountMapper;
import com.caizi.edu.sms.biz.service.OrgAccountService;
import com.caizi.edu.sms.entity.OrgAccount;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 机构账号表 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Service
public class OrgAccountServiceImpl extends AntnestBaseServiceImpl<OrgAccountMapper, OrgAccount> implements OrgAccountService {

    @Override
    public List<OrgAccount> findByAccountId(Long accountId) {
        if (accountId == null) return new ArrayList<>();
        QueryWrapper<OrgAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(OrgAccount.ACCOUNT_ID, accountId);
        queryWrapper.orderByAsc(OrgAccount.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<OrgAccount> findByAccountIdList(List<Long> accountIdList) {
        if (accountIdList == null || accountIdList.size() == 0) return new ArrayList<>();
        QueryWrapper<OrgAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(OrgAccount.ACCOUNT_ID, accountIdList);
        queryWrapper.orderByAsc(OrgAccount.ACCOUNT_ID, OrgAccount.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<OrgAccount> findByOrgIdAndRoleCode(Long orgId, String roleCode) {
        QueryWrapper<OrgAccount> queryWrapper = new QueryWrapper<>();
        if (orgId != null) {
            queryWrapper.eq(OrgAccount.ORG_ID, orgId);
        }
        if (roleCode != null) {
            queryWrapper.eq(OrgAccount.ROLE_CODE, roleCode);
        }
        queryWrapper.orderByAsc(OrgAccount.ACCOUNT_ID, OrgAccount.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<OrgAccount> findByOrgIdListAndRoleCode(List<Long> orgIdList, String roleCode) {
        if (orgIdList == null || orgIdList.size() == 0) return new ArrayList<>();
        QueryWrapper<OrgAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(OrgAccount.ORG_ID, orgIdList);
        if (roleCode != null) {
            queryWrapper.eq(OrgAccount.ROLE_CODE, roleCode);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void removeByOrgIdAndAccIdList(Long orgId, List<Long> accountIdList, String roleCode) {
        if (orgId == null) return;
        if (accountIdList == null || accountIdList.size() == 0) return;
        UpdateWrapper<OrgAccount> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(OrgAccount.ORG_ID, orgId);
        updateWrapper.in(OrgAccount.ACCOUNT_ID, accountIdList);
        if (!StringUtils.isEmpty(roleCode)) {
            updateWrapper.eq(OrgAccount.ROLE_CODE, roleCode);
        }
        super.remove(updateWrapper);
    }
}
