package com.caizi.edu.sms.biz.service;

import com.antnest.mscore.base.service.AntnestBaseService;
import com.caizi.edu.sms.entity.KpiTempItem;

import java.util.List;

/**
 * 模板指标项 服务类
 *
 * @author 张培文
 * @date 2023/5/11 15:37
 */
public interface KpiTempItemService extends AntnestBaseService<KpiTempItem> {

    List<KpiTempItem> findTempItemByTempId(Long tempId);

    List<KpiTempItem> findTempItemByTempIdAndBaseItemId(Long tempId,Long baseItemId);

    void saveTempItem(KpiTempItem kpiTempItem);

    void resetSortNumByTempId(Long id, Long tempId, Long pid);

    void removeTempItemBatchByIds(Long tempId,List<Long> tempItemIds);

    void updateTempItemSortNum(Long tempItemId, Integer operFlag);
}
