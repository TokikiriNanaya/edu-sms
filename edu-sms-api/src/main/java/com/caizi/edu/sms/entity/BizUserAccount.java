package com.caizi.edu.sms.entity;

import com.antnest.mscore.base.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户账号表
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_sms_biz_user_account")
@ApiModel(value = "BizUserAccount对象", description = "用户账号表")
public class BizUserAccount extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务用户id")
    private Long bizUserId;

    @ApiModelProperty(value = "账号id")
    private Long accountId;


    public static final String BIZ_USER_ID = "biz_user_id";

    public static final String ACCOUNT_ID = "account_id";


    public static Map<Long, Long> getUserIdByAccId(List<BizUserAccount> inList) {
        Map<Long, Long> outMap = new HashMap<>();// key：检查任务ID，value：参检学校集合
        if (inList == null || inList.size() == 0) return outMap;
        for (BizUserAccount bizUserAccount : inList) {
            if (bizUserAccount == null) continue;
            outMap.put(bizUserAccount.getAccountId(), bizUserAccount.getBizUserId());
        }
        return outMap;
    }
}
