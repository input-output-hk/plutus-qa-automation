package io.iohk.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TransactionsPage extends BasePage {
    @FindBy(xpath = "//div[@class='container'][contains(.,'Click the Simulation tab above and evaluate a simulation to see some results')]")
    private WebElement txtNoEvaluatedContract;

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkIfNoEvalueatedContractDisplayed() {
        return checkIfWebElementIsDisplayed(txtNoEvaluatedContract);
    }
}
