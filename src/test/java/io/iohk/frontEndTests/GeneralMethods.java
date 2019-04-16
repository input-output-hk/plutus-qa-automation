package io.iohk.frontEndTests;

import io.iohk.dataModels.*;
import io.iohk.utils.Constants;
import io.iohk.utils.Enums;
import io.iohk.utils.Log;
import org.testng.Assert;

import java.util.*;

import static io.iohk.dataProviders.DefaultContractOptionsProvider.readDefaultContractValuesFromJson;


public class GeneralMethods extends BaseTest {

    private final String GETTING_STARTED_URL =
            "https://testnet.iohkdev.io/plutus/get-started/writing-contracts-in-plutus/";
    private final String TUTORIAL_URL =
            "https://github.com/input-output-hk/plutus/tree/3785ed43fe66b59bb8ea3378c7942dfb0015e975/plutus-tutorial/tutorial/Tutorial";
    private final String API_URL =
            "https://input-output-hk.github.io/plutus/";
    private final String PRIVACY_URL =
            "https://static.iohk.io/docs/data-protection/iohk-data-protection-gdpr-policy.pdf";
    private final String WALLET_CURRENCY = "ADA";
    private final int WALLET_INIT_BALANCE = 10;

    private HashMap<String, Object> readDefaultContractOptionsFromJson(String contractName) throws Exception {
        return readDefaultContractValuesFromJson(contractName);
    }

    protected void compileSpecificSmartContract(Enums.SmartContract smartContractName) {
        Log.info("Compiling specific smart contract... - " + smartContractName);
        mainPage.clickEditorBtn();
        switch (smartContractName) {
            case CROWDFUNDING:
                editorPage.clickCrowdfundingBtn();
                break;
            case GAME:
                editorPage.clickGameBtn();
                break;
            case MESSAGES:
                editorPage.clickMessagesBtn();
                break;
            case VESTING:
                editorPage.clickVestingBtn();
                break;
        }
        editorPage.clickCompileBtn();
        editorPage.waitContractCompileSuccess();
    }

    private void evaluateSimulationAndWaitSuccess() {
        Log.info("Waiting for Simulation to be successfully evaluated...");
        simulationPage.clickEvaluateBtn();

        mainPage.waitABit(20000);

        simulationPage.waitEvaluateSuccess();
    }

    protected void checkDefaultTabValues() {
        Log.info("Checking the default tab values");
        checkDefaultSimulationTabValues();
        checkDefaultTransactionsTabValues();
        Log.info("The default tab messages are correctly displayed");
    }

    private void checkDefaultSimulationTabValues() {
        mainPage.clickbtnSimulationBtn();
        Assert.assertTrue(simulationPage.checkIfNoCompiledContractDisplayed(),
                "Error: 'No compiled message' is not displayed inside the Simulation tab.");
    }

    private void checkDefaultTransactionsTabValues() {
        mainPage.clickTransactionsBtn();
        Assert.assertTrue(transactionsPage.checkIfNoEvalueatedContractDisplayed(),
                "Error: 'No evaluated message' is not displayed inside the Transactions tab.");
    }

    protected void checkDefaultContractValues(Enums.SmartContract smartContract) throws Exception {
        checkDefaultWalletValues(smartContract);
        checkDefaultActionsValues(smartContract);
    }

    private void checkDefaultWalletValues(Enums.SmartContract smartContract) throws Exception {
        Log.info("--- Checking the default Wallet values ---");
        HashMap<String, Object> defaultContractValues = readDefaultContractOptionsFromJson(smartContract.toString());
        List<String> expectedWalletFunctions = new ArrayList<>(defaultContractValues.keySet());

        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        Assert.assertEquals(walletTitlesList.size(), 2, "Unexpected number of configured wallets;");
        for (String walletTitle : walletTitlesList) {
            Assert.assertEquals(simulationPage.getWalletCurrency(walletTitle).trim(), WALLET_CURRENCY,
                    "Wallet's currency is different than expected for wallet: " + walletTitle);
            Assert.assertEquals(simulationPage.getWalletBalance(walletTitle), WALLET_INIT_BALANCE,
                    "Wallet's initial balance is different than expected for wallet: " + walletTitle);
            List<String> walletFunctionsList = simulationPage.getWalletFunctionsByTitleList(walletTitle);
            Assert.assertTrue(simulationPage.complexCompareLists(expectedWalletFunctions, walletFunctionsList),
                    "Available functions are different than expected for wallet: " + walletTitle);
        }
    }

