package io.iohk.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends BasePage {
    @FindBy(xpath = "//a[contains(text(),'Getting Started')]")
    private WebElement btnGettingStarted;

    @FindBy(xpath = "//a[contains(text(),'Tutorial')]")
    private WebElement btnTutorial;

    @FindBy(xpath = "//a[contains(text(),'API')]")
    private WebElement btnApi;

    @FindBy(xpath = "//a[contains(text(),'Privacy')]")
    private WebElement btnPrivacy;

    @FindBy(xpath = "//a[@id='tab-editor']")
    private WebElement btnEditor;

    @FindBy(xpath = "//a[@id='tab-simulation']")
    private WebElement btnSimulation;

    @FindBy(xpath = "//a[@id='tab-transactions']")
    private WebElement btnTransactions;


    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickGettingStartedBtn() {
        clickOnElement(btnGettingStarted);
    }

    public void openGettingStartedLinkInNewTab() {
        openLinkInNewTab(btnGettingStarted);
    }

    public void clickTutorialBtn() {
        clickOnElement(btnTutorial);
    }

    public void openTutorialLinkInNewTab() {
        openLinkInNewTab(btnTutorial);
    }

    public void clickApiBtn() {
        clickOnElement(btnApi);
    }

    public void openApiLinkInNewTab() {
        openLinkInNewTab(btnApi);
    }

    public void clickPrivacyBtn() {
        clickOnElement(btnPrivacy);
    }

    public void openPrivacyLinkInNewTab() {
        openLinkInNewTab(btnPrivacy);
    }

    public void clickEditorBtn() {
        clickOnElement(btnEditor);
    }

    public void clickbtnSimulationBtn() {
        clickOnElement(btnSimulation);
    }

    public void clickTransactionsBtn() {
        clickOnElement(btnTransactions);
    }
}
