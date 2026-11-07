package com.stockpilot.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String checkHealth() {
        return "StockPilot backend is running";
    }
}