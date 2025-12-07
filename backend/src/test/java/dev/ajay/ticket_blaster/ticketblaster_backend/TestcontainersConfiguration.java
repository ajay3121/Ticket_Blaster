package dev.ajay.ticket_blaster.ticketblaster_backend;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgreSQLContainer() {
        return  new PostgreSQLContainer(DockerImageName.parse("postgres:15"));
    }

    @Bean
    ApplicationRunner logContainerInfo(PostgreSQLContainer container) {
        return args -> {
            System.out.println("\n--- 🚀 CONTAINER JDBC DETAILS 🚀 ---");
            System.out.println("URL:      " + container.getJdbcUrl());
            System.out.println("Username: " + container.getUsername());
            System.out.println("Password: " + container.getPassword());
            System.out.println("--------------------------------------\n");
        };
    }
}
