package com.caizi.edu.sms.biz.controller;

import com.antnest.mscore.base.controller.impl.AntnestSimpleBaseController;
import com.caizi.edu.sms.api.KpiTempMainApi;
import com.caizi.edu.sms.biz.service.KpiTempMainService;
import com.caizi.edu.sms.dto.org.VOTempItemTreeAndStat;
import com.caizi.edu.sms.entity.KpiTempMain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 指标模板 前端控制器
 *
 * @author 张培文
 * @date 2023/5/11 9:23
 */
@CrossOrigin
@RestController
@RequestMapping(KpiTempMainApi._PATH)
public class KpiTempMainController extends AntnestSimpleBaseController<KpiTempMainService, KpiTempMain> implements KpiTempMainApi {


    @Override
    public List<KpiTempMain> findTempList() {
        return baseService.findTempList();
    }

    @Override
    public VOTempItemTreeAndStat findTempItemTreeAndStat(Long tempId, Integer levelNum, String name) {
        return baseService.findTempItemTreeAndStat(tempId, levelNum, name);
    }

    @Override
    public KpiTempMain saveTemp(Long id, String name) {
        return baseService.saveTemp(id, name);
    }

    @Override
    public void removeTemp(Long tempId) {
        baseService.removeTemp(tempId);
    }

    @Override
    public void saveTempItemBatch(Long tempId, List<Long> baseItemIdList) {
        baseService.saveTempItemBatch(tempId, baseItemIdList);
    }

    @Override
    public void removeTempItemBatch(Long tempId,List<Long> ids) {
        baseService.removeTempItemBatch(tempId,ids);
    }

    @Override
    public void updateTempItemSortNum(Long tempItemId, Integer operFlag) {
        baseService.updateTempItemSortNum(tempItemId,operFlag);
    }
}
