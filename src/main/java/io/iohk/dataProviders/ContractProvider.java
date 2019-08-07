package io.iohk.dataProviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.iohk.dataModels.*;
import io.iohk.utils.Enums;
import io.iohk.utils.Log;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static io.iohk.utils.FilesUtils.readJson;

public class ContractProvider{

    public static Contract readContractFromJson(String dataSourceName) throws Exception {
        String contractTitle = null;

        File f = getFileResource(dataSourceName);
        JsonNode rootJsonNode = readJson(f);

        Log.info("=================== Start Reading JSON values =======================");
        if (rootJsonNode != null) {
            Log.info("Reading Contract values from JSON file...");
            Contract contract = new Contract();

            Iterator<String> fieldNames = rootJsonNode.fieldNames();
            while (fieldNames.hasNext()) {
                contractTitle = fieldNames.next();
            }
            contract.setTitle(contractTitle);

            return readSimulationsFromJsonNode(rootJsonNode.path(contract.getTitle().toLowerCase()), contract);
        } else {
            Assert.fail("ERROR: Requested json file was not found");
        }

        return null;
    }

    private static Contract readSimulationsFromJsonNode(JsonNode contractNode, Contract contract) {
        Log.info(" Reading Simulations values from JSON file...");
        ArrayNode simulationsNode = (ArrayNode) contractNode.path("simulations");
        if (simulationsNode != null && simulationsNode.isArray()) {
            contract.setSimulationsList(readListOfSimulationsFromJson(simulationsNode));
        } else {
            Assert.fail("ERROR: simulationsNode is null or it is not defined as Array; - " + simulationsNode);
        }

        Log.info("=================== End Reading JSON values =======================");
        return contract;
    }

    private static List<Simulation> readListOfSimulationsFromJson(ArrayNode simulationsNode) {
        Log.info(" Reading List of Simulation values from JSON file...");
        List<Simulation> simulationsList = new ArrayList<>();

        int noOfCreatedSimulations = 1;
        for (JsonNode simulationNode : simulationsNode) {
            int addMultipleTimes = 1;
            // as default the simulation will be added only 1 time
            // if "addMultipleTimes" parameter is present inside the simulation json, the simulation will be created by that number of times
            if (simulationNode.hasNonNull("addMultipleTimes")) {
                addMultipleTimes = simulationNode.get("addMultipleTimes").asInt();
            }
            for (int i = 1; i <= addMultipleTimes; i++) {
                Simulation simulation = new Simulation();
                simulation.setTitle("Simulation #" + noOfCreatedSimulations);

                ArrayNode walletsNode = (ArrayNode) simulationNode.get("wallets");
                if (walletsNode != null && walletsNode.isArray()) {
                    simulation.setWalletsList(readListOfWalletsFromJson(walletsNode));
                } else {
                    Assert.fail("ERROR: walletsNode is null or it is not defined as Array; - " + simulationsNode);
                }

                ArrayNode actionsNode = (ArrayNode) simulationNode.get("actions");
                if (actionsNode != null && actionsNode.isArray()) {
                    simulation.setActionsList(readListOfActionsFromJson(actionsNode));
                } else {
                    Assert.fail("ERROR: actionsNode is null or it is not defined as Array; - " + simulationsNode);
                }

                simulationsList.add(simulation);
                noOfCreatedSimulations++;
            }
        }
        return simulationsList;
    }

    private static List<Wallet> readListOfWalletsFromJson(ArrayNode walletsNode) {
        Log.info(" Reading List of Wallet values from JSON file...");
        List<Wallet> walletsList = new ArrayList<>();

        int noOfCreatedWallets = 1;
        for (JsonNode walletNode : walletsNode) {
            int addMultipleTimes = 1;
            // as default the wallet will be added only 1 time
            // if "addMultipleTimes" parameter is present inside the wallet json, the wallet will be created by that number of times
            if (walletNode.hasNonNull("addMultipleTimes")) {
                addMultipleTimes = walletNode.get("addMultipleTimes").asInt();
            }
            for (int i = 1; i <= addMultipleTimes; i++) {
                Wallet wallet = new Wallet();
                wallet.setTitle("Wallet #" + noOfCreatedWallets);
                wallet.setCurrency(walletNode.get("currency").asText());
                wallet.setBalance(walletNode.get("balance").asInt());
                wallet.setFinalBalance(walletNode.get("finalBalance").asInt());

                ArrayNode availableFunctionsNode = (ArrayNode) walletNode.get("availableFunctions");
                if (availableFunctionsNode != null && availableFunctionsNode.isArray()) {
                    wallet.setAvailableFunctionsList(readListOfWalletFunctionsFromJson(availableFunctionsNode));
                } else {
                    Log.warn("WARNING: Wallet's availableFunctions node is not Array or it is null (it can be ok) - " + walletsNode);
                }

//                ArrayNode actionsNode = (ArrayNode) walletNode.get("actions");
//                if (actionsNode != null && actionsNode.isArray()) {
//                    wallet.setActionsList(createListOfActionsFromJson(actionsNode));
//                } else {
//                    Assert.fail("ERROR: Wallet's Actions node is not Array or it is null - " + actionsNode);
//                }

                walletsList.add(wallet);
                noOfCreatedWallets ++;
            }
        }
        return walletsList;
    }

