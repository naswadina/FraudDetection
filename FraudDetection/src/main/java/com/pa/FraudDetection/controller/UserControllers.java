package com.pa.FraudDetection.controller;

import com.pa.FraudDetection.model.User;
import com.pa.FraudDetection.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

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
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            Model model,
            HttpSession session) {  // Tambahkan HttpSession untuk menyimpan session

        // Log parameter untuk debugging
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);

        // Simpan user ke database
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);

        // Simpan username dan email dalam session
        session.setAttribute("username", username);
        session.setAttribute("email", email);

        return "redirect:/inputManual"; // Redirect ke halaman input manual
    }
}
