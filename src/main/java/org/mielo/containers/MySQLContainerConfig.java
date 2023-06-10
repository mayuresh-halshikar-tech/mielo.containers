package org.mielo.containers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "org.mielo.containers")
@RequiredArgsConstructor
@Import(ContainerFactory.class)
public class MySQLContainerConfig {

    private final ContainerFactory factory;

    @Bean
    @Primary
    public DataSource datasource() {
        return factory.startMySQLContainer();
    }

}
