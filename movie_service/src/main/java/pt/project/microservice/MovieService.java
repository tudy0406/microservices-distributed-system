package pt.project.microservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pt.project.microservice.model.Movie;
import pt.project.microservice.model.MovieDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final RestTemplate restTemplate;

    public MovieService(RestTemplate restTemplate) {
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

        //mock
        return new Movie(1, "Inception", "Dream movie");
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

        List<Movie> movies = new ArrayList<>();

        for (Integer id : ids) {

            //mock
            movies.add(
                    new Movie(
                            id,
                            "Suggested Movie " + id,
                            "Description"
                    )
            );
        }

        return movies;
    }
}