    private void checkDefaultActionsValues(Enums.SmartContract smartContract) throws Exception {
        HashMap<String, Object> defaultContractValues = readDefaultContractOptionsFromJson(smartContract.toString());

        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        for (String walletTitle : walletTitlesList) {
            List<String> walletFunctionsList = simulationPage.getWalletFunctionsByTitleList(walletTitle);
            for (String walletFunction : walletFunctionsList) {
                Log.info("  --- Checking the default Action values for Action: ---  " + walletFunction);
                simulationPage.createAction(walletTitle, walletFunction);
                int numberOfConfiguredActions = simulationPage.getActionTitlesList().size();
                Log.info("Getting the functions for Action number: " + numberOfConfiguredActions + " - " + walletFunction);
                List<String> actionFunctionsList = simulationPage.getActionByNumberFunctionsList(numberOfConfiguredActions);
                LinkedList<String> expectedFunctionsList = new LinkedList<>();

                if (smartContract == Enums.SmartContract.VESTING) {

                    LinkedList<String> partialFunctionsList = new LinkedList<>(Arrays.asList(defaultContractValues.get(walletFunction).
                            toString().
                            replaceAll("\"", "").
                            replaceAll("]", "").
                            replaceAll("\\[", "")
                            .split(",")));

//                     cred ca mai bine fac un test cu o clasa si cum sa tranform din json in acea clasa...

                    for (String element : partialFunctionsList) {
                        String el = element.
                                replaceAll("\\{", "").
                                replaceAll("}", "").
                                replaceAll("]", "").
                                replaceAll("\\[", "").
                                replaceAll("\"", "").
                                split(":")[0];

                        expectedFunctionsList.add(el);
                    }

                    System.out.println("actionFunctionsList: " + actionFunctionsList);
                    System.out.println("partialFunctionsList: " + partialFunctionsList);
                    System.out.println("expectedFunctionsList: " + expectedFunctionsList);
                    System.out.println("defaultContractValues.get(walletFunction): " + defaultContractValues.get(walletFunction));


                } else {
                    expectedFunctionsList = new LinkedList<>(Arrays.asList(defaultContractValues.get(walletFunction).
                            toString().
                            replaceAll("\"", "").
                            replaceAll("]", "").
                            replaceAll("\\[", "")
                            .split(",")));
                }

                if (expectedFunctionsList.size() == 1 && expectedFunctionsList.get(0).equals("")) {
                    expectedFunctionsList.remove(0);
                }

                Assert.assertTrue(simulationPage.complexCompareLists(expectedFunctionsList, actionFunctionsList),
                        "Action functions are different than expected.");
            }
        }
    }

    protected void createActionsForEachWalletFunction(Enums.SmartContract smartContract) {
        Log.info("Creating 1 Action for each function for all configured wallets");
        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        for (String walletTitle : walletTitlesList) {
            List<String> walletFunctionsList = simulationPage.getWalletFunctionsByNumberList((walletTitlesList.indexOf(walletTitle) + 1));
            for (String walletFunction : walletFunctionsList) {
                simulationPage.createAction(walletTitle, walletFunction);
            }
        }
    }

    protected void checkHeaderLinks(Enums.MainPageLinks link) {
        Log.info("Checking link... - " + link);
        mainPage.clickEditorBtn();
        switch (link) {
            case GETTING_STARTED:
                checkGettingStartedLink();
                break;
            case TUTORIAL:
                checkTutorialLink();
                break;
            case API:
                checkApiLink();
                break;
            case PRIVACY:
                checkPrivacyLink();
                break;
        }
    }

    private void checkGettingStartedLink() {
        mainPage.openGettingStartedLinkInNewTab();
        mainPage.waitForPageLoad();
        Assert.assertEquals(mainPage.getCurrentUrl(), GETTING_STARTED_URL,
                "The Getting Started URL from the main is different than expected;");
        mainPage.closeSecondaryTabs();
    }

