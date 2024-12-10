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
    private Long transaction_id; // Ubah ke snake_case

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal amount; // Ubah ke sesuai dengan nama di app.py
    private int type_of_card; // Ubah ke sesuai dengan nama di app.py
    private int entry_mode; // Ubah ke sesuai dengan nama di app.py
    private int type_of_transaction; // Ubah ke sesuai dengan nama di app.py
    private int country_of_transaction; // Ubah ke sesuai dengan nama di app.py
    private int gender; // Ubah ke sesuai dengan nama di app.py
    private int bank; // Ubah ke sesuai dengan nama di app.py
    private int day_of_week; // Ubah ke sesuai dengan nama di app.py
    private Boolean fraud;

    public Transaksi() {}

    public Transaksi(User user, BigDecimal amount, int type_of_card, int entry_mode, int type_of_transaction,
                     int country_of_transaction, int gender, int bank, int day_of_week, Boolean fraud) {
        this.user = user;
        this.amount = amount;
        this.type_of_card = type_of_card;
        this.entry_mode = entry_mode;
        this.type_of_transaction = type_of_transaction;
        this.country_of_transaction = country_of_transaction;
        this.gender = gender;
        this.bank = bank;
        this.day_of_week = day_of_week;
        this.fraud = fraud;
    }

    // Getter and Setter for transactionId
    public Long getTransactionId() {
        return transaction_id;
    }

    public void setTransactionId(Long transaction_id) {
        this.transaction_id = transaction_id;
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
        return type_of_card;
    }

    public void setTypeOfCard(int type_of_card) {
        this.type_of_card = type_of_card;
    }

    // Getter and Setter for entryMode
    public int getEntryMode() {
        return entry_mode;
    }

    public void setEntryMode(int entry_mode) {
        this.entry_mode = entry_mode;
    }

    // Getter and Setter for typeOfTransaction
    public int getTypeOfTransaction() {
        return type_of_transaction;
    }

    public void setTypeOfTransaction(int type_of_transaction) {
        this.type_of_transaction = type_of_transaction;
    }

    // Getter and Setter for countryOfTransaction
    public int getCountryOfTransaction() {
        return country_of_transaction;
    }

    public void setCountryOfTransaction(int country_of_transaction) {
        this.country_of_transaction = country_of_transaction;
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
        return day_of_week;
    }

    public void setDayOfWeek(int day_of_week) {
        this.day_of_week = day_of_week;
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
