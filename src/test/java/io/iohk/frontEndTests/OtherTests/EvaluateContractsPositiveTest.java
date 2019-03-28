package io.iohk.frontEndTests.OtherTests;

import io.iohk.dataModels.Contract;
import io.iohk.dataProviders.ContractProvider;
import io.iohk.frontEndTests.GeneralMethods;
import io.iohk.utils.DriverManager;
import io.iohk.utils.Enums;
import io.iohk.utils.listeners.AnnotationTransformer;
import io.iohk.utils.listeners.ScreenshotListener;
import io.iohk.utils.listeners.TestListener;
import org.testng.annotations.*;

@Listeners({ScreenshotListener.class, TestListener.class, AnnotationTransformer.class})
public class EvaluateContractsPositiveTest extends GeneralMethods {
    @DataProvider
    public Object[][] DataProviderCrowdfunding() {
        return new Object[][] {
//                { "/jsons/CrowdfundigContract_1Simulation_2Wallets_7Actions.json", Enums.SmartContract.CROWDFUNDING},
//                { "/jsons/CrowdfundigContract_2Simulations_2Wallets_7Actions.json", Enums.SmartContract.CROWDFUNDING},
//                { "/jsons/CrowdfundigContract_2Simulations_15Wallets.json", Enums.SmartContract.CROWDFUNDING},
//                { "/jsons/VestingContract_1Simulation_2Wallets_8Actions.json", Enums.SmartContract.VESTING},
//                { "/jsons/MessagesContract_1Simulation_2Wallets_8Actions.json", Enums.SmartContract.MESSAGES},
                { "/jsons/GameContract_1Simulation_2Wallets_8Actions.json", Enums.SmartContract.GAME}
        };
    }

    @Test(dataProvider = "DataProviderCrowdfunding")
    public void checkDefaultContractOptions(String dataSoruce, Enums.SmartContract smartContract) throws Exception {
        // Test steps:
        //      1. Create the scenarios from the provided JSON files
        //      2. Create and Evaluate the scenarios and check that:
        //          3.1 each contract is successfully compiled
        //          3.2 each contract is successfully evaluated

        Contract contract = ContractProvider.readContractFromJson(dataSoruce, smartContract);
        executeContractFromScenario(contract);
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