    private static List<WalletFunction> readListOfWalletFunctionsFromJson(ArrayNode availableFunctionsNode) {
        Log.info(" Reading List of Wallet Available function values from JSON file...");

        List<WalletFunction> walletFunctionsList = new ArrayList<>();
        for (JsonNode functionNode : availableFunctionsNode) {
            walletFunctionsList.add(new WalletFunction(functionNode.asText()));
        }

        return walletFunctionsList;
    }

    private static List<Action> readListOfActionsFromJson(ArrayNode actionsNode) {
        Log.info(" Reading List of Action values from JSON file...");
        List<Action> actionsList = new ArrayList<>();


        for (JsonNode actionNode : actionsNode) {
            int addMultipleTimes = 1;
            // as default the action will be added only 1 time
            // if "addMultipleTimes" parameter is present inside the action json, the action will be created by that number of times
            if (actionNode.hasNonNull("addMultipleTimes")) {
                addMultipleTimes = actionNode.get("addMultipleTimes").asInt();
            }

            for (int i = 1; i <= addMultipleTimes; i++) {
                Action action = new Action();
                action.setTitle(actionNode.get("title").asText());
                if (!action.getTitle().contains("wait")) {
                    action.setWalletNumber(actionNode.get("walletNo").asInt());
                }

                if (actionNode.has("expectedError")) {
                    action.setExpectedError(actionNode.get("expectedError").asText());
                }

                JsonNode actionParametersNode = actionNode.path("parameters");
                if (actionParametersNode != null) {
                    action.setActionParametersList(readListOfActionParametersFromJson(actionParametersNode));
                } else {
                    Log.warn("WARNING: Action parameters node is not Array or it is null (it can be ok) - " + actionParametersNode);
                }
                actionsList.add(action);
            }
        }
        return actionsList;
    }

    private static List<ActionParameter> readListOfActionParametersFromJson(JsonNode actionParametersNode) {
        Log.info(" Reading List of Action parameter values from JSON file...");
        List<ActionParameter> actionParametersList = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> paramIterator = actionParametersNode.fields();
        while (paramIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = paramIterator.next();
            ActionParameter actionParameter = new ActionParameter();
            actionParameter.setTitle(entry.getKey());
            actionParameter.setType(entry.getValue().get("type").asText());

            switch (actionParameter.getType()) {
                case "wait":
                    LinkedHashMap<String, String> waitParamMap = new LinkedHashMap<>();
                    waitParamMap.put(entry.getKey(), entry.getValue().get("value").asText());
                    actionParameter.setValue(Collections.singletonList(waitParamMap));
                    break;
                case "basic":
                    LinkedHashMap<String, String> valueParamMap = new LinkedHashMap<>();
                    valueParamMap.put(entry.getKey(), entry.getValue().get("value").asText());
                    actionParameter.setValue(Collections.singletonList(valueParamMap));
                    break;
                case "string":
                    LinkedHashMap<String, String> valueParamMap1 = new LinkedHashMap<>();
                    valueParamMap1.put(entry.getKey(), entry.getValue().get("value").asText());
                    actionParameter.setValue(Collections.singletonList(valueParamMap1));
                    break;
                case "multivalue":
                    List<LinkedHashMap<String, String>> multiValueList = new ArrayList<>();
                    ArrayNode valuesNode = (ArrayNode) entry.getValue().get("value");
                    if (valuesNode != null && valuesNode.isArray()) {
                        for (JsonNode multiValuesNode: valuesNode) {
                            LinkedHashMap<String, String> multiValueParamMap = new LinkedHashMap<>();
                            Iterator<Map.Entry<String, JsonNode>> multiValueIterator = multiValuesNode.fields();
                            while (multiValueIterator.hasNext()) {
                                Map.Entry<String, JsonNode> valueEntry = multiValueIterator.next();
                                multiValueParamMap.put(valueEntry.getKey(), valueEntry.getValue().asText());
                            }
                            multiValueList.add(multiValueParamMap);
                        }
                        actionParameter.setValue(multiValueList);
                    } else {
                        Log.warn("WARNING: Action's valuesNode is not Array or it is null (it can be ok) - " + valuesNode);
                    }
                    break;
            }
            actionParametersList.add(actionParameter);
        }
        return actionParametersList;
    }

    private static File getFileResource(String resourcePath) {
        File outFile = null;

        try {
            InputStream fInputStream = ContractProvider.class.getResourceAsStream(resourcePath);
            outFile = File.createTempFile("upload", ".json");
            outFile.deleteOnExit();
            IOUtils.copy( fInputStream, new FileOutputStream(outFile));
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
        return outFile;
    }
}
