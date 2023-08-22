package com.caizi.edu.sms.biz.service.impl;

import com.antnest.foss.api.FileObjectApi;
import com.antnest.foss.dto.request.FileBizSingleRequestDTO;
import com.antnest.foss.dto.request.FileObjectBatchSaveRequestDTO;
import com.antnest.foss.entity.FileObject;
import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.antnest.mscore.config.prop.AntnestAppProperties;
import com.antnest.mscore.config.prop.AntnestWxProperties;
import com.antnest.mscore.context.RequestContextHolder;
import com.antnest.mscore.exception.BizException;
import com.antnest.mscore.tx.Tx;
import com.caizi.edu.sms.biz.mapper.SysConfMapper;
import com.caizi.edu.sms.biz.service.SysConfService;
import com.caizi.edu.sms.entity.SysConf;
import com.caizi.edu.sms.enums.FileObjectBizEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Service
public class SysConfServiceImpl extends AntnestBaseServiceImpl<SysConfMapper, SysConf> implements SysConfService {

    @Resource
    FileObjectApi fileObjectApi;

    @Resource
    private AntnestAppProperties appProperties;

    @Resource
    private AntnestWxProperties antnestWxProperties;

    @Override
    public FileObject getSysLogo() {
        /*
         获取系统配置
         */
        SysConf sysConf = this.getSysConf();
        /*
         获取附件
         */
        return getSysConfFile(sysConf, FileObjectBizEnum.SYSCONF_LOGO);
    }

    @Override
    public FileObject getSysIcon() {
        /*
         获取系统配置
         */
        SysConf sysConf = this.getSysConf();
        /*
         获取附件
         */
        return getSysConfFile(sysConf, FileObjectBizEnum.SYSCONF_ICON);
    }

    @Override
    public void doUploadSysLogo(Long fileObjectId) {
        /*
         获取系统配置
         */
        SysConf sysConf = this.getSysConf();
        /*
         删除老附件
         */
        FileBizSingleRequestDTO delDTO = new FileBizSingleRequestDTO();
        delDTO.setBizObject(FileObjectBizEnum.SYSCONF_LOGO.getCode());
        delDTO.setBizObjectId(sysConf.getId());
        fileObjectApi.delBizObjectFile(delDTO);
        /*
         上传附件
         */
        uploadFile(fileObjectId, sysConf, FileObjectBizEnum.SYSCONF_LOGO);
    }

    @Override
    public void doUploadSysIcon(Long fileObjectId) {
        /*
         获取系统配置
         */
        SysConf sysConf = this.getSysConf();
        /*
         删除老附件
         */
        FileBizSingleRequestDTO delDTO = new FileBizSingleRequestDTO();
        delDTO.setBizObject(FileObjectBizEnum.SYSCONF_ICON.getCode());
        delDTO.setBizObjectId(sysConf.getId());
        fileObjectApi.delBizObjectFile(delDTO);
        /*
         上传附件
         */
        uploadFile(fileObjectId, sysConf, FileObjectBizEnum.SYSCONF_ICON);
    }

    @Override
    public SysConf getSysConf() {
        RequestContextHolder.bind(appProperties.getOrganizationAccountId());
        List<SysConf> sysConfList = super.list();
        if (sysConfList == null || sysConfList.size() == 0) throw new BizException("系统配置记录丢失，请联系运维管理员！");
        SysConf sysConf = sysConfList.get(0);
        //拼装微信公众号ID
        sysConf.setWxAppId(antnestWxProperties.getAppId());

        return sysConf;
    }

