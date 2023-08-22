package com.caizi.edu.sms.api;

import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.controller.AntnestSimpleBaseApi;
import com.caizi.edu.sms.constant.ApplicationInfo;
import com.caizi.edu.sms.entity.SysConf;
import com.caizi.edu.sms.hystrix.SysConfFallBack;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 系统配置表 接口信息
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */

@Api(tags = {"系统配置表"})
@FeignClient(value = ApplicationInfo.ANTNEST_SERVICE_NAME, fallback = SysConfFallBack.class, path = SysConfApi._PATH)
public interface SysConfApi extends AntnestSimpleBaseApi<SysConf> {
    String _PATH = ApplicationInfo.ANTNEST_BASE_PATH + "/sms_sys_conf";


    @ApiOperation(value = "查看系统LOGO")
    @RequestMapping(value = "/getSysLogo", method = RequestMethod.POST)
    FileObject getSysLogo();

    @ApiOperation(value = "查看系统ICON")
    @RequestMapping(value = "/getSysIcon", method = RequestMethod.POST)
    FileObject getSysIcon();

    @ApiOperation(value = "上传系统LOGO")
    @RequestMapping(value = "/doUploadSysLogo", method = RequestMethod.POST)
    void doUploadSysLogo(@RequestParam(value = "fileObjectId") Long fileObjectId);

    @ApiOperation(value = "上传系统ICON")
    @RequestMapping(value = "/doUploadSysIcon", method = RequestMethod.POST)
    void doUploadSysIcon(@RequestParam(value = "fileObjectId") Long fileObjectId);

    @ApiOperation(value = "查询系统配置（无登录）")
    @RequestMapping(value = "/getSysConf", method = RequestMethod.POST)
    SysConf getSysConf();

    @ApiOperation(value = "编辑系统配置")
    @RequestMapping(value = "/updateSysConf", method = RequestMethod.POST)
    SysConf updateSysConf(@RequestBody SysConf sysConf);

    /*===================================移动端===================================*/

    @ApiOperation(value = "上传封面图片")
    @RequestMapping(value = "/doUploadAppBackGround", method = RequestMethod.POST)
    void doUploadAppBackGround(@RequestParam(value = "fileObjectId") Long fileObjectId);

    @ApiOperation(value = "查询封面图片（无登录）")
    @RequestMapping(value = "/getAppBackGround", method = RequestMethod.POST)
    FileObject getAppBackGround();

    @ApiOperation(value = "删除封面图片")
    @RequestMapping(value = "/deleteAppBackGround", method = RequestMethod.POST)
    void deleteAppBackGround();
}

