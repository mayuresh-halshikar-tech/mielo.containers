package org.mielo.containers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class ContainerFactory {

    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.mielo.containers.datasource.port}")
    private Integer containerMappedPort;
    @Value("${spring.datasource.database}")
    private String database;
    private final ConfigurableEnvironment environment;

    public DataSource startMySQLContainer() {
        MySQLContainer container = new MySQLContainer(ImageHolder.image)
                .withDatabaseName(database)
                .withUsername(user)
                .withPassword(password);
        container.withAccessToHost(true);
        Testcontainers.exposeHostPorts(containerMappedPort);
        container.getPortBindings().add(containerMappedPort + ":" + MySQLContainer.MYSQL_PORT);
        container.start();

        String jdbcUrl = container.getJdbcUrl();

        MutablePropertySources propertySources = environment.getPropertySources();
        Properties properties = new Properties();
        properties.put("spring.datasource.url", jdbcUrl);
        PropertySource<?> propertySource = new PropertiesPropertySource("containerDBProperties", properties);
        propertySources.addFirst(propertySource);

        return DataSourceBuilder
                .create()
                .url(container.getJdbcUrl())
                .username(user)
                .password(password)
                .driverClassName(container.getDriverClassName())
                .build();
    }


}
