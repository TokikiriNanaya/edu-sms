package com.caizi.edu.sms.hystrix;

import com.antnest.mscore.base.controller.impl.AntnestBaseFallback;
import com.caizi.edu.sms.api.KpiTempMainApi;
import com.caizi.edu.sms.dto.org.VOTempItemTreeAndStat;
import com.caizi.edu.sms.entity.KpiTempMain;

import java.util.List;

/**
 * @author 张培文
 * @date 2023/5/11 9:20
 */
public class KpiTempMainFallBack extends AntnestBaseFallback<KpiTempMain> implements KpiTempMainApi {

    @Override
    public List<KpiTempMain> findTempList() {
        return null;
    }

    @Override
    public VOTempItemTreeAndStat findTempItemTreeAndStat(Long tempId, Integer levelNum, String name) {
        return null;
    }

    @Override
    public KpiTempMain saveTemp(Long id, String name) {
        return null;
    }

    @Override
    public void removeTemp(Long tempId) {
    }

    @Override
    public void saveTempItemBatch(Long tempId, List<Long> baseItemIdList) {

    }

    @Override
    public void removeTempItemBatch(Long tempId, List<Long> ids) {

    }

    @Override
    public void updateTempItemSortNum(Long tempItemId, Integer operFlag) {

    }


}
