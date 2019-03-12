package io.iohk.utils.listeners;

import io.iohk.frontEndTests.BaseTest;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.Arrays;


public class ScreenshotListener extends TestListenerAdapter {
    @Override
    public void onTestFailure(ITestResult testResult) {
        if(testResult.getStatus() == ITestResult.FAILURE){
            String className = testResult.getTestClass().getRealClass().getSimpleName();
//            String methodName = testResult.getMethod().getMethodName();
            Object[] parameters = testResult.getParameters();
            BaseTest.takeScreenshot(className, Arrays.toString(parameters));
        }
    }

//    @Override
//    public void onTestStart(ITestResult testResult) {
//        String className = testResult.getTestClass().getRealClass().getSimpleName();
//        String methodName = testResult.getMethod().getMethodName();
//        Object[] parameters = testResult.getParameters();
//        BaseTest.takeScreenshot(className, Arrays.toString(parameters));
//    }
}
