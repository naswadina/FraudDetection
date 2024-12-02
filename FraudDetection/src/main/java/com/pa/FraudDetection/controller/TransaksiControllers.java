package com.pa.FraudDetection.controller;

import com.pa.FraudDetection.model.Transaksi;
import com.pa.FraudDetection.model.User;
import com.pa.FraudDetection.repository.TransaksiRepository;
import com.pa.FraudDetection.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class TransaksiControllers {

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private UserRepository userRepository;

    // URL FastAPI
    private static final String fastApiUrl = "http://127.0.0.1:8000";

    @GetMapping("/inputManual")
    public String showInputManualForm() {
        return "inputManual";  // Menampilkan halaman input manual
    }

    @PostMapping("/inputManual")
    public String analyzeTransaction(Transaksi transaksi, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        // Kirim data ke FastAPI
        ResponseEntity<String> response = restTemplate.postForEntity(
                fastApiUrl + "/analisis-transaksi/",  // Gunakan URL yang sudah didefinisikan
                transaksi,
                String.class
        );

        // Ambil hasil analisis
        String analysisResult = response.getBody();
        System.out.println("Hasil Analisis dari Python: " + analysisResult);

        // Menyimpan hasil analisis pada objek transaksi
        transaksi.setFraud(analysisResult.equals("Fraud"));

        // Menyimpan transaksi ke database
        transaksiRepository.save(transaksi);

        // Menambahkan hasil analisis ke model untuk ditampilkan di dashboard
        model.addAttribute("transaksi", transaksi);
        model.addAttribute("hasilAnalisis", analysisResult);

        return "dashboard"; // Tampilkan dashboard dengan hasil analisis
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

        // Ambil transaksi terbaru yang terkait dengan pengguna
        Transaksi transaksi = transaksiRepository.findTopByUserOrderByDateDesc(user);

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
