package io.iohk.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GithubLoginPage extends BasePage{
    @FindBy(xpath = "//input[@id='login_field']")
    private WebElement txtUsername;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement txtPassword;

    @FindBy(xpath = "//input[@value='Sign in'][@type='submit']")
    private WebElement btnSignIn;

    public GithubLoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setUsername(String string) {
        setTextFieldValue(txtUsername, string);
    }

    public void setPassword(String string) {
        setTextFieldValue(txtPassword, string);
    }

    public void clickSignInBtn() {
        clickOnElement(btnSignIn);
    }
}
