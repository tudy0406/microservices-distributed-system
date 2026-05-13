package pt.project.microservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SuggestionClient {

    private final RestTemplate restTemplate;

    public SuggestionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "suggestions", fallbackMethod = "fallbackSuggestions")
    public List<Integer> requestSuggestions(int requestedMovieId) {

        String url = UriComponentsBuilder
                .fromUriString("http://localhost:8084/suggestions/search")
                .queryParam("movieId", requestedMovieId)
                .toUriString();
        Integer[] response = restTemplate.getForObject(
                url,
                Integer[].class
        );
        System.out.println("SUGGESTION SERVICE SUCCESS");
        if(response == null || response.length == 0) {
            return new ArrayList<>();
        }

        return Arrays.asList(response);
    }

    public List<Integer> fallbackSuggestions(int requestedMovieId, Throwable throwable) {
        System.out.println( "Fallback triggered");
        return List.of(1,2,3,4,5);
    }
}
