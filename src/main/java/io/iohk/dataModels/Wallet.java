package io.iohk.dataModels;

import io.iohk.utils.Log;

import java.util.List;

public class Wallet {
    private String title;
    private String currency;
    private int balance;
    private int finalBalance;
    private List<WalletFunction> availableFunctionsList = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.info("  - Reading Wallet title as: " + title);
        this.title = title;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        Log.info("  - Reading wallet currency as: " + currency);
        this.currency = currency;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        Log.info("  - Reading wallet balance as: " + balance);
        this.balance = balance;
    }

    public int getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(int finalBalance) {
        this.finalBalance = finalBalance;
    }

    public List<WalletFunction> getAvailableFunctionsList() {
        return availableFunctionsList;
    }

    public void setAvailableFunctionsList(List<WalletFunction> availableFunctionsList) {
        Log.info("  - Reading wallet availableFunctionsList as: " + availableFunctionsList);
        this.availableFunctionsList = availableFunctionsList;
    }
}
