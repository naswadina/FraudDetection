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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
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
    public String showInputManualForm() {
        return "inputManual";  // Menampilkan halaman input manual
    }

    @PostMapping("/inputManual")
    public String analyzeTransaction(Transaksi transaksi, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            // Konversi objek transaksi menjadi JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(transaksi);  // Memungkinkan JsonProcessingException

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
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Ambil username dari session
        String username = (String) session.getAttribute("username");

        // Pastikan pengguna sudah login
        if (username == null) {
            return "redirect:/login";  // Jika belum login, arahkan ke halaman login
        }

        // Ambil data pengguna berdasarkan username
        User user = userRepository.findByUsername(username);

        List<Transaksi> transaksiList = transaksiRepository.findByUser(user);
        Transaksi transaksi = transaksiList.isEmpty() ? null : transaksiList.get(transaksiList.size() - 1);

        // Ambil hasil analisis dari transaksi terakhir
        String hasilAnalisis = (transaksi != null) ? transaksi.getFraud() ? "Fraud" : "Not Fraud" : "No transaction yet";

        // Menambahkan data ke model
        model.addAttribute("username", username);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("hasilAnalisis", hasilAnalisis);
        model.addAttribute("transaksi", transaksi); // Menambahkan transaksi terakhir ke model

        return "dashboard";  // Menampilkan halaman dashboard
    }

}
