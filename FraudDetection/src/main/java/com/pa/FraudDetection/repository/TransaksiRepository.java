package com.pa.FraudDetection.repository;

import com.pa.FraudDetection.model.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {

}
