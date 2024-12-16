package com.pa.FraudDetection.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa.FraudDetection.model.Transaksi;
import com.pa.FraudDetection.model.User;
import com.pa.FraudDetection.repository.TransaksiRepository;
import com.pa.FraudDetection.repository.UserRepository;
import com.pa.FraudDetection.service.TransaksiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class TransaksiControllers {

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransaksiService transaksiService;

    @GetMapping("/inputManual")
    public String showInputManualForm(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        if (username == null || email == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        model.addAttribute("email", email);
        return "inputManual";
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
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");
        if (username == null || email == null) {
            return "redirect:/login";
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", "Amount should be greater than zero");
            return "inputManual";
        }

        try {
            List<User> users = userRepository.findByUsername(username);
            Transaksi transaksi = new Transaksi(
                    users.get(0), dayOfWeek, typeOfCard, entryMode, amount, typeOfTransaction,
                    countryOfTransaction, gender, bank
            );

            // Panggil service untuk analisis transaksi
            String analysisResult = transaksiService.analisisTransaksi(transaksi);
            System.out.println("Hasil Analisis dari Python: " + analysisResult);

            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseJson = responseMapper.readTree(analysisResult);
            String fraudResult = responseJson.get("result").asText();

            if ("fraud".equalsIgnoreCase(fraudResult)) {
                transaksi.setFraud(true);
            } else {
                transaksi.setFraud(false);
            }

            transaksiRepository.save(transaksi);

            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("amount", amount);
            model.addAttribute("typeOfCard", getCardType(typeOfCard));
            model.addAttribute("entryMode", getEntryMode(entryMode));
            model.addAttribute("transactionType", getTransactionType(typeOfTransaction));
            model.addAttribute("country", getCountry(countryOfTransaction));
            model.addAttribute("gender", getGender(gender));
            model.addAttribute("bank", getBank(bank));
            model.addAttribute("dayOfWeek", getDayOfWeek(dayOfWeek));
            model.addAttribute("hasilAnalisis", fraudResult);

            return "Dashboard";

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
        String fraudResult = (String) session.getAttribute("hasilAnalisis");

        // Pastikan nilai tidak null agar tidak menyebabkan error
        if (fraudResult != null) {
            model.addAttribute("hasilAnalisis", fraudResult);
        } else {
            model.addAttribute("hasilAnalisis", "Tidak ada analisis yang tersedia");
        }

        return "Dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "logout";
    }
}