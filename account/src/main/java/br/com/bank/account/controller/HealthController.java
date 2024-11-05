package br.com.bank.account.controller;

import br.com.bank.account.dto.HealthStatusDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HealthController {


    @Value("${application.name}")
    private String applicationName;

    @Value("${application.version}")
    private String applicationVersion;

    @PostConstruct
    public void init() {
        System.out.println("Application Name: " + applicationName);
        System.out.println("Application Version: " + applicationVersion);
    }


    @GetMapping("/health")
    public ResponseEntity<HealthStatusDTO> getHealthStatus(){

        HealthStatusDTO response = new HealthStatusDTO(
                applicationName,
                "UP",
                applicationVersion,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
