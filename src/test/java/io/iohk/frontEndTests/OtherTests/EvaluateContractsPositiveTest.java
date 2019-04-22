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
public class EvaluateContractsPositiveTest extends GeneralMethods {
    @DataProvider
    public Object[][] dataProviderScenarios() {
        return new Object[][] {
                { "/jsons/GameContract_1Simulation_2Wallets.json"}
//                { "/jsons/CrowdfundigContract_3Wallets.json"}
        };
    }

    @Test(dataProvider = "dataProviderScenarios")
    public void evaluatePositiveDemoScenarios(String dataSource) throws Exception {
        // Test steps:
        //      1. Create the scenarios from the provided JSON files
        //      2. Create and Evaluate the scenarios and check that:
        //          3.1 each contract is successfully compiled
        //          3.2 all the values from the Simulation tab are filled correctly (as per json)
        //          3.3 each contract is successfully evaluated

        Contract contract = ContractProvider.readContractFromJson(dataSource);

        evaluatePositiveContractFromScenario(contract);
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
