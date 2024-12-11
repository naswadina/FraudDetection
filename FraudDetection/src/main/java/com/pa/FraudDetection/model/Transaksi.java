package com.pa.FraudDetection.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId; // Ubah ke camelCase

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal amount;
    private int typeOfCard;
    private int entryMode;
    private int typeOfTransaction;
    private int countryOfTransaction;
    private int gender;
    private int bank;
    private int dayOfWeek;
    private Boolean fraud;

    public Transaksi() {}

    public Transaksi(User user, BigDecimal amount, int typeOfCard, int entryMode, int typeOfTransaction,
                     int countryOfTransaction, int gender, int bank, int dayOfWeek, Boolean fraud) {
        this.user = user;
        this.amount = amount;
        this.typeOfCard = typeOfCard;
        this.entryMode = entryMode;
        this.typeOfTransaction = typeOfTransaction;
        this.countryOfTransaction = countryOfTransaction;
        this.gender = gender;
        this.bank = bank;
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

    // Getter and Setter for typeOfTransaction
    public int getTypeOfTransaction() {
        return typeOfTransaction;
    }

    public void setTypeOfTransaction(int typeOfTransaction) {
        this.typeOfTransaction = typeOfTransaction;
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

    // Getter and Setter for dayOfWeek
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    // Getter and Setter for fraud
    public Boolean getFraud() {
        return fraud;
    }

    public void setFraud(Boolean fraud) {
        this.fraud = fraud;
    }
}