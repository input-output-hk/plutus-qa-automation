package io.iohk.pageObjects;

import io.iohk.utils.Log;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.iohk.utils.DriverManager.getWebDriver;


public class BasePage {
	protected WebDriver driver;

	protected static final int DEFAULT_WAIT_ELEMENT_TIMEOUT = 5;
	protected static final int DEFAULT_WAIT_PAGE_TIMEOUT = 30;
    private static final String CHAR_LIST ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    /** Method to open a web page. It will close (accept) any Alert popup if displayed. */
	public void goToWebPage(String url) {
		Log.debug(" - opening URL: " + url);
		driver.get(url);
		if(isAlertPresent()){
		    Log.warn("Alert popup was displayed: " + isAlertPresent());
		    driver.switchTo().alert().accept();
		}
	}

    /** Method to refresh a web page. */
	protected void refreshWebPage() {
	    Log.debug(" - refreshing the current web page");
	    ((JavascriptExecutor) driver).executeScript("location.reload()");
    }

    /** Method to check if an Alert popup is displayed when trying to open a web page. */
    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

	/** Method to set the value of a textArea webelement. */
    protected void setTextAreaValue(WebElement webElement, String newValue) {
		Log.debug("Wait for element to be visible: " + webElement);
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		scrollToElement(webElement);
		webElement.clear();
		waitABit(250);
		Log.debug("Set the desired value to input element: " + newValue);
		webElement.sendKeys(newValue);
		// delete the curly brackets added automatically when pressing ENTER
        webElement.sendKeys(Keys.chord(Keys.CONTROL, Keys.SHIFT, Keys.END));
        waitABit(250);
        webElement.sendKeys(Keys.DELETE);
    }

	/** Method to set the value of a text webelement. */
    protected void setTextFieldValue(WebElement webElement, String string) {
		Log.debug("Wait for element to be visible: " + webElement);
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		scrollToElement(webElement);
		webElement.click();
		waitABit(100);
		webElement.clear();
		waitABit(100);
		Log.debug("Set the desired value to input element: " + string);
		webElement.sendKeys(string);
	}

	/** Method to set and check the value of a text webelement. The method also waits for the text to be set to the desired value	 */
	protected void setAndCheckTextFieldValue(WebElement webElement, String string) {
		setTextFieldValue(webElement, string);
        waitForElementToHaveSpecificValue(webElement, string, 2);
	}

	public void pressEnterKey(WebElement webElement) {
		webElement.sendKeys(Keys.ENTER);
		waitABit(100);
	}

	/** Method to wait for the value of a webelement to change to the desired expectedTextValue	 */
	protected void waitForElementToHaveSpecificValue(WebElement webElement, String expectedTextValue, int timeoutSeconds) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(timeoutSeconds, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(ElementNotVisibleException.class)
				.ignoring(StaleElementReferenceException.class);

		wait.until(ExpectedConditions.textToBePresentInElementValue(webElement, expectedTextValue));
	}

	/** Method to wait for the text of a webelement to change to the desired expectedTextValue	 */
	public void waitForElementToHaveSpecificText(WebElement webElement, String expectedTextValue, int timeoutSeconds) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(timeoutSeconds, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(ElementNotVisibleException.class)
				.ignoring(StaleElementReferenceException.class);

		wait.until(ExpectedConditions.textToBePresentInElement(webElement, expectedTextValue));
	}

	/** Method to get the text attribute of a webelement	 */
	protected String getTextFieldValue(WebElement webElement) {
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		return webElement.getText();
	}

	/** Method to get the value attribute of a webelement	 */
	protected String getValueAttrValue(WebElement webElement) {
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		return webElement.getAttribute("value");
	}

	/** Method to get the innerText attribute of a webelement	 */
	public String getInnerTextAttrValue(WebElement webElement) {
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		return webElement.getAttribute("innerText");
	}

	/** Method to get the innerText attribute of a webelement	 */
	public Integer getChildElementCountAttrValue(WebElement webElement) {
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		return Integer.valueOf(webElement.getAttribute("childElementCount"));
	}

	/** Method to get the href attribute of a webelement	 */
	public String getHrefAttrValue(WebElement webElement) {
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		return webElement.getAttribute("href");
	}

	/** Method to scroll to the webelement	 */
    protected void scrollToElement(WebElement webElement) {
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		waitABit(500);
	}

