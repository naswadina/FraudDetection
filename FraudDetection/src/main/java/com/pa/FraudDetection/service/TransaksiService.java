package com.pa.FraudDetection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa.FraudDetection.model.Transaksi;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransaksiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String fastApiUrl = "http://127.0.0.1:8000/analisis-transaksi/";

    public String analisisTransaksi(Transaksi transaksi) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String body = objectMapper.writeValueAsString(transaksi);
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(fastApiUrl, HttpMethod.POST, request, String.class);
            return response.getBody();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

}
