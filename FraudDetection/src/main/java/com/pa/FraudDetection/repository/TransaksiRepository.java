package com.pa.FraudDetection.repository;

import com.pa.FraudDetection.model.Transaksi;
import com.pa.FraudDetection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    List<Transaksi> findByUser(User user);
}




