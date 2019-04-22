package io.iohk.dataModels;

import io.iohk.utils.Log;

import java.util.List;

public class Simulation {
    private String title;
    private List<Wallet> walletsList = null;
    private List<Action> actionsList = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.info("  - Reading Simulation title as: " + title);
        this.title = title;
    }

    public List<Wallet> getWalletsList() {
        return walletsList;
    }

    public void setWalletsList(List<Wallet> walletsList) {
        Log.info("  - Reading Simulation walletList as: " + walletsList);
        this.walletsList = walletsList;
    }

    public List<Action> getActionsList() {
        return actionsList;
    }

    public void setActionsList(List<Action> actionsList) {
        this.actionsList = actionsList;
    }
}
