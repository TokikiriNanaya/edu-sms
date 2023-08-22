package com.caizi.edu.sms.biz.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class YmlEduSmsConfig {

    @Value("${edu-sms.temp-file-root}")
    private String tempFileRoot;

    @Value("${edu-sms.application-code-pc}")
    private String applicationCode_pc;

    @Value("${edu-sms.quartz-local-api-path}")
    private String quartzLocalApiPath;

    @Value("${edu-sms.wx-template-id}")
    private String wxTemplateId;

    @Value("${edu-sms.wx-web-url}")
    private String wxWebUrl;
}