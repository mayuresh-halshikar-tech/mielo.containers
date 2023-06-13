package org.mielo.containers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
public class ContainerConfigurationSelector implements ImportSelector {

    private final Environment environment;


    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        DatabaseConfiguration configuration = getDatabaseConfiguration();
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(EnableContainerManagement.class.getName(), false));
        String[] profiles = attributes.getStringArray("profiles");
        DatabaseConfigurationHolder.setDatabaseConfiguration(configuration);
        if(environment.matchesProfiles(profiles)) {
            if(configuration.getContainer().equalsIgnoreCase("mysql")) {
                log.info(format("**************** MIELO Containers | Starting container: '%s' | Image: '%s' ",
                        configuration.getContainer(), configuration.getImage()));
                return new String [] {MySQLContainerConfig.class.getName()};
            } else {
                log.warn(format("**************** MIELO Containers | Container '%s' is not yet supported", configuration.getContainer()));
            }
        }
        return new String[0];
    }

    private DatabaseConfiguration getDatabaseConfiguration() {
        String container = environment.getProperty("spring.mielo.container.name");
        String image = environment.getProperty("spring.mielo.container.image");
        Integer port = environment.getProperty("spring.mielo.container.port", Integer.class);
        String user = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        String database = environment.getProperty("spring.datasource.database");

        return DatabaseConfiguration.builder()
                .container(container)
                .image(image)
                .port(port)
                .user(user)
                .password(password)
                .database(database)
                .build();

    }
}
