package io.iohk.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.LinkedHashMap;

public class MainPage extends BasePage {
    @FindBy(xpath = "//a[contains(text(),'Getting Started')]")
    private WebElement btnGettingStarted;

    @FindBy(xpath = "//a[contains(text(),'Tutorial')]")
    private WebElement btnTutorial;

    @FindBy(xpath = "//a[contains(text(),'API')]")
    private WebElement btnApi;

    @FindBy(xpath = "//a[contains(text(),'Privacy')]")
    private WebElement btnPrivacy;

    @FindBy(xpath = "//a[@id='publish-gist']")
    private WebElement btnGithubLogin;

    @FindBy(xpath = "//button[@id='publish-gist']")
    private WebElement btnPublishGist;

    @FindBy(xpath = "//button[@id='publish-gist']/i[contains(@class,'fa-spinner')]")
    private WebElement btnPublishGistSpinner;

    @FindBy(xpath = "//button[@id='load-gist']")
    private WebElement btnLoadGist;

    @FindBy(xpath = "//div[@class='gist-controls']//input")
    private WebElement txtLoadGist;

    @FindBy(xpath = "//div[@class='gist-controls']//a[contains(.,'View on Github')]")
    private WebElement btnViewGistOnGithub;

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

    public void clickGithubLoginBtn() {
        clickOnElement(btnGithubLogin);
    }

    public void clickPublishGistBtn() {
        clickOnElement(btnPublishGist);
    }

    public void waitPublishGistToFinish() {
        waitABit(150);
        waitForElementToNotBeVisible(btnPublishGistSpinner);
    }

    public void clickLoadGistBtn() {
        clickOnElement(btnLoadGist);
    }

    public void setLoadGistId(String gistId) {
        setTextFieldValue(txtLoadGist, gistId);
    }

    public String getGistId() {
        return getValueAttrValue(txtLoadGist);
    }

    public void clickViewGistOnGithubBtn() {
        clickOnElement(btnViewGistOnGithub);
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
