package io.iohk.dataModels;

import io.iohk.utils.Log;

import java.util.List;

public class Action {
    private String title;
    private int walletNumber = 0;
    private int actionNumber;
    private List<ActionParameter> actionParametersList = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.info("  - Set Action title to: " + title);
        this.title = title;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(int number) {
        Log.info("  - Set Action number to: " + number);
        this.actionNumber = number;
    }

    public int getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(int walletNumber) {
        this.walletNumber = walletNumber;
    }

    public List<ActionParameter> getActionParametersList() {
        return actionParametersList;
    }

    public void setActionParametersList(List<ActionParameter> actionParametersList) {
        Log.info("  - Set Action parameters list to: " + actionParametersList);
        this.actionParametersList = actionParametersList;
    }
}
