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
    public String analyzeTransaction(@RequestParam BigDecimal amount,
                                     @RequestParam int typeOfCard,
                                     @RequestParam int entryMode,
                                     @RequestParam int typeOfTransaction,
                                     @RequestParam int countryOfTransaction,
                                     @RequestParam int gender,
                                     @RequestParam int bank,
                                     @RequestParam int dayOfWeek,
                                     Model model, HttpSession session) {
        // Mengecek apakah session ada dan valid
        String username = (String) session.getAttribute("username");
        if (username == null) {
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
            // Membuat objek transaksi dari data yang diterima dari form
            List<User> users = userRepository.findByUsername(username);
            Transaksi transaksi = new Transaksi(
                    users.get(0), amount, typeOfCard, entryMode, typeOfTransaction,
                    countryOfTransaction, gender, bank, dayOfWeek, false
            );

            // Konversi objek transaksi menjadi JSON string
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

            String analysisResult = response.getBody();
            System.out.println("Hasil Analisis dari Python: " + analysisResult);

            if (analysisResult != null && analysisResult.equals("Fraud")) {
                transaksi.setFraud(true);
            } else {
                transaksi.setFraud(false);
            }

            transaksiRepository.save(transaksi);
            model.addAttribute("transaksi", transaksi);
            model.addAttribute("hasilAnalisis", analysisResult);

            return "dashboard";

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

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        // Cek session
        if (username == null || email == null) {
            return "redirect:/login";  // Jika session tidak ada, arahkan ke halaman login
        }

        // Ambil data user berdasarkan username
        List<User> user = userRepository.findByUsername(username);
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("user", user);

        return "dashboard";  // Menampilkan halaman dashboard
    }
}
