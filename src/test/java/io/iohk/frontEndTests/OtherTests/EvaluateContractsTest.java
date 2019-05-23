package io.iohk.frontEndTests.OtherTests;

import io.iohk.dataModels.Contract;
import io.iohk.dataProviders.ContractProvider;
import io.iohk.frontEndTests.GeneralMethods;
import io.iohk.utils.DriverManager;
import io.iohk.utils.listeners.AnnotationTransformer;
import io.iohk.utils.listeners.ScreenshotListener;
import io.iohk.utils.listeners.TestListener;
import org.testng.annotations.*;

@Listeners({ScreenshotListener.class, TestListener.class, AnnotationTransformer.class})
public class EvaluateContractsTest extends GeneralMethods {
    @DataProvider
    public Object[][] dataProviderScenarios() {
        return new Object[][] {
//                { "/jsons/Game_1Simulation_2Wallets.json"},
//                { "/jsons/Crowdfundig_3Wallets.json"},
//                { "/jsons/Crowdfundig_10Wallets.json"},
                {"/jsons/Crowdfundig_PayToWallet_3Simulations.json"}
        };
    }

    @Test(dataProvider = "dataProviderScenarios")
    public void evaluatePositiveDemoScenarios(String dataSource) throws Exception {
        // Test steps:
        //      1. Create the scenarios from the provided JSON files
        //      2. Create and Evaluate the scenarios and check that:
        //          2.1 each contract is successfully compiled
        //          2.2 all the values from the Simulation tab are filled correctly (as per json)
        //          2.3 each contract is successfully evaluated
        //          2.4 there aren't any unexpected errors inside the Transactions tab
        //          2.5 the Final Balance values inside the Transaction tab have the expected values

        Contract contract = ContractProvider.readContractFromJson(dataSource);
        evaluateContractFromScenario(contract);
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
