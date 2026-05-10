package pt.project.microservice.model;
import pt.project.microservice.model.Movie;

import java.util.List;

public class MovieDTO {

    private Movie requestedMovie;
    private List<Movie> suggestions;

    public Movie getRequestedMovie() {
        return requestedMovie;
    }

    public void setRequestedMovie(Movie requestedMovie) {
        this.requestedMovie = requestedMovie;
    }

    public List<Movie> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Movie> suggestions) {
        this.suggestions = suggestions;
    }
}
