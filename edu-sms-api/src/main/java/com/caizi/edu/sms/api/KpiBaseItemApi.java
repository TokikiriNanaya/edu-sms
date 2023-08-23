package com.caizi.edu.sms.api;

import com.antnest.mscore.base.controller.AntnestSimpleBaseApi;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.org.DtoFindThirdKpiBaseItem;
import com.caizi.edu.sms.dto.org.DtoSaveKpiBaseItem;
import com.caizi.edu.sms.entity.KpiBaseItem;
import com.caizi.edu.sms.hystrix.KpiBaseItemFallBack;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 基础指标项 接口信息
 *
 * @author 张培文
 * @date 2023/5/9 11:31
 */
@Api(tags = {"基础指标项"})
@FeignClient(value = ApplicationInfo.ANTNEST_SERVICE_NAME, fallback = KpiBaseItemFallBack.class, path = KpiBaseItemApi._PATH)
public interface KpiBaseItemApi extends AntnestSimpleBaseApi<KpiBaseItem> {
    String _PATH = ApplicationInfo.ANTNEST_BASE_PATH + "/sms_kpi_base_item";

    @ApiOperation(value = "查询指标树")
    @RequestMapping(value = "/findBaseItemTree", method = RequestMethod.POST)
    List<KpiBaseItem> findBaseItemTree(@RequestParam(value = "name", required = false) String name);

    @ApiOperation(value = "查询三级指标项列表")
    @RequestMapping(value = "/findThirdBaseItemList", method = RequestMethod.POST)
    IPage<KpiBaseItem> findThirdBaseItemList(@RequestBody DtoFindThirdKpiBaseItem dtoFindThirdKpiBaseItem);

    @ApiOperation(value = "保存指标项")
    @RequestMapping(value = "/saveBaseItem", method = RequestMethod.POST)
    KpiBaseItem saveBaseItem(@RequestBody DtoSaveKpiBaseItem dtoSaveKpiBaseItem);

    @ApiOperation(value = "删除指标项")
    @RequestMapping(value = "/removeBaseItem", method = RequestMethod.POST)
    void removeBaseItem(@RequestParam(value = "itemId") Long itemId);

    @ApiOperation(value = "查询指标项")
    @RequestMapping(value = "/getBaseItemById", method = RequestMethod.POST)
    KpiBaseItem getBaseItemById(@RequestParam(value = "id") Long id);

}
