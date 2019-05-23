package io.iohk.pageObjects;

import io.iohk.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TransactionsPage extends BasePage {
    @FindBy(xpath = "//div[@class='container'][contains(.,'Click the Simulation tab above and evaluate a simulation to see some results')]")
    private WebElement txtNoEvaluatedContract;

    @FindBy(xpath = "//*[name()='svg']//span[contains(text(),'Wallet #')]")
    private List<WebElement> listWalletTitlesFromFinalBalances;

    @FindBy(xpath = "//div[@class='logs']//div[@class='error']")
    private List<WebElement> listErrors;

    @FindBy(xpath = "//div[@class='logs']//div[@class='info']")
    private List<WebElement> listInfos;

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkIfNoEvalueatedContractDisplayed() {
        return checkIfWebElementIsDisplayed(txtNoEvaluatedContract);
    }

    public ArrayList<String> getErrorsList() {
        ArrayList<String> errorsList = new ArrayList<>();
        for (WebElement errorObj : listErrors) {
            String errorTitle = getTextFieldValue(errorObj);
            errorsList.add(errorTitle);
        }
        return errorsList;
    }

    public ArrayList<String> getInfosList() {
        ArrayList<String> infosList = new ArrayList<>();
        for (WebElement infoObj : listInfos) {
            String infoTitle = getTextFieldValue(infoObj);
            infosList.add(infoTitle);
        }
        return infosList;
    }

    public LinkedList<String> getWalletTitlesFromFinalBalances() {
        LinkedList<String> walletTitlesFromFinalBalances = new LinkedList<>();
        for (WebElement walltTitleObj : listWalletTitlesFromFinalBalances) {
            String walletTitle = getAttributeValue(walltTitleObj, "innerText");
            walletTitlesFromFinalBalances.add(walletTitle);
        }
        return walletTitlesFromFinalBalances;
    }

     public int getWalletFinalBalance(String walletTitle, LinkedList<String> walletTitlesFromFinalBalances) {
         Log.info("  - Getting the (UI) Final Balance for: " + walletTitle);
         String walletBalanceLocator =
                "//*[name()='svg']//*[name()='line' and @class='ct-bar'][" +
                        (walletTitlesFromFinalBalances.indexOf(walletTitle) + 1) +
                        "]";
         WebElement walletBalanceObj = driver.findElement(By.xpath(walletBalanceLocator));
         return Integer.parseInt(getAttributeValue(walletBalanceObj, "ct:value"));
    }

}
