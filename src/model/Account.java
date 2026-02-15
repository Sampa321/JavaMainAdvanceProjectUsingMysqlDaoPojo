package model;

import java.time.LocalDate;

public class Account {
    private long accountNumber;
    private int customerID;
    private String accountType;
    private double balance;
    private String status;
    private LocalDate openingDate;

    public Account(long accountNumber, int customerID, String accountType, double balance, String status, LocalDate openingDate) {
        this.accountNumber = accountNumber;
        this.customerID = customerID;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.openingDate = openingDate;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
}
