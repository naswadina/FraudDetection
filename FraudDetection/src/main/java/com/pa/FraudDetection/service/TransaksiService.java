package com.pa.FraudDetection.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.LocalDate;

@Service
public class TransaksiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "http://127.0.0.1:8000/analisis-transaksi/";

    public String analisisTransaksi(String amount, String typeOfCard, String entryMode, String transactionType,
                                    String countryOfTransaction, String gender, String bank, String dayOfWeek, LocalDate date) {
        // Mengonversi LocalDate menjadi string dengan format "YYYY-MM-DD"
        String dateAsString = date.toString();  // Format default LocalDate adalah YYYY-MM-DD

        // Menyusun URL untuk API dengan menambahkan parameter "date"
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("amount", amount)
                .queryParam("typeOfCard", typeOfCard)
                .queryParam("entryMode", entryMode)
                .queryParam("transactionType", transactionType)
                .queryParam("countryOfTransaction", countryOfTransaction)
                .queryParam("gender", gender)
                .queryParam("bank", bank)
                .queryParam("dayOfWeek", dayOfWeek)
                .queryParam("date", dateAsString);  // Menambahkan parameter date

        // Mengirim request dan menerima hasil analisis
        ResponseEntity<String> response = restTemplate.postForEntity(builder.toUriString(), null, String.class);

        // Mengembalikan hasil dari analisis
        return response.getBody();
    }
}
