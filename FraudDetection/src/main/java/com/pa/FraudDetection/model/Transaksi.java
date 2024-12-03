package com.pa.FraudDetection.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal amount;
    private int typeOfCard;  // Ganti tipe menjadi int
    private int entryMode;   // Ganti tipe menjadi int
    private int transactionType;  // Ganti tipe menjadi int
    private int countryOfTransaction;  // Ganti tipe menjadi int
    private int gender;  // Ganti tipe menjadi int
    private int bank;  // Ganti tipe menjadi int
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private int dayOfWeek;
    private boolean fraud;

    // Konstruktor tanpa String pada tipe yang seharusnya int
    public Transaksi() {}

    public Transaksi(User user, BigDecimal amount, int typeOfCard, int entryMode, int transactionType,
                     int countryOfTransaction, int gender, int bank, Date date, int dayOfWeek, boolean fraud) {
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
    public int getTypeOfCard() {
        return typeOfCard;
    }

    public void setTypeOfCard(int typeOfCard) {
        this.typeOfCard = typeOfCard;
    }

    // Getter and Setter for entryMode
    public int getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(int entryMode) {
        this.entryMode = entryMode;
    }

    // Getter and Setter for transactionType
    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    // Getter and Setter for countryOfTransaction
    public int getCountryOfTransaction() {
        return countryOfTransaction;
    }

    public void setCountryOfTransaction(int countryOfTransaction) {
        this.countryOfTransaction = countryOfTransaction;
    }

    // Getter and Setter for gender
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    // Getter and Setter for bank
    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    // Getter and Setter for date
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Getter and Setter for dayOfWeek
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    // Getter and Setter for fraud
    public boolean isFraud() {
        return fraud;
    }

    public void setFraud(boolean fraud) {
        this.fraud = fraud;
    }

    public boolean getFraud() {
        return fraud;
    }
}

