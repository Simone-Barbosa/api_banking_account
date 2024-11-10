package br.com.bank.account.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class HealthStatusDTO {

    private String applicationName;
    private String status;
    private String applicationVersion;
    private LocalDateTime timestamp;

    // constructor
    public  HealthStatusDTO(){
    }

    // constructor
    public HealthStatusDTO(String applicationName, String status, String applicationVersion, LocalDateTime timestamp) {
        this.applicationName = applicationName;
        this.status = status;
        this.applicationVersion = applicationVersion;
        this.timestamp = timestamp;
    }
}

// mutable class