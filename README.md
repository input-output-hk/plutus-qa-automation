# plutus-qa-automation

## Synopsis

This project is used in order to test automatically most of the Plutus Playground UI functional scenarios.
Technologies used: Java, TestNG, Selenium, Maven, Log4j.

## Usage

- **_Set Up_**:
    - In order to have the framework work on your windows machine, follow the below steps:
        - install Java JDK (v. 10) 
        - install Maven
        - create a new project using the current git repository

- **_General considerations_**:
    - All the screenshots for the last run can be found in "**screenshots**" directory. Screenshots are taken for each failed Test.
    - There is also a more visual HTML report that can be found inside "**ExtentHtmlReports**" directory.
    - All the script logs for the last run can be found in "**logs**" directory.
    
- **_Creating input JSONs considerations_**:
    - when creating a new json (to be used as input for a script), use an existing one as model
    - each json should have these nodes/levels: 
        - contract
        - listOfSimulations 
            - listOfWallets
            - listOfActions 
                - listOfParameters
    - each Action Parameter should have a type defined - basic, string or multivalue 
        - basic - EX: crowdfunding/contribute/campaignDeadline - 1 parameter with 1 value 
        - string - EX: game/guess - 1 parameter with an input field (no title for the field)
        - multivalue 
            - EX - crowdfunding/payToWallet_/getValue - 1 parameter with multiple rows with multiple input values per row
            - EX - vesting/vestFunds/vestingTranche2 - 1 parameter with multiple secondary parameters  
    - if you want to add the same Simulation, Wallet or Action more than 1 time, make sure to add the "addMultipleTimes" parameter to it
        - EX: Crowdfundig_2Simulations_15Wallets.json
    - if you expect that 1 Action to generate an error message inside the Transactions Logs, make sure to add the "expectedError" parameter
        - EX: Game_1Simulation_2Wallets.json
    
- **_Running Scripts using MAVEN_**
    - install Chrome/Firefox on local PC
    - run the scripts using Maven commands:
        - **frontend scripts parameters**:
            - runner - supported values: LOCALHOST (using local browser), REMOTE (using selenium grid)
            - gridAddress - location of the selenium grid hub (when 'runner' = REMOTE)
            - browserType - supported values: CHROME, FIREFOX
            - env - supported values: Enums.Environment (src/main/utils/Enums)
            - loopCount - number of times a test will be run (you can find more details below)
            - headless - supported values: true, false (you can find more details below)
        - run single script:
            ```
            mvn clean test -Dtest=SCRIPT_NAME -Drunner=BROWSER_LOCATION -DgridAddress=GRID_LOCATION -DbrowserType=BROWSER_NAME -Denv=ENVIRONMENT
            ```     
        - run multiple scripts:
            - run all scripts: 
                ```
                mvn clean test -Drunner=REMOTE -DgridAddress=http://localhost:4444/wd/hub
                ```
            - run specific test suite: 
                ```
                mvn clean test -Drunner=LOCALHOST -Dsurefire.suiteXmlFiles=./src/test/resources/testSuites/frontEnd/basicRegression.xml
                ``` 
        - run the same Test in a loop (ex: run the Transactions test inside the SendMultipleTransactionsTest in a loop)
            - DloopCount tells how many times to execute all the configured transactions in a loop
            - Note: twoAccountsMultipleTransactions class has 2 annotations:
                - @Test - this is a TestNg test
                - @CanRunMultipleTimes - this Test will be run multiple times (loopCount times; default loopCount = 1)
                - loopCount value will be considered only when running a testsuite
            ```
            mvn clean test -Drunner=REMOTE -DgridAddress=http://localhost:4444/wd/hub -Dsurefire.suiteXmlFiles=./src/test/resources/testSuites/frontEnd/sanity.xml -DbrowserType=CHROME -Denv=PROD -DloopCount=10
            ```                      
        - run scripts locally using Chrome Headless browser (runner = LOCALHOST AND browserType = CHROME AND headless=true)
            ```
            mvn clean test -Denv=PROD -Drunner=LOCALHOST -DbrowserType=CHROME -Dheadless=true -Dtest=HeaderLinksTest
            ```
    - debug failures (smile)
        - for each failed frontend script there will be a screenshot in 'screenshots' folder
            - this folder is cleaned before each run
        - each script will have a log file inside the 'logs' folder
                
- **_Running Scripts from IDE_**
    - create a new project using the git repository
    - install Chrome/Firefox on local PC
    - frontend scripts:
        - select where the browser is located (default values are set here: src/test/java/io.iohk/uiTests/BaseTest/@BeforeClass - @Optional parameters)
            - Option 1 - using local browser (runner = LOCALHOST)
            - Option 2 - using local Docker environment configured with Selenium Grid (runner = REMOTE, gridAddress=http://localhost:4444/wd/hub)
         - select if you want the browser to be in headless mode or not
            - in order to use the browser in headless mode, you need to set:
                - runner = LOCALHOST 
                - browserType = CHROME 
                - set the (@optional) parameter headless = true in BaseTest / public void beforeClass
        - run tests using the 'run' right click option on: single test, folder, testsuite
            - some @Tests have the option to run the configured transactions in a loop
                - 'invocationCount' parameter sets how many times that test will be run
            - modify the value of the 'invocationCount' parameter as you want
            - default values for 'environment' and 'browserType' are taken from src/test/java/io/iohk/frontendTests/BaseTest
                - 'public static void beforeClass(@Optional("PROD") String env, @Optional("CHROME") String browser)'
"# plutus-qa-automation" 
