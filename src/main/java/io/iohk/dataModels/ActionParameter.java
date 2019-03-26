package io.iohk.dataModels;

import io.iohk.utils.Log;

import java.util.HashMap;
import java.util.List;

public class ActionParameter {
    private String title;
    private List<HashMap<String, String>> value;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.info("  - Reading Action Parameter title as: " + title);
        this.title = title;
    }

    public List<HashMap<String, String>> getValue() {
        return value;
    }

    public void setValue(List<HashMap<String, String>> value) {
        Log.info("  - Reading Action Parameter value as: " + value);
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        Log.info("  - Reading Action Parameter type as: " + type);
        this.type = type;
    }


}