    private void checkTutorialLink() {
        mainPage.openTutorialLinkInNewTab();
        mainPage.waitForPageLoad();
        Assert.assertEquals(mainPage.getCurrentUrl(), TUTORIAL_URL,
                "The Tutorial URL from the main is different than expected;");
        mainPage.closeSecondaryTabs();
    }

    private void checkApiLink() {
        mainPage.openApiLinkInNewTab();
        mainPage.waitForPageLoad();
        Assert.assertEquals(mainPage.getCurrentUrl(), API_URL,
                "The API URL from the main is different than expected;");
        mainPage.closeSecondaryTabs();
    }

    private void checkPrivacyLink() {
        mainPage.openPrivacyLinkInNewTab();
        mainPage.waitForPageLoad();
        Assert.assertEquals(mainPage.getCurrentUrl(), PRIVACY_URL,
                "The Privacy URL from the main page is different than expected;");
        mainPage.closeSecondaryTabs();
    }

    protected List<String> closeEvenWalletNumbers(List<String> walletTitlesList) {
        for (String walletTitle : walletTitlesList) {
            if ((Integer.parseInt(walletTitle.split("#")[1]) % 2) == 0) {
                simulationPage.closeWallet(walletTitle);
            }
        }
        return simulationPage.getWalletTitlesList();
    }

    protected List<String> checkWalletTitlesFormat(List<String> walletTitlesList) {
        // Wallet title format:  Wallet #1
        List<String> notMatchingWalletTitles = new ArrayList<>();
        for (String walletTitle : walletTitlesList) {
            if (!walletTitle.matches("^Wallet #[\\d]+$")) {
                notMatchingWalletTitles.add(walletTitle);
            }
        }
        return notMatchingWalletTitles;
    }

    protected List<String> checkActionTitlesFormat(List<String> actionTitlesList, List<String> walletFunctionsList) {
        // Action title format:  Wallet #1: scheduleCollection
        List<String> notMatchingActionTitles = new ArrayList<>();
        String actionTitlePattern = "^Wallet #[\\d]+:\\s?(\\b(" + String.join("|", walletFunctionsList) + ")\\b)$";
        for (String actionTitle : actionTitlesList) {
            if (!actionTitle.matches(actionTitlePattern)) {
                notMatchingActionTitles.add(actionTitle);
            }
        }
        return notMatchingActionTitles;
    }

    protected void signInToGithub() {
        Log.info("Signing In to GitHub");
        mainPage.clickGithubLoginBtn();
        githubLoginPage.setUsername(Constants.GITHUB_USER);
        githubLoginPage.setPassword(Constants.GITHUB_PASS);
        githubLoginPage.clickSignInBtn();
    }

    protected String publishGist() {
        Log.info("Publishing the Gist to Github...");
        mainPage.clickPublishGistBtn();
        mainPage.waitPublishGistToFinish();
        Log.info("Gist successfully published - " + mainPage.getGistId());
        return mainPage.getGistId();
    }

    protected void loadGistId(String gistId) {
        Log.info("Loading Gist ID - " + gistId);
        mainPage.setLoadGistId(gistId);
        mainPage.clickLoadGistBtn();
        mainPage.waitPublishGistToFinish();
    }

    protected void evaluateContractFromScenario(Contract contract) {
        Log.debug("Compile the Contract");
        compileSpecificSmartContract(Enums.SmartContract.valueOf(contract.getTitle()));

        Log.debug("Create the Contract from provided scenario - " + contract.getTitle());
        createContractFromScenario(contract);

        Log.debug("Check all configured values inside the Simulation Tab");
        checkSimulationTabValues(contract);

        Log.debug("Evaluate all the Simulations from the provided scenario");
        evaluateAllSimulations(contract.getSimulationsList());

//        Log.debug("Check the Transaction tab values for each simulation");
//        checkTransactionTabValues(contract);
    }

