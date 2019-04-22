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
public class GameContractTest extends GeneralMethods {
    @DataProvider
    public Object[][] dataProviderScenarios() {
        return new Object[][] {
                { "/jsons/GameContract_1Simulation_2Wallets.json"}
        };
    }

    @Test(dataProvider = "dataProviderScenarios")
    public void evaluatePositiveDemoScenarios(String dataSource) throws Exception {

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
