package pt.project.suggestions_service_v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

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
        String chaos = System.getenv("CHAOS_MODE");
        try{
            if ("true".equalsIgnoreCase(chaos)) {

                Random random = new Random();

                // Random latency
                int delay = 3000 + random.nextInt(7000);

                Thread.sleep(delay);

                // Random failure
                if (random.nextBoolean()) {
                    throw new ResponseStatusException(
                            HttpStatus.SERVICE_UNAVAILABLE,
                            "Chaos mode failure"
                    );
                }
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        List<Integer> response = suggestionsService.searchSuggestions(id);
        return ResponseEntity.ok(response);
    }

}
