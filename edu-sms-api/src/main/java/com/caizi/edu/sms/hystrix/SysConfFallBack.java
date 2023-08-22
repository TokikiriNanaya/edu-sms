package com.caizi.edu.sms.hystrix;

import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.controller.impl.AntnestBaseFallback;
import com.caizi.edu.sms.api.SysConfApi;
import com.caizi.edu.sms.entity.SysConf;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 系统配置表 接口信息
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */


@Component
public class SysConfFallBack extends AntnestBaseFallback<SysConf> implements SysConfApi {

    @Override
    public FileObject getSysLogo() {
        return null;
    }

    @Override
    public FileObject getSysIcon() {
        return null;
    }

    @Override
    public void doUploadSysLogo(@RequestParam(value = "fileObjectId") Long fileObjectId) {

    }

    @Override
    public void doUploadSysIcon(@RequestParam(value = "fileObjectId") Long fileObjectId) {

    }

    @Override
    public SysConf getSysConf() {
        return null;
    }

    @Override
    public SysConf updateSysConf(@RequestBody SysConf sysConf) {
        return null;
    }

    @Override
    public void doUploadAppBackGround(@RequestParam(value = "fileObjectId") Long fileObjectId) {

    }

    @Override
    public FileObject getAppBackGround() {
        return null;
    }

    @Override
    public void deleteAppBackGround() {

    }
}

