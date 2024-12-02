package com.pa.FraudDetection.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Relasi ke tabel users

    private BigDecimal amount;
    private String typeOfCard;
    private String entryMode;
    private String transactionType;
    private String countryOfTransaction;
    private String gender;
    private String bank;
    private LocalDate date;
    private String dayOfWeek;
    private boolean fraud;

    // Constructor
    public Transaksi() {}

    public Transaksi(User user, BigDecimal amount, String typeOfCard, String entryMode, String transactionType,
                     String countryOfTransaction, String gender, String bank, LocalDate date, String dayOfWeek, boolean fraud) {
        this.user = user;
        this.amount = amount;
        this.typeOfCard = typeOfCard;
        this.entryMode = entryMode;
        this.transactionType = transactionType;
        this.countryOfTransaction = countryOfTransaction;
        this.gender = gender;
        this.bank = bank;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.fraud = fraud;
    }

    // Getter and Setter for transactionId
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    // Getter and Setter for user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getter and Setter for amount
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Getter and Setter for typeOfCard
    public String getTypeOfCard() {
        return typeOfCard;
    }

    public void setTypeOfCard(String typeOfCard) {
        this.typeOfCard = typeOfCard;
    }

    // Getter and Setter for entryMode
    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    // Getter and Setter for transactionType
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    // Getter and Setter for countryOfTransaction
    public String getCountryOfTransaction() {
        return countryOfTransaction;
    }

    public void setCountryOfTransaction(String countryOfTransaction) {
        this.countryOfTransaction = countryOfTransaction;
    }

    // Getter and Setter for gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter and Setter for bank
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    // Getter and Setter for date
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Getter and Setter for dayOfWeek
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    // Getter and Setter for fraud
    public boolean isFraud() {
        return fraud;
    }

    public void setFraud(boolean fraud) {
        this.fraud = fraud;
    }
}
