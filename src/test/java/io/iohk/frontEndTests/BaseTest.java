package io.iohk.frontEndTests;

import io.iohk.pageObjects.*;
import io.iohk.utils.Constants;
import io.iohk.utils.DriverFactory;
import io.iohk.utils.DriverManager;
import io.iohk.utils.Enums.Browser;
import io.iohk.utils.Enums.Environment;
import io.iohk.utils.Enums.Runner;
import io.iohk.utils.Log;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BaseTest {
    protected SoftAssert softAssert;

    protected MainPage mainPage;
    protected EditorPage editorPage;
    protected SimulationPage simulationPage;
    protected TransactionsPage transactionsPage;
    protected GithubLoginPage githubLoginPage;

    @BeforeSuite
    public void beforeSuite() throws Exception {
        Log.info("Suite started");
        Log.info("Delete all existing files from screenshots folder.");
        cleanScreenshotsFolder();
    }

    @BeforeClass
    @Parameters({"env", "browserType", "runner", "gridAddress", "headless"})
    public void beforeClass(
            @Optional("PROD") Environment env,
            @Optional("CHROME") Browser browserType,
            @Optional("LOCALHOST") Runner runner,
            @Optional("http://localhost:4444/wd/hub") String gridAddress,
            @Optional("false") boolean headless) throws Exception {

        WebDriver driver = null;

        Log.info("===============================================");
        Log.info("Test started.");

        Log.info("  - Environment:  " + env);
        Log.info("  - Browser Type: " + browserType);
        Log.info("  - Runner:       " + runner);
        if (runner == Runner.REMOTE) {
            Log.info("  - Grid Address: " + gridAddress);
        }
        if ((runner == Runner.LOCALHOST) && (browserType == Browser.CHROME)) {
            Log.info("  - Headless:     " + headless);
        }

        Log.info("Creating webdriver instance");
        driver = DriverFactory.createInstance(browserType, runner, gridAddress, headless);
        DriverManager.setWebDriver(driver);

        Log.debug("Initializing the Page Objects (!! only after the driver was initialized !!)");
        // only after we have the driver initialize, we can initialize the pages
        softAssert = new SoftAssert();

        mainPage = new MainPage(driver);
        editorPage = new EditorPage(driver);
        simulationPage = new SimulationPage(driver);
        transactionsPage = new TransactionsPage(driver);
        githubLoginPage = new GithubLoginPage(driver);

        // open the web page / environment
        Log.debug("Open the selected Remix page");
        openPlaygroundsPage(env);
    }

    private void openPlaygroundsPage(Environment environment) {
        mainPage.waitForPageLoad();
        switch (environment) {
            case PROD:
                mainPage.goToWebPage(Constants.PROD);
                break;
            case DEV_KRIS:
                mainPage.goToWebPage(Constants.DEV_KRIS);
                break;
            case DEV_DAVID:
                mainPage.goToWebPage(Constants.DEV_DAVID);
                break;
        }
    }

    private void cleanScreenshotsFolder() throws Exception {
        try {
//          create screenshots directory if it is not already created
            File screenshotsDirectory = new File(String.valueOf(System.getProperties().get("user.dir")) + "/screenshots");
            if (!screenshotsDirectory.exists()) {
                screenshotsDirectory.mkdirs();
            }
        }
        catch(Exception e) {
            throw new Exception("Cannot create the screenshot folder;", e);
        }

//          when testsuite/ran starts, delete all the files inside the screenshot folder
        try{
            File screenshotsFolder = new File("screenshots/");
            for(File file: screenshotsFolder.listFiles())
                file.delete();
        }
        catch(Exception e) {
            throw new Exception("Cannot delete files from screenshot folder;", e);
        }
    }

    public static void takeScreenshot(String className, String testName) {
        // get the driver
        WebDriver driver = DriverManager.getWebDriver();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());

        // screenshot file
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        // add 'file name' + 'file path' to the screenshot name
        String fileName = "\\screenshots\\" + className + "_" + testName.replaceAll("/", "-") + "_" + currentDate + "_" + ".png";
        String filePath = System.getProperties().get("user.dir") + fileName;

        // copy screenshot in destination folder
        try {
            FileUtils.copyFile(scrFile, new File(filePath));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
