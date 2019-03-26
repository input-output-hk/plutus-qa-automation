package io.iohk.dataModels;

import io.iohk.utils.Log;

public class WalletFunction {
    private String title;

    public WalletFunction(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.info("  - Reading wallet available function as: " + title);
        this.title = title;
    }
}
