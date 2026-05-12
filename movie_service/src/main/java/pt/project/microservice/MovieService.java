package pt.project.microservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pt.project.microservice.db.DatabaseManager;
import pt.project.microservice.model.Movie;
import pt.project.microservice.model.MovieDTO;

import java.util.ArrayList;
import java.util.Arrays;
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
        if(movieDTO.getRequestedMovie().isEmpty()) {
            movieDTO.setSuggestions(this.findTopWatchedMovies());
            return movieDTO;
        }
        List<Integer> ids = this.requestSuggestions(movieDTO.getRequestedMovie().getFirst().getId());

        List<Movie> suggestedMovies = this.findMoviesByIds(ids);

        if(suggestedMovies.isEmpty()) {
            movieDTO.setSuggestions(this.findTopWatchedMovies());
        }else{
            movieDTO.setSuggestions(suggestedMovies);
        }

        return movieDTO;
    }

    public void addWatched(int movieId){
        databaseManager.addWatchedMovie(movieId);
    }

    private List<Movie> findMovie(String query) {
        return databaseManager.getMovieByTitle(query.toLowerCase());
    }

    private List<Integer> requestSuggestions(int requestedMovieId) {

        String url = UriComponentsBuilder
                .fromUriString("http://localhost:8084/suggestions/search")
                .queryParam("movieId", requestedMovieId)
                .toUriString();

        Integer[] response = restTemplate.getForObject(
                url,
                Integer[].class
        );

        if(response == null || response.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.asList(response);
    }

    private List<Movie> findMoviesByIds(List<Integer> ids) {
        return databaseManager.getMoviesByIds(ids);
    }

    private List<Movie> findTopWatchedMovies() {
        return databaseManager.getTopWatchedMovies();
    }
}