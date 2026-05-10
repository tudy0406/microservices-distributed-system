package pt.project.suggestions_service_v2;

import jdk.jshell.SourceCodeAnalysis;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestionsService {

    public SuggestionsService() {}

    public List<Integer> searchSuggestions(int id){
        Movie movie = this.getMovie();
        List<Integer> suggestions = this.getSuggestions(movie);
        return suggestions;
    }

    private Movie getMovie(){
        //mock
        return new Movie(1, "Inception", "Dream Movie");
    }

    private List<Integer> getSuggestions(Movie movie){
        List<Integer> suggestions = new ArrayList<>();

        //mock
        suggestions.add(2);
        suggestions.add(3);
        suggestions.add(4);
        return suggestions;
    }
}
