package pt.project.microservice;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.project.microservice.model.MovieDTO;

@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/movie")
@RestController
public class MovieController {

    private final  MovieService service;
    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<MovieDTO> search(@RequestParam String query){
        MovieDTO response = service.search(query);
        return ResponseEntity.ok(response);
    }
}
