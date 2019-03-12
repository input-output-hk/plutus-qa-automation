package io.iohk.utils.listeners;

import com.relevantcodes.extentreports.LogStatus;
import io.iohk.frontEndTests.BaseTest;
import io.iohk.utils.DriverManager;
import io.iohk.utils.extentReports.ExtentManager;
import io.iohk.utils.extentReports.ExtentTestManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

public class TestListener extends BaseTest implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    //Before starting all tests, below method runs.
    @Override
    public void onStart(ITestContext iTestContext) {
        iTestContext.setAttribute("WebDriver", DriverManager.getWebDriver());
    }

    //After ending all tests, below method runs.
    @Override
    public void onFinish(ITestContext iTestContext) {
        //Do tier down operations for extentreports reporting!
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        //Start operation for extentreports.
        String className = iTestResult.getTestClass().getRealClass().getSimpleName();
        Object[] parameters = iTestResult.getParameters();
        String testName = className + "_" + Arrays.toString(parameters);
        ExtentTestManager.startTest(testName,"");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        //Extentreports log operation for passed tests.
        ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        //Get driver from BaseTest and assign to local webdriver variable.
        Object testClass = iTestResult.getInstance();
        WebDriver webDriver = DriverManager.getWebDriver();

        //Take base64Screenshot screenshot.
        String base64Screenshot = "data:image/png;base64,"+((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BASE64);

        //Extentreports log and screenshot operations for failed tests.
        ExtentTestManager.getTest().log(LogStatus.FAIL,"Test Failed",
                ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        //Extentreports log operation for skipped tests.
        ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }
}
