package com.zhm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by 赵红明 on 2019/10/30.
 */
@SpringBootApplication(scanBasePackages = {"com.zhm"})
@EnableJpaRepositories(basePackages = {"com.zhm.**.dao"})
@EntityScan(basePackages = {"com.zhm.**.entity"})
@PropertySource("classpath:env/${spring.profiles.active}/application.properties")
@EnableSpringDataWebSupport
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync//启动异步
@ServletComponentScan//Servlet、Filter、Listener 可以直接通过 @WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码。
public class DemogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemogApplication.class, args);
    }

}

