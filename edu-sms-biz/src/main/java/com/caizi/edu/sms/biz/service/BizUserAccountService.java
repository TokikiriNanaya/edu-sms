package com.caizi.edu.sms.biz.service;

import com.antnest.mscore.base.service.AntnestBaseService;
import com.caizi.edu.sms.entity.BizUserAccount;

import java.util.List;

/**
 * <p>
 * 用户账号表 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
public interface BizUserAccountService extends AntnestBaseService<BizUserAccount> {

    void removeByBizUserId(Long bizUserId);

    void removeByAccountId(Long accountId);

    BizUserAccount getByAccountId(Long accountId);

    List<BizUserAccount> findByAccountIdList(List<Long> accountIdList);

    List<BizUserAccount> findByUserId(Long userId);

    List<BizUserAccount> findByUserIdList(List<Long> userIdList);
}
