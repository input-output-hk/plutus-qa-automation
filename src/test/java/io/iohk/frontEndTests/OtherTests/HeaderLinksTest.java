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
public class HeaderLinksTest extends GeneralMethods {

    @DataProvider
    public Object[][] mainPageLinks()  {
        List<Enums.MainPageLinks> enumValues = Arrays.asList(Enums.MainPageLinks.values());

        Object[][] data = new Object[enumValues.size()][1];

        int index = 0;
        for(Enums.MainPageLinks link: enumValues){
            data[index][0] = link;
            index ++;
        }

        return data;
    }

    @Test(dataProvider = "mainPageLinks")
    public void headerLinks(Enums.MainPageLinks mainPageLinks) {
        // Test steps:
        //      1. Check that the main page header links are opening the correct web pages

        checkHeaderLinks(mainPageLinks);
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
