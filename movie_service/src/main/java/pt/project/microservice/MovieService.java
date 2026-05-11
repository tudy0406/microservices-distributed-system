package pt.project.microservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pt.project.microservice.db.DatabaseManager;
import pt.project.microservice.model.Movie;
import pt.project.microservice.model.MovieDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final DatabaseManager databaseManager;
    private final RestTemplate restTemplate;

    public MovieService(DatabaseManager databaseManager, RestTemplate restTemplate) {
        this.databaseManager = databaseManager;
        this.restTemplate = restTemplate;

    }

    public MovieDTO search(String query) {

        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setRequestedMovie(this.findMovie(query));
        List<Integer> ids = this.requestSuggestions(movieDTO.getRequestedMovie());
        movieDTO.setSuggestions(this.findMoviesByIds(ids));

        return movieDTO;
    }

    private Movie findMovie(String query) {
        return databaseManager.getMovieByTitle(query.toLowerCase());
    }

    private List<Integer> requestSuggestions(Movie movie) {

        String url = UriComponentsBuilder
                .fromUriString("http://localhost:8084/suggestions/search")
                .queryParam("movieId", movie.getId())
                .toUriString();

        Integer[] response = restTemplate.getForObject(
                url,
                Integer[].class
        );

        return List.of(response);
    }

    private List<Movie> findMoviesByIds(List<Integer> ids) {
        return databaseManager.getMoviesByIds(ids);
    }
}