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

import java.util.Arrays;
import java.util.List;


@Listeners({ScreenshotListener.class, TestListener.class, AnnotationTransformer.class})
public class DefaultContractOptionsTest extends GeneralMethods {

    @BeforeClass
    public void checkTabValues() {
        checkDefaultTabValues();
    }

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
    public void checkDefaultContractOptions(Enums.SmartContract smartContract) throws Exception {
        // Test steps:
        //      1. Open Plutus Playground UI
        //      2. Check the default messages displayed on the Simulation and Transaction page
        //      3. Compile each Demo smart contract and check that:
        //          3.1 the correct Wallet values are created
        //          3.2 the correct Actions are created for each Wallet function

        Contract contract = ContractProvider.readContractFromJson(
                "/jsons/DefaultContractOptions.json",
                Enums.SmartContract.CROWDFUNDING);

        createContractDefaultScenario(contract);


//        checkDefaultContractValues(smartContract);


        mainPage.waitABit(30000);
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
