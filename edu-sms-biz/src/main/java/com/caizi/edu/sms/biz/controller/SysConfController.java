package com.caizi.edu.sms.biz.controller;

import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.controller.impl.AntnestSimpleBaseController;
import com.caizi.edu.sms.api.SysConfApi;
import com.caizi.edu.sms.biz.service.SysConfService;
import com.caizi.edu.sms.entity.SysConf;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@RestController
@RequestMapping(SysConfApi._PATH)
public class SysConfController extends AntnestSimpleBaseController<SysConfService, SysConf> implements SysConfApi {

    @Override
    public FileObject getSysLogo() {
        return baseService.getSysLogo();
    }

    @Override
    public FileObject getSysIcon() {
        return baseService.getSysIcon();
    }

    @Override
    public void doUploadSysLogo(@RequestParam(value = "fileObjectId") Long fileObjectId) {
        baseService.doUploadSysLogo(fileObjectId);
    }

    @Override
    public void doUploadSysIcon(@RequestParam(value = "fileObjectId") Long fileObjectId) {
        baseService.doUploadSysIcon(fileObjectId);
    }

    @Override
    public SysConf getSysConf() {
        return baseService.getSysConf();
    }

    @Override
    public SysConf updateSysConf(@RequestBody SysConf sysConf) {
        return baseService.updateSysConf(sysConf);
    }

    @Override
    public void doUploadAppBackGround(@RequestParam(value = "fileObjectId") Long fileObjectId) {
        baseService.doUploadAppBackGround(fileObjectId);
    }

    @Override
    public FileObject getAppBackGround() {
        return baseService.getAppBackGround();
    }

    @Override
    public void deleteAppBackGround() {
        baseService.deleteAppBackGround();
    }
}

