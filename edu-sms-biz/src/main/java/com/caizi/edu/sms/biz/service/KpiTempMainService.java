package com.caizi.edu.sms.biz.service;

import com.antnest.mscore.base.service.AntnestBaseService;
import com.caizi.edu.sms.dto.org.VOTempItemTreeAndStat;
import com.caizi.edu.sms.entity.KpiTempMain;

import java.util.List;

/**
 * 指标模板 服务类
 *
 * @author 张培文
 * @date 2023/5/11 9:24
 */
public interface KpiTempMainService extends AntnestBaseService<KpiTempMain> {
    List<KpiTempMain> findTempList();

    VOTempItemTreeAndStat findTempItemTreeAndStat(Long tempId, Integer levelNum, String name);

    KpiTempMain saveTemp(Long id, String name);

    void removeTemp(Long tempId);

    void saveTempItemBatch(Long tempId, List<Long> baseItemIdList);

    void removeTempItemBatch(Long tempId, List<Long> tempItemIds);

    void updateTempItemSortNum(Long tempItemId, Integer operFlag);

}
