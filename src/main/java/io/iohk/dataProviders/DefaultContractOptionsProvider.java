package io.iohk.dataProviders;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

import static io.iohk.utils.FilesUtils.readJson2;


public class DefaultContractOptionsProvider {

    public static HashMap<String, Object> readDefaultContractValuesFromJson(String contractName) throws Exception {
        HashMap<String, Object> actionFunctionsList = new HashMap<>();

        JsonNode rootJsonNode = readJson2("src/test/resources/jsons/DefaultContractOptionsBk.json");
        JsonNode actionFunctionsNode = rootJsonNode.get(contractName.toLowerCase()).get("action_functions");

        Iterator<Map.Entry<String, JsonNode>> nodes = actionFunctionsNode.fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            actionFunctionsList.put(entry.getKey(), entry.getValue());
        }
        return actionFunctionsList;
    }

}
