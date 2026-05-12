package pt.project.microservice.model;
import pt.project.microservice.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDTO {

    private List<Movie> requestedMovie =  new ArrayList<>();
    private List<Movie> suggestions = new ArrayList<>();

    public List<Movie> getRequestedMovie() {
        return requestedMovie;
    }

    public void setRequestedMovie(List<Movie> requestedMovie) {
        this.requestedMovie = requestedMovie;
    }

    public List<Movie> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Movie> suggestions) {
        this.suggestions = suggestions;
    }
}
