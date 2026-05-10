package pt.project.api_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GatewayController {

    @Autowired
    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }

    @GetMapping("/movie")
    public String query(@RequestBody String query) {
        return service.search(query);
    }
}
