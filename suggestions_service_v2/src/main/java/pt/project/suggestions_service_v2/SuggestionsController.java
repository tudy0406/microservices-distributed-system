package pt.project.suggestions_service_v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RequestMapping("/suggestions")
@RestController
public class SuggestionsController {

    private final SuggestionsService suggestionsService;
    public SuggestionsController(SuggestionsService suggestionsService ) {
        this.suggestionsService = suggestionsService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Integer>> getSuggestions(@RequestParam("movieId") int id){
        List<Integer> response = suggestionsService.searchSuggestions(id);
        return ResponseEntity.ok(response);
    }

}
