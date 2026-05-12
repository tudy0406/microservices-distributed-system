package pt.project.api_gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<String> query(@RequestParam String query) {
        String response = service.search(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/watch")
    public ResponseEntity<Void> watch(@RequestParam int movieId) {
        service.addWatched(movieId);
        return ResponseEntity.noContent().build();
    }
}
