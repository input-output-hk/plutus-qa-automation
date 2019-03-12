package io.iohk.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TransactionsPage extends BasePage {
    @FindBy(xpath = "")
    private WebElement btnTransactions;

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

//    public boolean checkIfTransactionsTabIsOpen() {
//        return checkIfWebElementIsDisplayed(btnTransactions);
//    }

//    public void waitForTransactionsTabIsOpen() {
//        waitForElementToBeVisible(btnTransactions, DEFAULT_WAIT_ELEMENT_TIMEOUT);
//    }
}
