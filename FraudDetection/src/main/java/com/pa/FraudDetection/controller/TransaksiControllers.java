package com.pa.FraudDetection.controller;

import com.pa.FraudDetection.repository.TransaksiRepository;
import com.pa.FraudDetection.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransaksiControllers {

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/pilihInput")
    public String pilihInput() {
        // Menampilkan halaman pilihInput.html
        return "pilihan";
    }

    // Menangani pilihan metode input
    @PostMapping("/pilihInput")
    public String handlePilihInput(String inputMethod, HttpSession session) {
        // Menyimpan metode input yang dipilih dalam session
        session.setAttribute("inputMethod", inputMethod);

        // Mengarahkan ke halaman yang sesuai berdasarkan pilihan input
        if ("manual".equals(inputMethod)) {
            return "redirect:/inputManual";  // Halaman untuk input manual
        } else if ("upload".equals(inputMethod)) {
            return "redirect:/inputFile";  // Halaman untuk upload file
        }
        return "pilihan";  // Kembali ke halaman pemilihan jika tidak ada pilihan yang dipilih
    }

    @GetMapping("/inputManual")
    public String showInputManualForm() {
        return "inputManual";  // Menampilkan halaman input manual
    }


    // Menampilkan halaman form upload file
    @GetMapping("/inputFile")
    public String showInputFileForm() {
        return "inputFile";  // Menampilkan halaman upload file
    }
}