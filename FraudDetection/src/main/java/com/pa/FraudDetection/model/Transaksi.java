package com.pa.FraudDetection.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int dayOfWeek;
    private int typeOfCard;
    private int entryMode;
    private BigDecimal amount;
    private int typeOfTransaction;
    private int countryOfTransaction;
    private int gender;
    private int bank;
    private boolean fraud;

    public Transaksi() {}

    public Transaksi(User user, int dayOfWeek, int typeOfCard, int entryMode, BigDecimal amount,
                     int typeOfTransaction, int countryOfTransaction, int gender, int bank, boolean fraud) {
        this.user = user;
        this.dayOfWeek = dayOfWeek;
        this.typeOfCard = typeOfCard;
        this.entryMode = entryMode;
        this.amount = amount;
        this.typeOfTransaction = typeOfTransaction;
        this.countryOfTransaction = countryOfTransaction;
        this.gender = gender;
        this.bank = bank;
        this.fraud = fraud;
    }

    // Getter and Setter for user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getter and Setter for dayOfWeek
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    // Getter and Setter for amount
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    // Getter and Setter for fraud
    public boolean isFraud() {
        return fraud;
    }

    public void setFraud(boolean fraud) {
        this.fraud = fraud;
    }
}
