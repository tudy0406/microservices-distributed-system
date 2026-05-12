package pt.project.microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import pt.project.microservice.db.DatabaseManager;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(1500);
        factory.setReadTimeout(1500);

        return new RestTemplate(factory);
    }

    @Bean
    public DatabaseManager databaseManager() {
        return new DatabaseManager();
    }
}