	/** Method to maximize the browser	 */
	public void maximizeBrowser()
	{
		driver.manage().window().maximize();
	}

	/** Method to navigate Back in browser	 */
	public void goToPreviousPage()
	{
		driver.navigate().back();
	}

	/** Method to wait for an element to not be visible	 */
	public void waitForElementToNotBeVisible(WebElement webElement) {
		Log.debug(" - waiting for element to NOT be visible: " + webElement);
		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, DEFAULT_WAIT_PAGE_TIMEOUT);
			wait.until(ExpectedConditions.invisibilityOf(webElement));
		}
		catch(Exception ignored) {
		}
	}

	/** Method to wait for an element to be visible	 */
    protected void waitForElementToBeVisible(WebElement webElement, int seconds) {
		Log.debug(" - waiting for element to be visible: " + webElement);
		try {
			waitForElementInSeconds(webElement, seconds);
		}
		catch(Exception ignored) {
		}
	}

	public void waitForPageLoad() {
		ExpectedCondition<Boolean> pageLoadCondition = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
		WebDriverWait wait = new WebDriverWait(getWebDriver(), DEFAULT_WAIT_PAGE_TIMEOUT);
		try {
			wait.until(pageLoadCondition);
		} catch (Exception e) {
			Log.error("Unable to load page correctly");
		}
	}

	/** Method to wait a specific time for an element to be present on page	 */
	private void waitForElementInSeconds(WebElement webElement, int seconds) {
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		wait.until(ExpectedConditions.visibilityOf(webElement));
	}

	/** Method to check if webelement is displayed/visible on the screen - true/false	 */
	protected boolean checkIfWebElementIsDisplayed(WebElement webElement) {
		try {
			Log.debug("checkIfWebElementIsDisplayed: try");
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			webElement.isDisplayed();
			return true;
		}
		catch (Exception e) {
			Log.debug("checkIfWebElementIsDisplayed: catch");
			return false;
		}
	}

	/** Method to click on specified WebElement	 */
	protected void clickOnElement(WebElement webElement) {
		Log.debug("Clicking on element: " + webElement);
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		webElement.click();
	}

	/** Method to right click on specified WebElement	 */
	protected void rightClickOnElement(WebElement webElement) {
		Log.debug("Right clicking on element: " + webElement);
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		Actions action = new Actions(driver);
		action.contextClick(webElement).perform();
	}

	/** Method to select Drop Down value based on visible text	 */
	public void selectDpdValueByVisibleText(WebElement dropDownElement, String visibleText) {
		waitForElementToBeVisible(dropDownElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		Select select = new Select(dropDownElement);
		select.selectByVisibleText(visibleText);
	}

	/** Method to select Drop Down value based on value	 */
	protected void selectDpdValueByValue(WebElement dropDownElement, String value) {
		waitForElementToBeVisible(dropDownElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		Select select = new Select(dropDownElement);
		select.selectByValue(value);
	}
	/** Method to set a check box element to True(enabled) / False(disabled)	 */
	public void setCheckboxValue(WebElement webElement, boolean value) {
		waitForElementToBeVisible(webElement, DEFAULT_WAIT_ELEMENT_TIMEOUT);
		if (value) {
			if(webElement.getAttribute("checked").equals("false"))
				clickOnElement(webElement);
		} else {
			if(webElement.getAttribute("checked").equals("true"))
				clickOnElement(webElement);
		}
	}

	/** Method to stop the script execution for specified time; NOT RECOMMENDED !!! but still useful sometimes	 */
	public void waitABit(final long delayInMilliseconds) {
		try {
			Thread.sleep(delayInMilliseconds);
		} catch (Exception ignored) {
		}
	}

	/** Method to get the url of the page	 */
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	/** Method to close all tabs except the first one	 */
	public void closeSecondaryTabs() {
		ArrayList<String> openTabs = new ArrayList<>(driver.getWindowHandles());
		if (openTabs.size() > 1) {
			for (int i = 1; i < openTabs.size(); i++) {
				driver.switchTo().window(openTabs.get(i));
				waitABit(300);
				driver.close();
				waitABit(300);
			}
		}
		driver.switchTo().window(openTabs.get(0));
	}

	/** Method to go to first tab	 */
	public void goToFirstTab() {
		ArrayList<String> openTabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(openTabs.get(0));
		waitABit(300);
	}

	/** Method to open url in new tab	 */
	protected void openLinkInNewTab(WebElement link) {
		new Actions(driver)
				.keyDown(Keys.CONTROL)
				.keyDown(Keys.SHIFT)
				.click(link)
				.keyUp(Keys.CONTROL)
				.keyUp(Keys.SHIFT)
				.build()
				.perform();
		waitABit(300);
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
	}

	/** Method to open a new tab	 */
	public void openNewTab() {
        ((JavascriptExecutor)driver).executeScript("window.open('about:blank','_blank');");
		waitABit(300);
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		waitABit(300);
	}

	/** Method to close current tab and move to previous tab (tab 0)	 */
	public void closeCurrentTab() {
		new Actions(driver)
				.sendKeys(Keys.CONTROL + "w")
				.perform();
		waitABit(300);
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));
		waitABit(300);
	}

	/** Method to compare 2 lists 	 */
    private boolean simpleCompareLists(List<String> list1, List<String> list2) {
		if (list1 == null && list2 == null){
			return true;
		}

		if((list1 == null && list2 != null) || (list1 != null && list2 == null) || (list1.size() != list2.size())){
			return false;
		}

		ArrayList<String> l1 = new ArrayList<String>(list1);
		ArrayList<String> l2 = new ArrayList<String>(list2);
		Collections.sort(l1);
		Collections.sort(l2);
		return l1.equals(l2);
	}

	/** Method to log/print the elements that are not common to 2 different lists	 */
	public boolean complexCompareLists(List<String> list1, List<String> list2) {
		if (!simpleCompareLists(list1, list2)) {
			Log.error("L1 - L2: " + new ArrayList<Object>(CollectionUtils.subtract(list1, list2)));
			Log.error("L2 - L1: " + new ArrayList<Object>(CollectionUtils.subtract(list2, list1)));
			return false;
		} else {
			return true;
		}
	}

	/** Method to compare 2 HashMaps
	 * hashMap1 = returned values
	 * hashMap2 = expected values
	 */
	public boolean compareLinkedHashMaps(LinkedHashMap<String, String> hashMap1, LinkedHashMap<String, String> hashMap2) {
		if (hashMap1 == null || hashMap2 == null)
			return false;

		for (String key : hashMap1.keySet()) {
			if (!(hashMap1.containsKey(key) == hashMap2.containsKey(key))) {
				Log.error("Unexpected key was not found: " + key);
				Log.error("values from UI(hashMap1): " + hashMap1);
				Log.error("values from json(hashMap2): " + hashMap2);
				return false;
			} else if (!(hashMap1.get(key).equals(hashMap2.get(key)))) {
				Log.error("Different values for provided key;");
				Log.error("  Key: " + key);
				Log.error("  Expected value: " + hashMap1.get(key));
				Log.error("  Returned value: " + hashMap2.get(key));
				Log.error("values from UI(hashMap1): " + hashMap1);
				Log.error("values from json(hashMap2): " + hashMap2);
				return false;
			}
		}

		for (String key : hashMap2.keySet()) {
			if (!(hashMap2.containsKey(key) == hashMap1.containsKey(key))) {
				Log.error("Expected key was not found: " + key);
				Log.error("values from UI(hashMap1): " + hashMap1);
				Log.error("values from json(hashMap2): " + hashMap2);
				return false;
			} else if (!(hashMap1.get(key).equals(hashMap2.get(key)))) {
				Log.error("Different values for provided key;");
				Log.error("  Key: " + key);
				Log.error("  Unexpected value: " + hashMap1.get(key));
				Log.error("values from UI(hashMap1): " + hashMap1);
				Log.error("values from json(hashMap2): " + hashMap2);
				return false;
			}
		}

		return true;
	}

    /** Method to generate random number - 1 digit length	 */
    private int generateRandomNumber() {
        int randomInt;
        Random randomGen = new Random();
        randomInt = randomGen.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1 ) {
            return randomInt;
        }
        else {
            return randomInt-1;
        }
    }

	/** Method to generate random string	 */
	public String generateRandomString(int stringLength) {
		StringBuilder randStr = new StringBuilder();
		for(int i = 0; i < stringLength; i++) {
			int number = generateRandomNumber();
			char c = CHAR_LIST.charAt(number);
			randStr.append(c);
		}
		return randStr.toString();
	}
}
