package com.caizi.edu.sms.biz.service;

import com.antnest.mscore.base.service.AntnestBaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.dto.org.DtoFindThirdKpiBaseItem;
import com.caizi.edu.sms.dto.org.DtoSaveKpiBaseItem;
import com.caizi.edu.sms.entity.KpiBaseItem;

import java.util.List;

/**
 * 基础指标项 服务类
 * @author 张培文
 * @date 2023/5/9 11:41
 */
public interface KpiBaseItemService extends AntnestBaseService<KpiBaseItem> {
    List<KpiBaseItem> findBaseItemTree(String name);

    KpiBaseItem saveBaseItem(DtoSaveKpiBaseItem dtoSaveKpiBaseItem);

    void removeBaseItem(Long itemId);

    IPage<KpiBaseItem> findThirdBaseItemList(DtoFindThirdKpiBaseItem dtoFindThirdKpiBaseItem);

    List<KpiBaseItem> selectBatchIds(List<Long> ids);

    void savaTempItemByThirdBaseItemId(Long id, Long tempId);



}
