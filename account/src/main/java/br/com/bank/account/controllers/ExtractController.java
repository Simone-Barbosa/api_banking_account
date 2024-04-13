package br.com.bank.account.controllers;

import br.com.bank.account.entities.ExtractEntity;
import br.com.bank.account.services.ExtractService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("extract_transactions")
public class ExtractController {
    @Autowired
    ExtractService extractService;



    @PostMapping("/extract")
    public ResponseEntity <List<ExtractEntity>> searchExtractByNumber(@RequestBody String param) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(param);
        String account_main = jsonNode.get("account_main").asText();

        try {
            List<ExtractEntity> extract = extractService.getExtractAccount(account_main);
            if (!extract.isEmpty()) {
                return ResponseEntity.ok(extract);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // erro 500
        }
    }
}
