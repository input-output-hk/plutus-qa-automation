package io.iohk.frontEndTests.OtherTests;

import io.iohk.frontEndTests.GeneralMethods;
import io.iohk.utils.DriverManager;
import io.iohk.utils.Enums;
import io.iohk.utils.Log;
import io.iohk.utils.listeners.AnnotationTransformer;
import io.iohk.utils.listeners.ScreenshotListener;
import io.iohk.utils.listeners.TestListener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Listeners({ScreenshotListener.class, TestListener.class, AnnotationTransformer.class})
public class AddDeleteWalletsTest extends GeneralMethods {

    @DataProvider
    public Object[][] SmartContracts()  {
        List<Enums.SmartContract> enumValues = Arrays.asList(Enums.SmartContract.values());

        Object[][] data = new Object[enumValues.size()][1];

        int index = 0;
        for(Enums.SmartContract contract: enumValues){
            data[index][0] = contract;
            index ++;
        }

        return data;
    }

    @Test(dataProvider = "SmartContracts")
    public void addDeleteWallets(Enums.SmartContract smartContract) {
        // Test steps:
        //      1. Open Plutus Playground UI
        //      2. Compile each Demo smart contract and:
        //          2.1 check that you can add multiple wallets
        //          2.2 check that you can add multiple actions (for each function from each wallet)
        //          2.3 check that you can delete multiple wallets (all even numbers)
        //          2.4 check that the actions associated with the deleted wallets are also deleted

        int NUMBER_OF_TOTAL_WALLETS = 10;

        Log.info("Compile the smart contracts: " + smartContract);
        compileSpecificSmartContract(smartContract);

        Log.info("Create multiple Wallets");
        simulationPage.createMultipleWallets(NUMBER_OF_TOTAL_WALLETS);

        Log.info("Create Actions for each function from each Wallet");
        createActionsForEachWalletFunction(smartContract);

        Log.info("Check the number of created Wallets and Actions");
        List<String> walletTitlesList = simulationPage.getWalletTitlesList();
        List<String> walletFunctionsList = simulationPage.getWalletFunctionsByNumberList(1);
        List<String> actionTitlesList = simulationPage.getActionTitlesList();

        Assert.assertEquals(walletTitlesList.size(), NUMBER_OF_TOTAL_WALLETS,
                "There is a different number of wallets than expected");
        Assert.assertEquals(actionTitlesList.size(), NUMBER_OF_TOTAL_WALLETS * walletFunctionsList.size(),
                "There is a different number of actions than expected");

        Log.info("Check the Wallet titles format");
        List<String> notMatchingWalletTitles = checkWalletTitlesFormat(walletTitlesList);
        Assert.assertEquals(notMatchingWalletTitles.size(), 0,
                "Some Wallet titles are not matching the expected format - " + notMatchingWalletTitles);

        Log.info("Check the Action titles format");
        List<String> notMatchingActionTitles = checkActionTitlesFormat(actionTitlesList, walletFunctionsList);
        Assert.assertEquals(notMatchingActionTitles.size(), 0,
                "Some Action titles are not matching the expected format - " + notMatchingActionTitles);

        Log.info("Close all the Wallets with even numbers into title");
        List<String> remainingWalletTitlesList = closeEvenWalletNumbers(walletTitlesList);
        List<String> remainingActionTitlesList = simulationPage.getActionTitlesList();

        Log.info("Check the number of remaining Wallets and Actions");
        Assert.assertEquals(remainingWalletTitlesList.size(), NUMBER_OF_TOTAL_WALLETS / 2,
                "There is a different number of wallets than expected");
        Assert.assertEquals(remainingActionTitlesList.size(), NUMBER_OF_TOTAL_WALLETS / 2 * walletFunctionsList.size(),
                "There is a different number of actions than expected");

        Log.info("Check the titles for the remaining Wallets - " + remainingWalletTitlesList);
        List<String> notMatchingWallets2 = new ArrayList<>();
        for (String walletTitle : remainingWalletTitlesList) {
            if(!walletTitle.matches("^Wallet #\\d*[13579]+$")){
                notMatchingWallets2.add(walletTitle);
            }
        }
        Assert.assertEquals(notMatchingWallets2.size(), 0,
                "Some remaining Wallet titles are not matching the expected regex - " + notMatchingWallets2);

        Log.info("Check the titles for the remaining Actions - " + remainingActionTitlesList);
        List<String> notMatchingActions2 = new ArrayList<>();
        String actionOddTitlePattern = "^Wallet #\\d*[13579]+:\\s?(\\b(" + String.join("|", walletFunctionsList) + ")\\b)$";
        for (String actionTitle : remainingActionTitlesList) {
            if(!actionTitle.matches(actionOddTitlePattern)){
                notMatchingActions2.add(actionTitle);
            }
        }
        Assert.assertEquals(notMatchingActions2.size(), 0,
                "Some remaining Action titles are not matching the expected regex - " + notMatchingActions2);
    }

    @AfterMethod
    public void cleanUp() {
        mainPage.closeSecondaryTabs();
    }

    @AfterClass
    public void closeBrowser() {
        // close the webDriver
        DriverManager.getWebDriver().quit();
    }
}
