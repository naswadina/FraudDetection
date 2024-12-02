package com.pa.FraudDetection.repository;

import com.pa.FraudDetection.model.Transaksi;
import com.pa.FraudDetection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    Transaksi findTopByUserOrderByDateDesc(User user);  // Menyesuaikan dengan field `date`
}



