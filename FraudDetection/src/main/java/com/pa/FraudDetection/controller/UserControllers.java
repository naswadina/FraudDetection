package com.pa.FraudDetection.controller;

import com.pa.FraudDetection.model.User;
import com.pa.FraudDetection.repository.TransaksiRepository;
import com.pa.FraudDetection.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserControllers {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/login")
    public String loginPage() {
        // Menampilkan halaman login jika diakses dengan metode GET
        return "login";
    }
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username, // Pastikan nama sesuai dengan form
            @RequestParam("email") String email,
            Model model) {

        // Log parameter untuk debugging
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        // Simpan ke database
        userRepository.save(user);

        return "redirect:/pilihInput"; // Redirect ke halaman pilih input
    }
}
