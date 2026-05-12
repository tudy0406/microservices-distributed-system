package pt.project.api_gateway;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class GatewayService {

    private final RestTemplate restTemplate;

    public GatewayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String search(String query) {

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8082/movie/search")
                .queryParam("query", query)
                .build()
                .toUri();
        return restTemplate.getForObject(uri, String.class);
    }

    public void addWatched(int movieId) {
        String url = UriComponentsBuilder
                .fromUriString("http://localhost:8082/movie/watch")
                .queryParam("movieId", movieId)
                .toUriString();
        restTemplate.getForObject(url, null);
    }
}