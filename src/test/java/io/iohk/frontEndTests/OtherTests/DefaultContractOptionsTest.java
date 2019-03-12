package io.iohk.frontEndTests.OtherTests;

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
    public void checkDefaultContractOptions(Enums.SmartContract smartContract) {
        // Test steps:
        //      1. Open Plutus Playground UI
        //      2. Compile each Demo smart contract and check that the correct Wallet values are created
        //      3. Evaluate each smart contract (using the default values)
        
        compileSpecificSmartContract(smartContract);
        checkDefaultContractValues(smartContract);
        evaluateSimulation();
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
