package com.caizi.edu.sms.biz.service;

import com.antnest.foss.entity.FileObject;
import com.caizi.edu.sms.entity.SysConf;
import com.antnest.mscore.base.service.AntnestBaseService;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
public interface SysConfService extends AntnestBaseService<SysConf> {

    FileObject getSysLogo();

    FileObject getSysIcon();

    void doUploadSysLogo(Long fileObjectId);

    void doUploadSysIcon(Long fileObjectId);

    SysConf getSysConf();

    SysConf updateSysConf(SysConf sysConf);

    void doUploadAppBackGround(Long fileObjectId);

    FileObject getAppBackGround();

    void deleteAppBackGround();
}
