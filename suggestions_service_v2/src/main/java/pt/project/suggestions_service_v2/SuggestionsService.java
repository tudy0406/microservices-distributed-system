package pt.project.suggestions_service_v2;

import jdk.jshell.SourceCodeAnalysis;
import org.springframework.stereotype.Service;
import pt.project.suggestions_service_v2.db.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestionsService {

    private final DatabaseManager databaseManager;

    public SuggestionsService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<Integer> searchSuggestions(int id){
        Movie movie = this.getMovie(id);
        if(movie == null){
            return new ArrayList<>();
        }
        List<Integer> suggestions = this.getSuggestions(movie);
        return suggestions;
    }

    private Movie getMovie(int id){
        return databaseManager.getMovieById(id);
    }

    private List<Integer> getSuggestions(Movie movie){
        List<Integer> genres = movie.getGenreIds();
        int directorId = movie.getDirectorId();
        int requestedMovieId = movie.getId();
        List<Integer> suggestions = new ArrayList<>();

        suggestions.addAll(databaseManager.getSuggestions("genres", genres, requestedMovieId));
        suggestions.addAll(databaseManager.getSuggestions("director", List.of(directorId), requestedMovieId));

        return suggestions;

    }
}