    private void evaluateAllSimulations(List<Simulation> simulationList) {
        Log.info("Evaluate each created Simulation");
        for (Simulation simulation : simulationList) {
            Log.info("  - Evaluating Simulation - " + simulation.getTitle());
            mainPage.clickbtnSimulationBtn();
            simulationPage.openSimulation(simulation.getTitle());
            evaluateSimulationAndWaitSuccess();
        }
    }

    private void createContractFromScenario(Contract contract) {
        Log.info("Create the Contract from the provided JSON Scenario");
        List<Simulation> simulationList = contract.getSimulationsList();
        createSimulations(simulationList);
    }

    private void createSimulations(List<Simulation> simulationList) {
        Log.info("Create the Contract Simulations from the provided JSON Scenario");
        int count = 1;
        for (Simulation simulation : simulationList) {
            Log.info("  - Creating Simulation - " + simulation.getTitle()); // starting with the second Simulation
            if (count > 1) {
                simulationPage.createAndOpenNewSimulation();
            }

            Log.info("Close all the available Wallets from - " + simulation.getTitle());
            simulationPage.closeAllWallets();

            List<Wallet> walletList = simulation.getWalletsList();
            createWallets(walletList);

            count++;
        }
    }

    private void createWallets(List<Wallet> walletList) {
        Log.info("Create the Simulation Wallets from the provided JSON Scenario");
        for (Wallet wallet : walletList) {
            Log.info("  -- Creating Wallet - " + wallet.getTitle());
            simulationPage.clickAddWalletBtn();
            simulationPage.setWalletBalance(wallet.getTitle(), wallet.getBalance());

            List<Action> actionList = wallet.getActionsList();
            createActions(wallet, actionList);
        }
    }

    private void createActions(Wallet wallet, List<Action> actionList) {
        Log.info("Create the Wallet Actions from the provided JSON Scenario");
        for (Action action : actionList) {
            Log.info("  --- Creating Action: " + wallet.getTitle() + ": " + action.getTitle());
            int noOfPreExistingActions = simulationPage.getActionTitlesList().size();
            simulationPage.createAction(wallet.getTitle(), action.getTitle());
            action.setNumber(noOfPreExistingActions + 1);

            List<ActionParameter> actionParameterList = action.getActionParametersList();
            fillActionParameters(action, actionParameterList);
        }
    }

    private void fillActionParameters(Action action, List<ActionParameter> actionParameterList) {
        Log.info("Fill the Action Parameters values from the provided JSON Scenario");
        for (ActionParameter parameter : actionParameterList) {
            switch (parameter.getType()) {
                case "basic":
                    simulationPage.fillBasicActionParameters(action.getNumber(), parameter.getTitle(),
                            parameter.getValue().get(0).values().toArray()[0].toString());
                    break;
                case "string":
                    simulationPage.fillStringActionParameters(action.getNumber(), parameter.getTitle(),
                            parameter.getValue().get(0).values().toArray()[0].toString());
                    break;
                case "multivalue":
                    int count2 = 1;
                    for (Map<String, String> valueMap: parameter.getValue()) {
                        Set<String> paramKeys = valueMap.keySet();
                        if (parameter.getTitle().equals("getValue")) {
                            simulationPage.addGetValue(action.getNumber());
                            simulationPage.fillGetValueParam(action.getNumber(), count2,1, valueMap.get("int1"));
                            simulationPage.fillGetValueParam(action.getNumber(), count2,2, valueMap.get("int2"));
                            count2 ++;
                        } else {
                            for (String secondParameter: paramKeys) {
                                simulationPage.fillMultiValueParam(action.getNumber(), parameter.getTitle(),
                                        secondParameter, valueMap.get(secondParameter));
                            }
                        }
                    }
                    break;
            }
        }
    }

