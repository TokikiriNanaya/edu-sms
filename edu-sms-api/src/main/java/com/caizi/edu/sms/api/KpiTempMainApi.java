package com.caizi.edu.sms.api;

import com.antnest.mscore.base.controller.AntnestSimpleBaseApi;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.dto.org.VOTempItemTreeAndStat;
import com.caizi.edu.sms.entity.KpiTempMain;
import com.caizi.edu.sms.hystrix.KpiTempMainFallBack;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 指标模板 接口信息
 *
 * @author 张培文
 * @date 2023/5/9 11:31
 */
@Api(tags = {"基础指标项"})
@FeignClient(value = ApplicationInfo.ANTNEST_SERVICE_NAME, fallback = KpiTempMainFallBack.class, path = KpiTempMainApi._PATH)
public interface KpiTempMainApi extends AntnestSimpleBaseApi<KpiTempMain> {
    String _PATH = ApplicationInfo.ANTNEST_BASE_PATH + "/sms_kpi_temp_main";

    @ApiOperation(value = "查询指标模板列表")
    @RequestMapping(value = "/findTempList", method = RequestMethod.POST)
    List<KpiTempMain> findTempList();

    @ApiOperation(value = "查询模板指标项树及统计信息")
    @RequestMapping(value = "/findTempItemTreeAndStat", method = RequestMethod.POST)
    VOTempItemTreeAndStat findTempItemTreeAndStat(@RequestParam(value = "tempId") Long tempId,
                                                  @RequestParam(value = "levelNum", required = false) Integer levelNum,
                                                  @RequestParam(value = "name", required = false) String name);

    @ApiOperation(value = "保存模板主信息")
    @RequestMapping(value = "/saveTemp", method = RequestMethod.POST)
    KpiTempMain saveTemp(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "name") String name);

    @ApiOperation(value = "删除模板")
    @RequestMapping(value = "/removeTemp", method = RequestMethod.POST)
    void removeTemp(@RequestParam(value = "tempId") Long tempId);

    @ApiOperation(value = "批量新增模板指标项")
    @RequestMapping(value = "/saveTempItemBatch", method = RequestMethod.POST)
    void saveTempItemBatch(@RequestParam(value = "tempId") Long tempId,
                           @RequestParam(value = "baseItemIdList") List<Long> baseItemIdList);

    @ApiOperation(value = "批量删除模板指标项")
    @RequestMapping(value = "/removeTempItemBatch", method = RequestMethod.POST)
    void removeTempItemBatch(@RequestParam(value = "tempId") Long tempId,
                             @RequestParam(value = "ids") List<Long> ids);

    @ApiOperation(value = "模板指标项调序")
    @RequestMapping(value = "/updateTempItemSortNum", method = RequestMethod.POST)
    void updateTempItemSortNum(@RequestParam(value = "tempItemId") Long tempItemId,
                               @RequestParam(value = "operFlag") Integer operFlag);


}
