package pt.project.api_gateway;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GatewayService {

    private final RestTemplate restTemplate;

    public GatewayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String search(String query) {

        String url = UriComponentsBuilder
                .fromUriString("http://localhost:8080/movie/search")
                .queryParam("query", query)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}