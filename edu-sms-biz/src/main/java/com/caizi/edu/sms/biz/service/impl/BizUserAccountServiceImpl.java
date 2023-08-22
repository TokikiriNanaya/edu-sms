package com.caizi.edu.sms.biz.service.impl;

import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.caizi.edu.sms.biz.mapper.BizUserAccountMapper;
import com.caizi.edu.sms.biz.service.BizUserAccountService;
import com.caizi.edu.sms.entity.BizUserAccount;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户账号表 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Service
public class BizUserAccountServiceImpl extends AntnestBaseServiceImpl<BizUserAccountMapper, BizUserAccount> implements BizUserAccountService {

    @Override
    public void removeByBizUserId(Long bizUserId) {
        if (bizUserId == null) return;
        UpdateWrapper<BizUserAccount> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(BizUserAccount.BIZ_USER_ID, bizUserId);
        super.remove(updateWrapper);
    }

    @Override
    public void removeByAccountId(Long accountId) {
        if (accountId == null) return;
        UpdateWrapper<BizUserAccount> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(BizUserAccount.ACCOUNT_ID, accountId);
        super.remove(updateWrapper);
    }

    @Override
    public BizUserAccount getByAccountId(Long accountId) {
        if (accountId == null) return null;
        QueryWrapper<BizUserAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BizUserAccount.ACCOUNT_ID, accountId);
        List<BizUserAccount> list = baseMapper.selectList(queryWrapper);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<BizUserAccount> findByAccountIdList(List<Long> accountIdList) {
        if (accountIdList == null || accountIdList.size() == 0) return new ArrayList<>();
        QueryWrapper<BizUserAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(BizUserAccount.ACCOUNT_ID, accountIdList);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<BizUserAccount> findByUserId(Long userId) {
        if (userId == null) return new ArrayList<>();
        QueryWrapper<BizUserAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BizUserAccount.BIZ_USER_ID, userId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<BizUserAccount> findByUserIdList(List<Long> userIdList) {
        if (userIdList == null || userIdList.size() == 0) return new ArrayList<>();
        QueryWrapper<BizUserAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(BizUserAccount.BIZ_USER_ID, userIdList);
        return baseMapper.selectList(queryWrapper);
    }
}
