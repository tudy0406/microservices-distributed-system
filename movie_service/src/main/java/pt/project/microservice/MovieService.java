package pt.project.microservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    private final SuggestionClient suggestionClient;

    public MovieService(DatabaseManager databaseManager, SuggestionClient suggestionClient) {
        this.databaseManager = databaseManager;
        this.suggestionClient = suggestionClient;
    }

    public MovieDTO search(String query) {

        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setRequestedMovie(this.findMovie(query));
        if(movieDTO.getRequestedMovie().isEmpty()) {
            movieDTO.setSuggestions(this.findTopWatchedMovies());
            return movieDTO;
        }
        List<Integer> ids = suggestionClient.requestSuggestions(movieDTO.getRequestedMovie().getFirst().getId());

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

    private List<Movie> findMoviesByIds(List<Integer> ids) {
        return databaseManager.getMoviesByIds(ids);
    }

    private List<Movie> findTopWatchedMovies() {
        return databaseManager.getTopWatchedMovies();
    }
}