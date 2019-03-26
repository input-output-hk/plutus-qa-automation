package io.iohk.dataModels;

import io.iohk.utils.Log;

import java.util.List;

public class Contract {
    private String title;
    private List<Simulation> simulationsList = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.info("  - Reading Contract title as: " + title);
        this.title = title;
    }

    public List<Simulation> getSimulationsList() {
        return simulationsList;
    }

    public void setSimulationsList(List<Simulation> simulationsList) {
        Log.info("  - Reading Contract simulationsList as: " + simulationsList);
        this.simulationsList = simulationsList;
    }
}
