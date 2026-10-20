package com.Showroom.Microservice.ApiGateway.SecurityController;

import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class DownstreamFallbackController {
    @RequestMapping("/internal/downstream-unavailable")
    public ResponseEntity<Map<String, String>> unavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("message", "Downstream service is temporarily unavailable"));
    }
}
