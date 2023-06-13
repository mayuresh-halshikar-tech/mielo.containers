package org.mielo.containers;

import lombok.RequiredArgsConstructor;
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

    private final ConfigurableEnvironment environment;

    public DataSource startMySQLContainer() {
        DatabaseConfiguration configuration = DatabaseConfigurationHolder.getDatabaseConfiguration();
        MySQLContainer container = new MySQLContainer(configuration.getImage())
                .withDatabaseName(configuration.getDatabase())
                .withUsername(configuration.getUser())
                .withPassword(configuration.getPassword());
        container.withAccessToHost(true);
        Testcontainers.exposeHostPorts(configuration.getPort());
        container.getPortBindings().add(configuration.getPort() + ":" + MySQLContainer.MYSQL_PORT);
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
                .username(configuration.getUser())
                .password(configuration.getPassword())
                .driverClassName(container.getDriverClassName())
                .build();
    }


}
