package com.caizi.edu.sms.hystrix;

import com.antnest.mscore.base.controller.impl.AntnestBaseFallback;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.api.KpiBaseItemApi;
import com.caizi.edu.sms.dto.org.DtoFindThirdKpiBaseItem;
import com.caizi.edu.sms.dto.org.DtoSaveKpiBaseItem;
import com.caizi.edu.sms.entity.KpiBaseItem;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 张培文
 * @date 2023/5/9 11:36
 */
public class KpiBaseItemFallBack extends AntnestBaseFallback<KpiBaseItem> implements KpiBaseItemApi {
    @Override
    public List<KpiBaseItem> findBaseItemTree(String name) {
        return null;
    }

    @Override
    public IPage<KpiBaseItem> findThirdBaseItemList(DtoFindThirdKpiBaseItem dtoFindThirdKpiBaseItem) {
        return null;
    }

    @Override
    public KpiBaseItem saveBaseItem(@RequestBody DtoSaveKpiBaseItem dtoSaveKpiBaseItem) {
        return null;
    }

    @Override
    public void removeBaseItem(Long itemId) {

    }

    @Override
    public KpiBaseItem getBaseItemById(Long id) {
        return null;
    }
}
