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
public class GistTest extends GeneralMethods {
    @DataProvider
    public Object[][] DataProviderCrowdfunding() {
        return new Object[][] {
                { "/jsons/CrowdfundigContract_1Simulation_2Wallets_7Actions.json", Enums.SmartContract.CROWDFUNDING}
//                { "/jsons/CrowdfundigContract_3Simulations_15Wallets.json", Enums.SmartContract.CROWDFUNDING}
        };
    }

    @Test(dataProvider = "DataProviderCrowdfunding")
    public void checkDefaultContractOptions(String dataSoruce, Enums.SmartContract smartContract) throws Exception {
        // Test steps:
        //      1. Create the scenarios from the provided JSON files
        //      2. Save the scenario as a github gist
        //      3. Load the gist and check the Simulation tab values

        Contract contract = ContractProvider.readContractFromJson(dataSoruce, smartContract);
        executeContractFromScenario(contract);
        signInToGithub();
        publishGist();
        checkSimulationTabValues(contract);

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
