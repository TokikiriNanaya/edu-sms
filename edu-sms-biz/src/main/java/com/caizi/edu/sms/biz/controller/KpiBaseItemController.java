package com.caizi.edu.sms.biz.controller;

import com.antnest.mscore.base.controller.impl.AntnestSimpleBaseController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.api.KpiBaseItemApi;
import com.caizi.edu.sms.biz.service.KpiBaseItemService;
import com.caizi.edu.sms.dto.org.DtoFindThirdKpiBaseItem;
import com.caizi.edu.sms.dto.org.DtoSaveKpiBaseItem;
import com.caizi.edu.sms.entity.KpiBaseItem;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 基础指标项 前端控制器
 *
 * @author 张培文
 * @date 2023/5/9 11:39
 */
@CrossOrigin
@RestController
@RequestMapping(KpiBaseItemApi._PATH)
public class KpiBaseItemController extends AntnestSimpleBaseController<KpiBaseItemService, KpiBaseItem> implements KpiBaseItemApi {

    @Override
    public List<KpiBaseItem> findBaseItemTree(String name) {
        return baseService.findBaseItemTree(name);
    }

    @Override
    public IPage<KpiBaseItem> findThirdBaseItemList(@RequestBody DtoFindThirdKpiBaseItem dtoFindThirdKpiBaseItem) {
        return baseService.findThirdBaseItemList(dtoFindThirdKpiBaseItem);
    }

    @Override
    public KpiBaseItem saveBaseItem(@RequestBody DtoSaveKpiBaseItem dtoSaveKpiBaseItem) {
        return baseService.saveBaseItem(dtoSaveKpiBaseItem);
    }

    @Override
    public void removeBaseItem(Long itemId) {
        baseService.removeBaseItem(itemId);
    }
}
