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
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(EnableContainerManagement.class.getName(), false));
        String[] profiles = attributes.getStringArray("profiles");
        ImageHolder.image = attributes.getString("image");
        if(environment.matchesProfiles(profiles)) {
            String container = attributes.getString("container");
            if(container.equalsIgnoreCase("mysql")) {
                log.info(format("**************** MIELO Containers | Starting container: '%s' | Image: '%s' ", container, ImageHolder.image));
                return new String [] {MySQLContainerConfig.class.getName()};
            } else {
                log.warn(format("**************** MIELO Containers | Container '%s' is not yet supported", container));
            }
        }
        return new String[0];
    }
}
