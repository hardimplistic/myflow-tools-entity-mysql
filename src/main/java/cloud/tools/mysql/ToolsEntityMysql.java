package cloud.tools.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import cloud.dependency.core.jpa.CloudJpaRepositoryFactoryBean;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaRepositories(basePackages = {"cloud.service.tools.entity.mysql.jpa.repository" }, repositoryFactoryBeanClass = CloudJpaRepositoryFactoryBean.class)
@EnableSpringDataWebSupport
public class ToolsEntityMysql {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ToolsEntityMysql.class, args);
    }

}
