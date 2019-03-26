package io.iohk.dataModels;

import io.iohk.utils.Log;

import java.util.List;

public class Action {
    private String title;
    private int number;
    private List<ActionParameter> actionParametersList = null;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.info("  - Set Action title to: " + title);
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        Log.info("  - Set Action number to: " + number);
        this.number = number;
    }

    public List<ActionParameter> getActionParametersList() {
        return actionParametersList;
    }

    public void setActionParametersList(List<ActionParameter> actionParametersList) {
        Log.info("  - Set Action parameters list to: " + actionParametersList);
        this.actionParametersList = actionParametersList;
    }
}
