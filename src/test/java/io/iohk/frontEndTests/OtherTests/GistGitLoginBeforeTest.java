package io.iohk.frontEndTests.OtherTests;

import io.iohk.dataModels.Contract;
import io.iohk.dataProviders.ContractProvider;
import io.iohk.frontEndTests.GeneralMethods;
import io.iohk.utils.DriverManager;
import io.iohk.utils.Enums;
import io.iohk.utils.Log;
import io.iohk.utils.listeners.AnnotationTransformer;
import io.iohk.utils.listeners.ScreenshotListener;
import io.iohk.utils.listeners.TestListener;
import org.testng.annotations.*;


@Listeners({ScreenshotListener.class, TestListener.class, AnnotationTransformer.class})
public class GistGitLoginBeforeTest extends GeneralMethods {
    @DataProvider
    public Object[][] dataProviderScenarios() {
        return new Object[][] {
                { "/jsons/Crowdfundig_1Simulation_2Wallets_7Actions.json"}
//                { "/jsons/Crowdfundig_3Simulations_15Wallets.json"}
        };
    }

    @Test(dataProvider = "dataProviderScenarios")
    public void gistGitLoginBeforeTest(String dataSource) throws Exception {
        // Test steps:
        //      1. Sign In to Github button using the header button/option
        //      2. Create and Evaluate the scenarios from each provided JSON files
        //      3. Check that the Simulation tab values are still remembered
        //      4. Save the scenario as a github gist
        //      5. Compile another Demo contract - Vesting
        //      6. Load the previously created gist and check the Simulation tab values

        Contract contract = ContractProvider.readContractFromJson(dataSource);

        Log.debug("1. Sign In to Github button using the header button/option");
        signInToGithub();

        Log.debug(" 2. Create and Evauate the scenarios from each provided JSON files");
        evaluateContractFromScenario(contract);

        Log.debug("3. Check that the Simulation tab values");
        checkSimulationTabValues(contract);

        Log.debug("4. Save the scenario as a github gist");
        String gistId = publishGist();

        Log.debug("5. Compile another Demo contract - Vestings");
        compileSpecificSmartContract(Enums.SmartContract.VESTING);

        Log.debug("6. Load the previously created gist");
        loadGistId(gistId);

        Log.debug("7. Check that the Simulation tab values");
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
