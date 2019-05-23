package io.iohk.frontEndTests;

import io.iohk.dataModels.*;
import io.iohk.utils.Constants;
import io.iohk.utils.Enums;
import io.iohk.utils.Log;
import org.testng.Assert;

import java.util.*;

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

    protected void createActionsForEachWalletFunction(Enums.SmartContract smartContract) {
        Log.info("Creating 1 Action for each function for all configured wallets");
        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        for (String walletTitle : walletTitlesList) {
            List<String> walletFunctionsList = simulationPage.getWalletFunctionsByNumberList((walletTitlesList.indexOf(walletTitle) + 1));
            for (String walletFunction : walletFunctionsList) {
                int walletNumber = Integer.parseInt(walletTitle.split("#")[1]);
                simulationPage.createAction(walletNumber, walletFunction);
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
        compileSpecificSmartContract(Enums.SmartContract.valueOf(contract.getTitle().toUpperCase()));

        Log.debug("Create the Contract from provided scenario - " + contract.getTitle());
        createContractFromScenario(contract);

        Log.debug("Check all configured values inside the Simulation Tab (for all Simulations)");
        checkSimulationTabValues(contract);

        Log.debug("Evaluate each Simulation from the provided scenario and check the Transaction tab values");
        for (Simulation simulation : contract.getSimulationsList()) {
            evaluateSimulation(simulation);

            List<String> expectedErrors = new ArrayList<>();
            for (Action action: simulation.getActionsList()) {
                if (action.getExpectedError() != null) {
                    expectedErrors.add(action.getExpectedError());
                }
            }
            checkTransactionTabValues(expectedErrors, simulation);
        }
    }

    private void checkTransactionsTabErrors(List<String> expectedErrors) {
        Log.info("Checking for unexpected errors inside the Transactions tab");
        List<String> existingErrors = transactionsPage.getErrorsList();
        Log.info("  - Expected errors: " + expectedErrors.size());
        Log.info("  - Existing errors: " + existingErrors.size());

        List<String> unexpectedErrors = new ArrayList<>();
        for (String error: existingErrors) {
            boolean flag = false;
            for (String expectedError: expectedErrors) {
                if (error.contains(expectedError)) {
                    flag = true;
                }
            }
            if (!flag) {
                unexpectedErrors.add(error);
            }
        }

        Assert.assertEquals(unexpectedErrors.size(), 0,
                "ERROR: There are " + unexpectedErrors.size() + " unexpected errors on the Transaction page: " +
                        unexpectedErrors);

        Assert.assertTrue(existingErrors.size() >= expectedErrors.size(),
                "The number of existing errors is smaller than the expected one --> Exected: " +
                        expectedErrors.size() + " VS Existing: " + existingErrors.size());
    }

    private void checkTransactionsTabFinalBalances(Simulation simulation) {
        Log.info("Checking the Final Balances inside the Transactions tab");
        LinkedList<String> walletTitlesFromFinalBalances = transactionsPage.getWalletTitlesFromFinalBalances();
        for (String walletName: walletTitlesFromFinalBalances) {

            int expectedWalletBalance = getWalletFinalBalanceFromJson(simulation, walletName);
            int returnedWalletBalance = transactionsPage.getWalletFinalBalance(walletName, walletTitlesFromFinalBalances);

            Assert.assertEquals(returnedWalletBalance, expectedWalletBalance,
                    "Balance for " + walletName + " is different than expected; Expected: " +
                            expectedWalletBalance + " vs Returned (UI): " + returnedWalletBalance
            );
        }
    }

    private void checkTransactionTabValues(List<String> expectedErrors, Simulation simulation) {
        Log.info("==================== Start Checking Transactions Tab values ========================");

        checkTransactionsTabErrors(expectedErrors);
        checkTransactionsTabFinalBalances(simulation);

        Log.info("==================== End Checking Transactions Tab values ========================");
    }

    private void evaluateSimulation(Simulation simulation) {
        Log.info(" Evaluating Simulation - " + simulation.getTitle());
        mainPage.clickbtnSimulationBtn();
        simulationPage.openSimulation(simulation.getTitle());
        evaluateSimulationAndWaitSuccess();
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

            List<Action> actionsList = simulation.getActionsList();
            createActions(actionsList);

            count++;
        }
    }

    private void createWallets(List<Wallet> walletList) {
        Log.info("Create the Simulation Wallets from the provided JSON Scenario");
        for (Wallet wallet : walletList) {
            Log.info("  -- Creating Wallet - " + wallet.getTitle());
            simulationPage.clickAddWalletBtn();
            simulationPage.setWalletBalance(wallet.getTitle(), wallet.getBalance());
        }
    }

    private void createActions(List<Action> actionList) {
        Log.info("Create the Actions from the provided JSON Scenario");
        for (Action action : actionList) {
            Log.info("  -- Creating Action: Wallet #" + action.getWalletNumber() + ": " + action.getTitle());
            int noOfPreExistingActions = simulationPage.getActionTitlesList().size();
            simulationPage.createAction(action.getWalletNumber(), action.getTitle());
            action.setActionNumber(noOfPreExistingActions + 1);

            List<ActionParameter> actionParameterList = action.getActionParametersList();
            fillActionParameters(action, actionParameterList);
        }
    }

    private void fillActionParameters(Action action, List<ActionParameter> actionParameterList) {
        Log.info("Fill the Action Parameters values from the provided JSON Scenario");
        for (ActionParameter parameter : actionParameterList) {
            switch (parameter.getType()) {
                case "wait":
                    simulationPage.fillWaitActionParameters(action.getActionNumber(), parameter.getTitle(),
                            parameter.getValue().get(0).values().toArray()[0].toString());
                    break;
                case "basic":
                    simulationPage.fillBasicActionParameters(action.getActionNumber(), parameter.getTitle(),
                            parameter.getValue().get(0).values().toArray()[0].toString());
                    break;
                case "string":
                    simulationPage.fillStringActionParameters(action.getActionNumber(), parameter.getTitle(),
                            parameter.getValue().get(0).values().toArray()[0].toString());
                    break;
                case "multivalue":
                    int count2 = 1;
                    for (Map<String, String> valueMap: parameter.getValue()) {
                        Set<String> paramKeys = valueMap.keySet();
                        if (parameter.getTitle().equals("getValue")) {
                            simulationPage.addGetValue(action.getActionNumber());
                            simulationPage.fillGetValueParam(action.getActionNumber(), count2,1, valueMap.get("int1"));
                            simulationPage.fillGetValueParam(action.getActionNumber(), count2,2, valueMap.get("int2"));
                            count2 ++;
                        } else {
                            for (String secondParameter: paramKeys) {
                                simulationPage.fillMultiValueParam(action.getActionNumber(), parameter.getTitle(),
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

        Assert.assertEquals(simulationPage.getSimulationTitlesList().size(), contract.getSimulationsList().size(),
                "ERROR: There is a different number of Simulations than expected inside the Simulations tab");

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
                }

            for (Action action: simulation.getActionsList()) {
                Log.info("  --- Checking the values for Action Number: " + action.getActionNumber() + " - " + action.getTitle());

                if (!action.getTitle().contains("wait")) {
                    Assert.assertEquals(simulationPage.getActionTitleByNumber(action.getActionNumber()),
                            "Wallet #" + action.getWalletNumber() + ": " + action.getTitle(),
                            "ERROR: Action title is different than expected");
                } else {
                    Assert.assertEquals(simulationPage.getActionTitleByNumber(action.getActionNumber()), "Wait",
                            "ERROR: Action title is different than expected");
                }

                for (ActionParameter parameter: action.getActionParametersList()) {
                    Log.info("    - Checking the value of parameter: " + parameter.getTitle());
                    switch (parameter.getType()) {
                        case "basic":
                            Assert.assertEquals(
                                simulationPage.getActionBasicParameterValue(action.getActionNumber(), parameter.getTitle()),
                                parameter.getValue().get(0).values().toArray()[0].toString(),
                                "ERROR: Parameter - " + parameter.getTitle() + " - has a different value than expected");
                            break;
                        case "string":
                            Assert.assertEquals(parameter.getValue().get(0).get("inputString"),
                                    simulationPage.getActionStringParameterValue(action.getActionNumber()),
                                    "ERROR: Parameter - " + parameter.getTitle() + " - has a different value than expected");
                            break;
                        case "multivalue":
                            int count = 0;
                            if (parameter.getTitle().equals("getValue")) {
                                for (LinkedHashMap<String, String> rowValuesMap : parameter.getValue()) {
                                    Assert.assertTrue(mainPage.compareLinkedHashMaps(
                                            rowValuesMap,
                                            simulationPage.getActionGetValueParameterValue(action.getActionNumber()).get(count)),
                                            "ERROR: Parameter - " + parameter.getTitle() + " - has a different value than expected");
                                    count++;
                                }
                            } else {
                                Assert.assertTrue(mainPage.compareLinkedHashMaps(
                                        parameter.getValue().get(0),
                                        simulationPage.getActionMultiValueParameterValue(action.getActionNumber(), parameter.getTitle())),
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

    private int getWalletBalanceFromJson(Contract contract, String walletTitle) {
        Log.info("  - Getting the (JSON) Balance value for: " + walletTitle);
        List<Wallet> walletList = contract.getSimulationsList().get(0).getWalletsList();
        for (Wallet wallet: walletList) {
            if (wallet.getTitle().equals(walletTitle)) {
                return wallet.getBalance();
            }
        }
        Log.error("Wallet title was not find in contract json - " + walletTitle);
        return 0;
    }

    private int getWalletFinalBalanceFromJson(Simulation simulation, String walletTitle) {
        Log.info("  - Getting the (JSON) Final Balance value for: " + walletTitle);
        List<Wallet> walletList = simulation.getWalletsList();
        for (Wallet wallet: walletList) {
            if (wallet.getTitle().equals(walletTitle)) {
                return wallet.getFinalBalance();
            }
        }
        Log.error("Wallet title was not find in contract json - " + walletTitle);
        return 0;
    }

    private String getActionParameterValueFromJson(Contract contract, int actionNumber, String parameterTitle) {
        Log.info("  - Getting the (JSON) value of parameter " + parameterTitle + " of Action No " + actionNumber);
        List<Action> actionsList = contract.getSimulationsList().get(0).getActionsList();
        for (Action action: actionsList) {
            if (action.getActionNumber() == actionNumber) {
                for (ActionParameter parameter : action.getActionParametersList()) {
                    if (parameter.getTitle().equals(parameterTitle)) {
                        return parameter.getValue().get(0).get("Value");
                    }
                }
            }
        }
        Log.error("Action Parameter was not found in contract json; Action number: " + actionNumber + "; Parameter: " + parameterTitle);
        return null;
    }




}