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
    @FindBy(xpath = "//div[@class='container'][contains(.,'Click the Editor tab above and compile a contract to get started')]")
    private WebElement txtNoCompiledContract;

    @FindBy(xpath = "//div[@id='simulation-nav']//i[contains(@class,'fa-plus')]")
    private WebElement btnAddNewSimulation;

    @FindBy(xpath = "//div[@id='simulation-nav']//button[contains(text(),'Simulation #')]")
    private List<WebElement> listSimulationTitles;

    @FindBy(xpath = "//div[contains(@class,'wallet-')]//span[@class='wallet-id']")
    private List<WebElement> listWalletTitles;

    @FindBy(xpath = "//div[@class='add-wait-action']//i[contains(@class,'fa-plus')]")
    private WebElement btnAddWaitAction;

    @FindBy(xpath = "//div[contains(@class,'action-')]//h3")
    private List<WebElement> listActionTitles;

    @FindBy(xpath = "//div[@class='add-wallet']//i[contains(@class,'fa-plus')]")
    private WebElement btnAddWallet;

    @FindBy(xpath = "//button[@id='evaluate']")
    private WebElement btnEvaluate;

    @FindBy(xpath = "//button[@id='evaluate'][contains(@class,'btn-success')]")
    private WebElement btnEvaluateSuccess;

    @FindBy(xpath = "//button[@id='evaluate'][contains(@class,'btn-danger')]")
    private WebElement btnEvaluateFailed;

    @FindBy(xpath = "//i[contains(@class,'fa-spinner')]")
    private WebElement btnEvaluateSpinner;

    public SimulationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkIfSimulationTabIsOpen() {
        return checkIfWebElementIsDisplayed(btnEvaluate);
    }

    public boolean checkIfNoCompiledContractDisplayed() {
        return checkIfWebElementIsDisplayed(txtNoCompiledContract);
    }

    public void waitForSimulationTabIsOpen() {
        waitForElementToBeVisible(btnEvaluate, DEFAULT_WAIT_ELEMENT_TIMEOUT);
    }

    public void clickAddNewSimulationBtn() {
        clickOnElement(btnAddNewSimulation);
    }

    public void openSimulation(String simulationTitle) {
        // simulation title: Simulation #2
        Log.info("  - Opening simulation: " + simulationTitle);
        String simulationTabLocator =
                    "//div[@id='simulation-nav']//button[@id='simulation-nav-item-" +
                    (Integer.parseInt(simulationTitle.split("#")[1].trim()) - 1) +
                    "']";
        WebElement simulationTabObj = driver.findElement(By.xpath(simulationTabLocator));
        clickOnElement(simulationTabObj);
    }

    public void createAndOpenNewSimulation() {
        Log.info("  - Creating new Simulation");
        int noOfCreatedSimulations = getSimulationTitlesList().size();
        clickAddNewSimulationBtn();
        int noOfExpectedSimulations = noOfCreatedSimulations + 1;
        int count = 0;
        while (noOfCreatedSimulations != noOfExpectedSimulations) {
            noOfCreatedSimulations = getSimulationTitlesList().size();
            waitABit(250);
            if (count > 40) {
                Assert.fail("ERROR: The Simulation was not created after 10 seconds");
                break;
            }
            count ++;
        }
        openSimulation(getSimulationTitlesList().get(getSimulationTitlesList().size() - 1));
    }

    public ArrayList<String> getSimulationTitlesList() {
        ArrayList<String> simulationTitlesList = new ArrayList<>();
        for (WebElement simulationTitleObj : listSimulationTitles) {
            String simulationTitle = getTextFieldValue(simulationTitleObj);
            simulationTitlesList.add(simulationTitle);
        }
        return simulationTitlesList;
    }

    public void closeSimulation(String simulationTitle) {
        // simulation title: Simulation #2
        Log.info("  - Closing simulation: " + simulationTitle);
        String walletXBtnLocator =
                "//button[@id='simulation-nav-item-" +
                (Integer.parseInt(simulationTitle.split("#")[1].trim()) - 1) +
                "-remove']//i[contains(@class,'fa-close')]";
        WebElement xButtonObj = driver.findElement(By.xpath(walletXBtnLocator));
        clickOnElement(xButtonObj);
    }

    public void clickAddWalletBtn() {
        Log.info("  - Creating new Wallet");
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
        Assert.assertTrue(getEvaluationStatus(), "Error: Evaluate action was not successful.");
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
        Log.info("  - Closing wallet: " + walletTitle);
        String walletXBtnLocator =
                    "//div[contains(@class,'wallet-')][contains(., '" +
                    walletTitle +
                    "')]//i[contains(@class,'fa-close')]";
        WebElement xButtonObj = driver.findElement(By.xpath(walletXBtnLocator));
        clickOnElement(xButtonObj);
    }

    public void closeAllWallets() {
        Log.info("  - Closing all Wallets from current Simulation");
        for (String walletTitle: getWalletTitlesList()) {
            closeWallet(walletTitle);
        }
    }

    public String getWalletCurrency(String walletTitle) {
        String walletCurrencyLocator =
                    "//div[contains(@class,'wallet-')][contains(., '" +
                    walletTitle +
                    "')]//div[@class='col'][1]";
        WebElement walletCurrencyObj = driver.findElement(By.xpath(walletCurrencyLocator));
        return getTextFieldValue(walletCurrencyObj);
    }

    public int getWalletInitialBalance(String walletTitle) {
        String walletBalanceLocator =
                    "//div[contains(@class,'wallet-')][contains(., '" +
                    walletTitle +
                    "')]//input[@type='number']";
        WebElement walletBalanceObj = driver.findElement(By.xpath(walletBalanceLocator));
        return Integer.parseInt(getValueAttrValue(walletBalanceObj));
    }

    public void setWalletBalance(String walletTitle, int walletBalance) {
        Log.info("  - Setting balance of '" + walletTitle + "' to: " + walletBalance);
        String walletBalanceLocator =
                    "//div[contains(@class,'wallet-')][contains(., '" +
                    walletTitle +
                    "')]//input[@type='number']";
        WebElement walletBalanceObj = driver.findElement(By.xpath(walletBalanceLocator));
        setTextFieldValue(walletBalanceObj, String.valueOf(walletBalance));
    }

    public ArrayList<String> getWalletFunctionsByTitleList(String walletTitle) {
        ArrayList<String> walletFunctionsList = new ArrayList<>();
        String walletFunctionsLocator =
                    "//div[contains(@class,'wallet-')][contains(., '" +
                    walletTitle +
                    "')]//button[contains(@class,'action-button')]";
        List<WebElement> listWalletFunctionsObj = driver.findElements(By.xpath(walletFunctionsLocator));
        for (WebElement functionNameObj : listWalletFunctionsObj) {
            String functionName = getTextFieldValue(functionNameObj);
            walletFunctionsList.add(functionName);
        }
        return walletFunctionsList;
    }

    public ArrayList<String> getWalletFunctionsByNumberList(int walletNumber) {
        ArrayList<String> walletFunctionsList = new ArrayList<>();
        String walletFunctionsLocator =
                    "//div[substring(@class,string-length(@class)-string-length('wallet-"+
                    (walletNumber-1) +
                    "')+1)='wallet-" +
                    (walletNumber-1) +
                    "']//button[contains(@class,'action-button')]";
        List<WebElement> listWalletFunctionsObj = driver.findElements(By.xpath(walletFunctionsLocator));
        for (WebElement functionNameObj : listWalletFunctionsObj) {
            String functionName = getTextFieldValue(functionNameObj);
            walletFunctionsList.add(functionName);
        }
        return walletFunctionsList;
    }

    private void clickWalletFunction(String walletTitle, String functionTitle) {
        Log.info("  - Clicking on '" + functionTitle + "' function for wallet: " + walletTitle);
        String walletFunctionLocator =
                     "//div[contains(@class,'wallet-')][contains(., '" +
                    walletTitle +
                    "')]//button[contains(text(),'" +
                    functionTitle +
                    "')]";
        WebElement walletFunctionObj = driver.findElement(By.xpath(walletFunctionLocator));
        clickOnElement(walletFunctionObj);
    }

    public void createMultipleWallets(int totalNumberOfWallets) {
        Log.info("  - Creating wallets: " + totalNumberOfWallets + " in total");
        int noOfCreatedWallets = getWalletTitlesList().size();
        while (noOfCreatedWallets < totalNumberOfWallets) {
            clickAddWalletBtn();
            waitABit(250);
            noOfCreatedWallets = getWalletTitlesList().size();
        }
    }

    public void createAction(String walletTitle, String walletFunction) {
        // create action and wait for action to be displayed on UI
        Log.info("  - Creating new action: " + walletTitle + ":" + walletFunction);
        int noOfConfiguredActions = getActionTitlesList().size();
        int expectedNoOfConfiguredActions = noOfConfiguredActions + 1;
        clickWalletFunction(walletTitle, walletFunction);
        int count = 0;
        while (noOfConfiguredActions < expectedNoOfConfiguredActions) {
            noOfConfiguredActions = getActionTitlesList().size();
            waitABit(250);
            count ++;
            if (count > 12) {
                Assert.fail("Error: waitTimeout --> The action was not created in 3 seconds.");
            }
        }
    }

    public ArrayList<String> getActionTitlesList() {
        // returns the titles of all visible actions (Ex: 'Wallet #1: scheduleCollection')
        ArrayList<String>actionTitlesList = new ArrayList<String>();
        for (WebElement actionTitleObj : listActionTitles) {
            String actionTitle = getTextFieldValue(actionTitleObj);
            actionTitlesList.add(actionTitle);
        }
        return actionTitlesList;
    }

    public void closeActionByNumber(int actionNumber) {
        Log.info("  - Closing action with number: " + actionNumber);
        String aciontXBtnLocator =
                "//div[contains(@class,'action-')]//div[contains(@class,'badge')][contains(text(),'" +
                actionNumber +
                "')]//parent::div//i[contains(@class,'fa-close')]";
        WebElement xButtonObj = driver.findElement(By.xpath(aciontXBtnLocator));
        clickOnElement(xButtonObj);
    }

    public void closeActionByTitle(String actionTitle) {
        Log.info("  - Closing action with title: " + actionTitle);
        String aciontXBtnLocator =
                "//div[contains(@class,'action-')]//div[@class='card-body'][contains(.,'" +
                actionTitle +
                "')]//i[contains(@class,'fa-close')]";
        WebElement xButtonObj = driver.findElement(By.xpath(aciontXBtnLocator));
        clickOnElement(xButtonObj);
    }

    public ArrayList<String> getActionByNumberFunctionsList(int actionNumber) {
        //action-ids starts form 0 when action numbers (in UI) starts from 1
        ArrayList<String> actionFunctionsList = new ArrayList<String>();
        String actionFunctionsLocator =
                 "//div[contains(@class,'action-" +
                (actionNumber - 1) +
                "')]//div[@class='form-group']/label[not(ancestor::div[@class='nested'])]";
        List<WebElement> listActionFunctionsObj = driver.findElements(By.xpath(actionFunctionsLocator));
        for (WebElement actionFunctionObj : listActionFunctionsObj) {
            String actionName = getTextFieldValue(actionFunctionObj);
            actionFunctionsList.add(actionName);
        }
        return actionFunctionsList;
    }

    public List<Integer> getActionNumbersByName(String actionName) {
        Log.info("  - Getting the list of action numbers of the actions with title: " + actionName);
        ArrayList<Integer> actionNumbersList = new ArrayList<>();
        String aciontNumbersLocator =
                "//div[contains(@class,'action-')]//div[@class='card-body'][contains(.,'" +
                actionName +
                "')]//ancestor::div[contains(@class,'badge')]";
        List<WebElement> listActionNumbersObj = driver.findElements(By.xpath(aciontNumbersLocator));
        for (WebElement actionNumbersObj : listActionNumbersObj) {
            int actionNumber = Integer.parseInt(getTextFieldValue(actionNumbersObj));
            actionNumbersList.add(actionNumber);
        }
        return actionNumbersList;
    }

    public String getActionNameByNumber(int actionNumber) {
        Log.info("  - Getting the name of the action with number: " + actionNumber);
        String actionTitleLocator =
                    "//div[contains(@class,'action-" +
                    actionNumber +
                    "')]//h3";
        WebElement actionTitleObj = driver.findElement(By.xpath(actionTitleLocator));
        return getTextFieldValue(actionTitleObj);
    }

    public void closeAllActions() {
        int noOfConfiguredActions = getActionTitlesList().size();
        while (noOfConfiguredActions > 0) {
            closeActionByNumber(1);
            waitABit(250);
            noOfConfiguredActions = getActionTitlesList().size();
        }
        Log.info("  - All actions were successfully closed");
    }

    public void fillBasicActionParameters(int actionNumber, String parameterTitle, String parameterValue) {
        Log.info("  - Set parameter value: " + parameterValue + "; actionNo: " + actionNumber + " - " + parameterTitle);
        String actionParameterLocator =
                        "//div[contains(@class,'action-" +
                        (actionNumber - 1) +
                        "')]//label[text()='" +
                        parameterTitle +
                        "'][not(ancestor::div[@class='nested'])]//parent::div//input";
        WebElement actionParameterObj = driver.findElement(By.xpath(actionParameterLocator));
        setTextFieldValue(actionParameterObj, parameterValue);
    }

    public void fillStringActionParameters(int actionNumber, String parameterTitle, String parameterValue) {
        Log.info("  - Set parameter value: " + parameterValue + "; actionNo: " + actionNumber + " - " + parameterTitle);
        String actionParameterLocator =
                "//div[contains(@class,'action-" +
                        (actionNumber - 1) +
                        "')]//input[not(ancestor::div[@class='form-group'])]";
        WebElement actionParameterObj = driver.findElement(By.xpath(actionParameterLocator));
        setTextFieldValue(actionParameterObj, parameterValue);
    }

    public void addGetValue(int actionNumber) {
        Log.info("  - Adding 1 new getValue parameter for Action no " + actionNumber);
        String getValueAddButtonLocator =
                "//div[contains(@class,'action-" +
                        (actionNumber - 1) +
                        "')]//label[text()='getValue']//parent::div//i[contains(@class,'fa-plus')]";
        WebElement getValueAddButtonObj = driver.findElement(By.xpath(getValueAddButtonLocator));
        clickOnElement(getValueAddButtonObj);
        waitABit(200);
    }

    public void removeGetValue(int actionNumber, int geValueRowNumber) {
        Log.info("  - Removing getValue parameter from position " + geValueRowNumber + " for Action no " + actionNumber);
        String getValueRemoveButtonLocator =
                "(//div[contains(@class,'action-" +
                        (actionNumber - 1) +
                        "')]//label[text()='getValue']//parent::div//i[contains(@class,'fa fa-trash')])[" +
                        geValueRowNumber +
                        "]";
        WebElement getValueRemoveButtonObj = driver.findElement(By.xpath(getValueRemoveButtonLocator));
        clickOnElement(getValueRemoveButtonObj);
        waitABit(200);
    }

    public void fillMultiValueParam(int actionNumber, String mainParamName, String secondaryParamName, String fieldValue) {
        Log.info("  - Fill " + mainParamName + "-" + secondaryParamName + " parameter for Action no " + actionNumber + " : " + fieldValue);
        String getValueRemoveButtonLocator =
                    "//div[contains(@class,'action-" +
                    (actionNumber - 1) +
                    "')]//input[contains(@class,'action-argument-0-" +
                    mainParamName +
                    "-" +
                    secondaryParamName +
                    "')]";
        WebElement getValueRemoveButtonObj = driver.findElement(By.xpath(getValueRemoveButtonLocator));
        setTextFieldValue(getValueRemoveButtonObj, fieldValue);
    }

    public void fillGetValueParam(int actionNumber, int getValueRowNumber, int getValueColNumber, String fieldValue) {
        // actionNumber, geValueRowNumber, geValueColNumber - param values should start from 1
        Log.info("  - Fill getValue parameter (row/col) " + getValueRowNumber + "/" + getValueColNumber + " for Action no " + actionNumber);
        String getValueRemoveButtonLocator =
                    "//div[contains(@class,'action-" +
                    (actionNumber - 1) +
                    "')][contains(.,'getValue')]//input[contains(@class,'action-argument-1-getValue-" +
                    (getValueRowNumber - 1) +
                    "-_" +
                    getValueColNumber +
                    "')]";
        WebElement getValueRemoveButtonObj = driver.findElement(By.xpath(getValueRemoveButtonLocator));
        setTextFieldValue(getValueRemoveButtonObj, fieldValue);
    }
}