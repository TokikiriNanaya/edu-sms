package com.caizi.edu.sms.biz.service;

import com.antnest.mscore.base.service.AntnestBaseService;
import com.caizi.edu.sms.entity.OrgAccount;

import java.util.List;

/**
 * <p>
 * 机构账号表 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
public interface OrgAccountService extends AntnestBaseService<OrgAccount> {

    List<OrgAccount> findByAccountId(Long accountId);

    List<OrgAccount> findByAccountIdList(List<Long> accountIdList);

    List<OrgAccount> findByOrgIdAndRoleCode(Long orgId, String roleCode);

    List<OrgAccount> findByOrgIdListAndRoleCode(List<Long> orgIdList, String roleCode);

    void removeByOrgIdAndAccIdList(Long orgId, List<Long> accountIdList, String roleCode);
}
