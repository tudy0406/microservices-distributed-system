package pt.project.suggestions_service_v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pt.project.suggestions_service_v2.db.DatabaseManager;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DatabaseManager databaseManager() {
        return new DatabaseManager();
    }
}
