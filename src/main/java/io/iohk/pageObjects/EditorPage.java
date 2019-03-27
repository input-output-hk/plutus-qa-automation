package io.iohk.pageObjects;

import io.iohk.utils.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class EditorPage extends BasePage {
    @FindBy(xpath = "//div[@id='demos']//button[contains(text(),'Crowdfunding')]")
    private WebElement btnCrowdfunding;

    @FindBy(xpath = "//div[@id='demos']//button[contains(text(),'Game')]")
    private WebElement btnGame;

    @FindBy(xpath = "//div[@id='demos']//button[contains(text(),'Messages')]")
    private WebElement btnMessages;

    @FindBy(xpath = "//div[@id='demos']//button[contains(text(),'Vesting')]")
    private WebElement btnVesting;

    @FindBy(xpath = "//div[@id='editor']")
    private WebElement txtScriptTextArea;

    @FindBy(xpath = "//button[@id='compile']")
    private WebElement btnCompile;

    @FindBy(xpath = "//i[contains(@class,'fa-spinner')]")
    private WebElement btnCompileSpinner;

    @FindBy(xpath = "//button[@id='compile'][contains(@class,'btn-success')]")
    private WebElement btnCompileSuccess;

    @FindBy(xpath = "//button[@id='compile'][contains(@class,'btn-danger')]")
    private WebElement btnCompileFailed;

    @FindBy(xpath = "//div[@class='compilation-error']//code")
    private List<WebElement> listCompilationErrors;

    @FindBy(xpath = "//button[@id='publish-gist']")
    private WebElement btnPublish;

    public EditorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkIfEditorTabIsOpen() {
        return checkIfWebElementIsDisplayed(btnCompile);
    }

    public void waitForEditorTabIsOpen() {
        waitForElementToBeVisible(btnCompile, DEFAULT_WAIT_ELEMENT_TIMEOUT);
    }

    public void clickCrowdfundingBtn() {
        clickOnElement(btnCrowdfunding);
    }

    public void clickGameBtn() {
        clickOnElement(btnGame);
    }

    public void clickMessagesBtn() {
        clickOnElement(btnMessages);
    }

    public void clickVestingBtn() {
        clickOnElement(btnVesting);
    }

    public void setScriptArea(String newValue) {
        setTextFieldValue(txtScriptTextArea, newValue);
    }

    public void clickCompileBtn() {
        clickOnElement(btnCompile);
    }

    public void clickPublishBtn() {
        clickOnElement(btnPublish);
    }

    public void waitContractCompileSuccess() {
        Log.info("  - Waiting for the smart contract to be successfully compiled...");
        waitForElementToBeVisible(btnCompileSuccess, DEFAULT_WAIT_ELEMENT_TIMEOUT);
        Assert.assertTrue(getCompileStatus(), "Error: Compile action was not successful.");
    }

    private boolean getCompileStatus() {
        Log.info("  - Waiting the smart contract's compile status...");
        waitForElementToBeVisible(btnCompileSuccess, DEFAULT_WAIT_ELEMENT_TIMEOUT);
        if (checkIfWebElementIsDisplayed(btnCompileSuccess)) {
            return true;
        } else if (checkIfWebElementIsDisplayed(btnCompileFailed)) {
            return false;
        }
        return false;
    }
}
