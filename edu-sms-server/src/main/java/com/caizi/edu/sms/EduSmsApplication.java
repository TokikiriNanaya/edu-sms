package com.caizi.edu.sms;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication(scanBasePackages = {
        //核心逻辑实现
        "com.antnest.amc.biz",
        "com.antnest.uim.biz",
        "com.antnest.cmc.biz",
        "com.antnest.uam.biz",
        "com.antnest.uom.biz",
        "com.antnest.foss.biz",
        "com.antnest.lac.biz",
        "com.caizi.edu.sms.biz"
})
//mybatis mapper　路径
@MapperScan({
        "com.antnest.amc.biz.mapper",
        "com.antnest.uim.biz.mapper",
        "com.antnest.cmc.biz.mapper",
        "com.antnest.uam.biz.mapper",
        "com.antnest.uom.biz.mapper",
        "com.antnest.foss.biz.mapper",
        "com.antnest.lac.biz.mapper",
        "com.caizi.edu.sms.biz.mapper"
})
//使用的feign接口
@EnableFeignClients({
        "com.caizi.edu.sms.api"
})
//开启事务控制
@EnableTransactionManagement
//开启swagger文档支持
@EnableSwagger2Doc
@EnableEurekaClient
@EnableScheduling
@EnableAsync
public class EduSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduSmsApplication.class, args);
    }
    @Bean
    public TaskExecutor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(2);
        //配置最大线程数
        executor.setMaxPoolSize(30);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("edu-sms-");

        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
