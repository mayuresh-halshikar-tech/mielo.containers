package org.mielo.containers;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DatabaseConfiguration {
    private String container;
    private String image;
    private Integer port;
    private String user;
    private String password;
    private String database;
}