    @Tx
    @Override
    public SysConf updateSysConf(SysConf sysConf) {
        /*
         入参校验
         */
        if (sysConf == null) throw new BizException("传入的对象为空！");
        if (StringUtils.isEmpty(sysConf.getName())) throw new BizException("系统名称不能为空！");
        if (StringUtils.isEmpty(sysConf.getAppMainPageName())) throw new BizException("APP首页名称不能为空！");
        if (sysConf.getId() == null) throw new BizException("传入的系统配置ID为空，请联系运维管理员！");
        if (sysConf.getMonthReportStatus() == null) throw new BizException("同步大数据平台配置不能为空，请联系运维管理员！");
        if (sysConf.getMonthReportStatus() != null && sysConf.getMonthReportStatus()) {
            if (StringUtils.isEmpty(sysConf.getMonthReportBegin())) throw new BizException("开启月报上传状态后，月报开始时间范围不能为空！");
            if (StringUtils.isEmpty(sysConf.getMonthReportEnd())) throw new BizException("开启月报上传状态后，月报结束时间范围不能为空！");
        }
        /*
         补全默认值
         */
        if (sysConf.getStatus() == null) sysConf.setStatus(true);
        if (sysConf.getMonthReportStatus() == null) sysConf.setMonthReportStatus(false);
        /*
         更新
         */
        super.updateById(sysConf);
        return sysConf;
    }

    @Override
    public void doUploadAppBackGround(Long fileObjectId) {
        /*
         获取系统配置
         */
        SysConf sysConf = this.getSysConf();
        /*
         删除老附件
         */
        FileBizSingleRequestDTO delDTO = new FileBizSingleRequestDTO();
        delDTO.setBizObject(FileObjectBizEnum.SYSCONF_APP.getCode());
        delDTO.setBizObjectId(sysConf.getId());
        fileObjectApi.delBizObjectFile(delDTO);
        /*
         上传附件
         */
        uploadFile(fileObjectId, sysConf, FileObjectBizEnum.SYSCONF_APP);
    }

    @Override
    public FileObject getAppBackGround() {
        RequestContextHolder.bind(appProperties.getOrganizationAccountId());
        /*
         获取系统配置
         */
        SysConf sysConf = this.getSysConf();
        /*
         获取附件
         */
        return getSysConfFile(sysConf, FileObjectBizEnum.SYSCONF_APP);
    }

    @Override
    public void deleteAppBackGround() {
        /*
         获取系统配置
         */
        SysConf sysConf = this.getSysConf();
        /*
         删除附件
         */
        FileBizSingleRequestDTO delDTO = new FileBizSingleRequestDTO();
        delDTO.setBizObject(FileObjectBizEnum.SYSCONF_APP.getCode());
        delDTO.setBizObjectId(sysConf.getId());
        fileObjectApi.delBizObjectFile(delDTO);
    }


    /*
     * ========================================以下都是私有方法========================================
     */
    private void uploadFile(Long fileObjectId, SysConf sysConf, FileObjectBizEnum sysconfLogo) {
        List<FileObject> fileObjectList = new ArrayList<>();
        FileObject fileObject = new FileObject();
        fileObject.setId(fileObjectId);
        fileObject.setBizType(10);
        fileObject.setBizObjectId(sysConf.getId());
        fileObject.setBizObject(sysconfLogo.getCode());
        fileObject.setBizObjectName(sysconfLogo.getName());
        fileObjectList.add(fileObject);
        FileObjectBatchSaveRequestDTO requestDTO = new FileObjectBatchSaveRequestDTO();
        requestDTO.setFileObjects(fileObjectList);
        fileObjectApi.submitSaveBatch(requestDTO);
    }

    private FileObject getSysConfFile(SysConf sysConf, FileObjectBizEnum sysconfLogo) {
        FileBizSingleRequestDTO requestDTO = new FileBizSingleRequestDTO();
        requestDTO.setBizObject(sysconfLogo.getCode());
        requestDTO.setBizObjectId(sysConf.getId());
        List<FileObject> fileObjectList = fileObjectApi.listOfBizObject(requestDTO);
        return fileObjectList != null && fileObjectList.size() > 0 ? fileObjectList.get(0) : null;
    }
}
