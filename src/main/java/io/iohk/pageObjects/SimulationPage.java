package io.iohk.pageObjects;

import io.iohk.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class SimulationPage extends BasePage {
    @FindBy(xpath = "//div[@class='add-wallet']//i[contains(@class,'fa-plus')]")
    private WebElement btnAddWallet;

    @FindBy(xpath = "//div[@class='add-wait-action']//i[contains(@class,'fa-plus')]")
    private WebElement btnAddWaitAction;

    @FindBy(xpath = "//button[@id='evaluate']")
    private WebElement btnEvaluate;

    @FindBy(xpath = "//button[@id='evaluate'][contains(@class,'btn-success')]")
    private WebElement btnEvaluateSuccess;

    @FindBy(xpath = "//button[@id='evaluate'][contains(@class,'btn-danger')]")
    private WebElement btnEvaluateFailed;

    @FindBy(xpath = "//i[contains(@class,'fa-spinner')]")
    private WebElement btnEvaluateSpinner;

    @FindBy(xpath = "//div[@class='wallet']//span[@class='wallet-id']")
    private List<WebElement> listWalletTitles;

    public SimulationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkIfSimulationTabIsOpen() {
        return checkIfWebElementIsDisplayed(btnEvaluate);
    }

    public void waitForSimulationTabIsOpen() {
        waitForElementToBeVisible(btnEvaluate, DEFAULT_WAIT_ELEMENT_TIMEOUT);
    }

    public void clickAddWalletBtn() {
        clickOnElement(btnAddWallet);
    }

    public void clickAddWaitActionBtn() {
        clickOnElement(btnAddWaitAction);
    }

    public void clickEvaluateBtn() {
        clickOnElement(btnEvaluate);
    }

    private boolean getEvaluationStatus() {
        waitForElementToBeVisible(btnEvaluateSuccess, DEFAULT_WAIT_ELEMENT_TIMEOUT);
        if (checkIfWebElementIsDisplayed(btnEvaluateSuccess)) {
            return true;
        } else if (checkIfWebElementIsDisplayed(btnEvaluateSuccess)) {
            return false;
        }
        return false;
    }

    public void waitEvaluateSuccess() {
        waitForElementToBeVisible(btnEvaluateSuccess, DEFAULT_WAIT_ELEMENT_TIMEOUT);
        Assert.assertTrue(getEvaluationStatus());
    }

    public ArrayList<String> getWalletTitlesList() {
        ArrayList<String> walletTitlesList = new ArrayList<String>();
        for (WebElement walletTitleObj : listWalletTitles) {
            String walletTitle = getTextFieldValue(walletTitleObj);
            walletTitlesList.add(walletTitle);
        }
        return walletTitlesList;
    }

    public void closeWallet(String walletTitle) {
        Log.info("Closing wallet: " + walletTitle);
        String walletXBtnLocator =
                "//div[@class='wallet'][contains(., '" +
                        walletTitle +
                        "')]//i[contains(@class,'fa-close')]";
        WebElement xButtonObj = driver.findElement(By.xpath(walletXBtnLocator));
        clickOnElement(xButtonObj);
    }

    public String getWalletCurrency(String walletTitle) {
        String walletCurrencyLocator =
                "//div[@class='wallet'][contains(., '" +
                        walletTitle +
                        "')]//div[@class='col'][1]";
        WebElement walletCurrencyObj = driver.findElement(By.xpath(walletCurrencyLocator));
        return getTextFieldValue(walletCurrencyObj);
    }

    public int getWalletInitialBalance(String walletTitle) {
        String walletBalanceLocator =
                "//div[@class='wallet'][contains(., '" +
                        walletTitle +
                        "')]//input[@type='number']";
        WebElement walletBalanceObj = driver.findElement(By.xpath(walletBalanceLocator));
        return Integer.parseInt(getValueAttrValue(walletBalanceObj));
    }

    public ArrayList<String> getWalletFunctionsList(String walletTitle) {
        ArrayList<String> walletFunctionsList = new ArrayList<String>();
        String walletFunctionsLocator =
                "//div[@class='wallet'][contains(., '" +
                        walletTitle +
                        "')]//button[contains(@class,'action-button')]";
        List<WebElement> listWalletFunctionsObj = driver.findElements(By.xpath(walletFunctionsLocator));
        for (WebElement functionNameObj : listWalletFunctionsObj) {
            String functionName = getTextFieldValue(functionNameObj);
            walletFunctionsList.add(functionName);
        }
        return walletFunctionsList;
    }

    public void clickWalletFunction(String walletTitle, String functionTitle) {
        Log.info("Clicking on " + functionTitle + " function for wallet: " + walletTitle);
        String walletFunctionLocator =
                "//div[@class='wallet'][contains(., '" +
                        walletTitle +
                        "')]//button[contains(text(),'" +
                        functionTitle +
                        "')]";
        WebElement walletFunctionObj = driver.findElement(By.xpath(walletFunctionLocator));
        clickOnElement(walletFunctionObj);
    }
}
