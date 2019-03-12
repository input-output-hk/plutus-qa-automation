package io.iohk.frontEndTests;

import io.iohk.utils.Enums;
import io.iohk.utils.Log;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;


public class GeneralMethods extends BaseTest {
    private final String GETTING_STARTED_URL =
            "https://testnet.iohkdev.io/plutus/get-started/writing-contracts-in-plutus/";
    private final String TUTORIAL_URL =
            "https://github.com/input-output-hk/plutus/blob/4c1bc1ebfb927b064c01ee12b91609ab505c8212/wallet-api/tutorial/Tutorial.md";
    private final String API_URL =
            "https://input-output-hk.github.io/plutus/";
    private final String PRIVACY_URL =
            "https://static.iohk.io/docs/data-protection/iohk-data-protection-gdpr-policy.pdf";
    private final String WALLET_CURRENCY = "ADA";
    private final int WALLET_INIT_BALANCE = 10;
    private final List<String> crowdfundingWalletFunctions =
            Arrays.asList("scheduleCollection", "contribute", "payToPublicKey_");
    private final List<String> gameWalletFunctions =
            Arrays.asList("lock", "guess", "startGame", "payToPublicKey_");
    private final List<String> messagesWalletFunctions =
            Arrays.asList("logAMessage", "submitInvalidTxn", "throwWalletAPIError", "payToPublicKey_");
    private final List<String> vestingWalletFunctions =
            Arrays.asList("vestFunds", "registerVestingOwner", "payToPublicKey_");


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

    protected void evaluateSimulation() {
        Log.info("Evaluating the Simulation...");
        simulationPage.clickEvaluateBtn();
        simulationPage.waitEvaluateSuccess();
    }

    protected void checkDefaultContractValues(Enums.SmartContract smartContract) {
        switch (smartContract) {
            case CROWDFUNDING:
                checkDefaultCrowdfundingValues();
                break;
            case GAME:
                checkDefaultGameValues();
                break;
            case MESSAGES:
                checkDefaultMessagesValues();
                break;
            case VESTING:
                checkDefaultVestingValues();
                break;
        }
    }

    private void checkDefaultCrowdfundingValues() {
        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        Assert.assertEquals(walletTitlesList.size(), 2, "Unexpected number of configured wallets;");
        for (String walletTitle : walletTitlesList) {
            Assert.assertEquals(simulationPage.getWalletCurrency(walletTitle).trim(), WALLET_CURRENCY);
            Assert.assertEquals(simulationPage.getWalletInitialBalance(walletTitle), WALLET_INIT_BALANCE);
            List<String> walletFunctionsList = simulationPage.getWalletFunctionsList(walletTitle);
            Assert.assertTrue(simulationPage.complexCompareLists(crowdfundingWalletFunctions, walletFunctionsList));
        }
    }

    private void checkDefaultGameValues() {
        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        Assert.assertEquals(walletTitlesList.size(), 2, "Unexpected number of configured wallets;");
        for (String walletTitle : walletTitlesList) {
            Assert.assertEquals(simulationPage.getWalletCurrency(walletTitle).trim(), WALLET_CURRENCY);
            Assert.assertEquals(simulationPage.getWalletInitialBalance(walletTitle), WALLET_INIT_BALANCE);
            List<String> walletFunctionsList = simulationPage.getWalletFunctionsList(walletTitle);
            Assert.assertTrue(simulationPage.complexCompareLists(gameWalletFunctions, walletFunctionsList));
        }
    }

    private void checkDefaultMessagesValues() {
        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        Assert.assertEquals(walletTitlesList.size(), 2, "Unexpected number of configured wallets;");
        for (String walletTitle : walletTitlesList) {
            Assert.assertEquals(simulationPage.getWalletCurrency(walletTitle).trim(), WALLET_CURRENCY);
            Assert.assertEquals(simulationPage.getWalletInitialBalance(walletTitle), WALLET_INIT_BALANCE);
            List<String> walletFunctionsList = simulationPage.getWalletFunctionsList(walletTitle);
            Assert.assertTrue(simulationPage.complexCompareLists(messagesWalletFunctions, walletFunctionsList));
        }
    }

    private void checkDefaultVestingValues() {
        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        Assert.assertEquals(walletTitlesList.size(), 2, "Unexpected number of configured wallets;");
        for (String walletTitle : walletTitlesList) {
            Assert.assertEquals(simulationPage.getWalletCurrency(walletTitle).trim(), WALLET_CURRENCY);
            Assert.assertEquals(simulationPage.getWalletInitialBalance(walletTitle), WALLET_INIT_BALANCE);
            List<String> walletFunctionsList = simulationPage.getWalletFunctionsList(walletTitle);
            Assert.assertTrue(simulationPage.complexCompareLists(vestingWalletFunctions, walletFunctionsList));
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
        Assert.assertEquals(mainPage.getCurrentUrl(), GETTING_STARTED_URL);
        mainPage.closeSecondaryTabs();
    }

    private void checkTutorialLink() {
        mainPage.openTutorialLinkInNewTab();
        mainPage.waitForPageLoad();
        Assert.assertEquals(mainPage.getCurrentUrl(), TUTORIAL_URL);
        mainPage.closeSecondaryTabs();
    }

    private void checkApiLink() {
        mainPage.openApiLinkInNewTab();
        mainPage.waitForPageLoad();
        Assert.assertEquals(mainPage.getCurrentUrl(), API_URL);
        mainPage.closeSecondaryTabs();
    }

    private void checkPrivacyLink() {
        mainPage.openPrivacyLinkInNewTab();
        mainPage.waitForPageLoad();
        Assert.assertEquals(mainPage.getCurrentUrl(), PRIVACY_URL);
        mainPage.closeSecondaryTabs();
    }

}
