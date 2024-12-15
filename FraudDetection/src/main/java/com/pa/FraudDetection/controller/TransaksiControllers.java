package com.pa.FraudDetection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa.FraudDetection.model.Transaksi;
import com.pa.FraudDetection.model.User;
import com.pa.FraudDetection.repository.TransaksiRepository;
import com.pa.FraudDetection.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class TransaksiControllers {

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private UserRepository userRepository;

    // URL FastAPI
    private static final String fastApiUrl = "http://127.0.0.1:8000/analisis-transaksi/";

    @GetMapping("/inputManual")
    public String showInputManualForm(HttpSession session, Model model) {
        // Cek session apakah ada username dan email
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        if (username == null || email == null) {
            return "redirect:/login";  // Jika session tidak ada, arahkan ke halaman login
        }

        model.addAttribute("username", username);  // Kirim data username ke view
        model.addAttribute("email", email);  // Kirim data email ke view
        return "inputManual";  // Menampilkan halaman input manual
    }

    @PostMapping("/inputManual")
    public String analyzeTransaction(@RequestParam int dayOfWeek,
                                     @RequestParam int typeOfCard,
                                     @RequestParam int entryMode,
                                     @RequestParam BigDecimal amount,
                                     @RequestParam int typeOfTransaction,
                                     @RequestParam int countryOfTransaction,
                                     @RequestParam int gender,
                                     @RequestParam int bank,
                                     Model model, HttpSession session) {
        // Mengecek apakah session ada dan valid
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");  // Ambil email dari session
        if (username == null || email == null) {
            return "redirect:/login";  // Jika session tidak ada, arahkan ke halaman login
        }

        // Validasi input dari form
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", "Amount should be greater than zero");
            return "inputManual";
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            // Membuat objek transaksi dari data yang diterima dari form tanpa memasukkan 'fraud'
            List<User> users = userRepository.findByUsername(username);
            Transaksi transaksi = new Transaksi(
                    users.get(0), dayOfWeek, typeOfCard, entryMode, amount, typeOfTransaction,
                    countryOfTransaction, gender, bank, false
            );

            // Buat objek JSON dari transaksi tanpa field 'fraud'
            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(transaksi);

            // Cetak JSON yang akan dikirim ke console
            System.out.println("JSON yang dikirim ke FastAPI: " + body);

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            // Kirim data ke FastAPI
            ResponseEntity<String> response = restTemplate.exchange(
                    fastApiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Ambil hasil analisis dari FastAPI
            String analysisResult = response.getBody();
            System.out.println("Hasil Analisis dari Python: " + analysisResult);

            // Tentukan fraud berdasarkan hasil prediksi dari FastAPI
            if (analysisResult != null && analysisResult.equals("fraud")) {
                transaksi.setFraud(true);
            } else {
                transaksi.setFraud(false);
            }

            // Simpan transaksi yang sudah terprediksi fraudnya
            transaksiRepository.save(transaksi);

            // Menambahkan data ke model untuk ditampilkan di dashboard
            model.addAttribute("username", username);
            model.addAttribute("email", email);  // Menambahkan email ke model
            model.addAttribute("transaksi", transaksi);
            model.addAttribute("hasilAnalisis", analysisResult);

            // Mapping data yang diterima dengan deskripsi yang lebih mudah dimengerti
            model.addAttribute("amount", amount);
            model.addAttribute("typeOfCard", getCardType(typeOfCard));
            model.addAttribute("entryMode", getEntryMode(entryMode));
            model.addAttribute("transactionType", getTransactionType(typeOfTransaction));
            model.addAttribute("country", getCountry(countryOfTransaction));
            model.addAttribute("gender", getGender(gender));
            model.addAttribute("bank", getBank(bank));
            model.addAttribute("dayOfWeek", getDayOfWeek(dayOfWeek));

            return "Dashboard";

        } catch (JsonProcessingException e) {
            model.addAttribute("error", "Error converting object to JSON: " + e.getMessage());
            return "error";
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "HTTP error: " + e.getMessage());
            return "error";
        } catch (ResourceAccessException e) {
            model.addAttribute("error", "Connection error: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error: " + e.getMessage());
            return "error";
        }
    }
    private String getCardType(int typeOfCard) {
        switch (typeOfCard) {
            case 0: return "MasterCard";
            case 1: return "Visa";
            default: return "Unknown";
        }
    }

    private String getEntryMode(int entryMode) {
        switch (entryMode) {
            case 0: return "CVC";
            case 1: return "PIN";
            case 2: return "Tap";
            default: return "Unknown";
        }
    }

    private String getTransactionType(int typeOfTransaction) {
        switch (typeOfTransaction) {
            case 0: return "ATM";
            case 1: return "Online";
            case 2: return "POS";
            default: return "Unknown";
        }
    }

    private String getCountry(int countryOfTransaction) {
        switch (countryOfTransaction) {
            case 0: return "China";
            case 1: return "India";
            case 2: return "Russia";
            case 3: return "USA";
            case 4: return "UK";
            default: return "Unknown";
        }
    }

    private String getGender(int gender) {
        switch (gender) {
            case 0: return "Female";
            case 1: return "Male";
            default: return "Unknown";
        }
    }

    private String getBank(int bank) {
        switch (bank) {
            case 0: return "Barclays";
            case 1: return "Barlcays";
            case 2: return "HSBC";
            case 3: return "Halifax";
            case 4: return "Lloyds";
            case 5: return "Metro";
            case 6: return "Monzo";
            case 7: return "RBS";
            default: return "Unknown";
        }
    }

    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 4: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 1: return "Thursday";
            case 0: return "Friday";
            case 5: return "Saturday";
            case 6: return "Sunday";
            default: return "Unknown";
        }
    }

    @GetMapping("/Dashboard")
    public String showDashboard(HttpSession session, Model model) {
        String analysisResult = "fraud";
        model.addAttribute("hasilAnalisis", analysisResult);

        return "Dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "logout";
    }
}