    protected void checkSimulationTabValues(Contract contract) {
        Log.info("==================== Start Checking Simulation Tab values ========================");
        Log.info("Check all the configured values inside the Simulations tab");
        mainPage.clickbtnSimulationBtn();

        List<String> simulationTitlesList = new ArrayList<>();
        List<Simulation> simulationList = contract.getSimulationsList();
        for (Simulation simulation: simulationList) {
            simulationPage.openSimulation(simulation.getTitle());
            simulationTitlesList.add(simulation.getTitle());

            Log.info("  -- Checking the values for: " + simulation.getTitle());
            List<String> walletTitlesList = new ArrayList<>();
            for (Wallet wallet: simulation.getWalletsList()) {
                Log.info("  -- Checking the values for: " + wallet.getTitle());
                walletTitlesList.add(wallet.getTitle());

                Assert.assertEquals(wallet.getBalance(), simulationPage.getWalletBalance(wallet.getTitle()),
                        "ERROR: Wallet's balance is different than expected for wallet: " + wallet.getTitle());
                Assert.assertEquals(wallet.getCurrency().toLowerCase(), simulationPage.getWalletCurrency(wallet.getTitle()).toLowerCase(),
                        "ERROR: Wallet's currency is different than expected for wallet: " + wallet.getTitle());

                if (wallet.getAvailableFunctionsList() != null) {
                    List<String> walletFunctionsList = new ArrayList<>();
                    for (WalletFunction walletFunction : wallet.getAvailableFunctionsList()) {
                        walletFunctionsList.add(walletFunction.getTitle());
                    }
                    Assert.assertTrue(mainPage.complexCompareLists(walletFunctionsList, simulationPage.getWalletFunctionsByTitleList(wallet.getTitle())),
                            "ERROR: Wallet's available functions are different than expected for wallet: " + wallet.getTitle());

                    List<String> actionTitlesList = simulationPage.getActionTitlesList();
                    List<String> notMatchingActionTitles = checkActionTitlesFormat(actionTitlesList, walletFunctionsList);
                    Assert.assertEquals(notMatchingActionTitles.size(), 0,
                            "ERROR: Some Action titles does not have the expected format - " + notMatchingActionTitles);
                }

                for (Action action: wallet.getActionsList()) {
                    Log.info("  --- Checking the values for Action Number: " + action.getNumber() + " - " + action.getTitle());
                    Assert.assertEquals(simulationPage.getActionTitleByNumber(action.getNumber()), wallet.getTitle() + ": " + action.getTitle(),
                            "ERROR: Action title is different than expected");

                    for (ActionParameter parameter: action.getActionParametersList()) {
                        Log.info("    - Checking the value of parameter: " + parameter.getTitle());
                        switch (parameter.getType()) {
                            case "basic":
                                Assert.assertEquals(
                                    simulationPage.getActionBasicParameterValue(action.getNumber(), parameter.getTitle()),
                                    parameter.getValue().get(0).values().toArray()[0].toString(),
                                    "ERROR: Parameter - " + parameter.getTitle() + " - has a different value than expected");
                                break;
                            case "string":
                                Assert.assertEquals(parameter.getValue().get(0).get("inputString"),
                                        simulationPage.getActionStringParameterValue(action.getNumber()),
                                        "ERROR: Parameter - " + parameter.getTitle() + " - has a different value than expected");
                                break;
                            case "multivalue":
                                int count = 0;
                                if (parameter.getTitle().equals("getValue")) {
                                    for (LinkedHashMap<String, String> rowValuesMap : parameter.getValue()) {
                                        Assert.assertTrue(mainPage.compareLinkedHashMaps(
                                                rowValuesMap,
                                                simulationPage.getActionGetValueParameterValue(action.getNumber()).get(count)),
                                                "ERROR: Parameter - " + parameter.getTitle() + " - has a different value than expected");
                                        count++;
                                    }
                                } else {
                                    Assert.assertTrue(mainPage.compareLinkedHashMaps(
                                            parameter.getValue().get(0),
                                            simulationPage.getActionMultiValueParameterValue(action.getNumber(), parameter.getTitle())),
                                            "ERROR: Parameter - " + parameter.getTitle() + " - has a different value than expected");
                                }
                                break;
                        }
                    }
                }
            }
            Assert.assertTrue(mainPage.complexCompareLists(walletTitlesList, simulationPage.getWalletTitlesList()),
                    "ERROR: The configured Simulations have different names than expected");
        }
        Assert.assertTrue(mainPage.complexCompareLists(simulationTitlesList, simulationPage.getSimulationTitlesList()),
                "ERROR: The configured Simulations have different names than expected");

        Log.info("==================== End Checking Simulation Tab values ========================");
    }
}