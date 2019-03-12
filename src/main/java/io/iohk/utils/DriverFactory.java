package io.iohk.utils;

import io.iohk.utils.Enums.Browser;
import io.iohk.utils.Enums.Runner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {
    public static WebDriver createInstance(Browser browserType, Runner runner, String gridAddress, Boolean headless) throws MalformedURLException {
        WebDriver driver = null;

        if (browserType ==  Browser.CHROME) {
            switch (runner) {
                case LOCALHOST:
                    driver = createLocalChromeDriver(headless);
                    break;
                case REMOTE:
                    driver = createRemoteChromeDriver(gridAddress);
                    break;
            }
        }
        if (browserType ==  Browser.FIREFOX) {
            switch (runner) {
                case LOCALHOST:
                    driver = createLocalFirefoxDriver();
                    break;
                case REMOTE:
                    driver = createRemoteFirefoxDriver(gridAddress);
                    break;
            }
        }
        return driver;
    }

    private static WebDriver createLocalChromeDriver(boolean headless) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--incognito");
        options.addArguments("--disable-popup-blocking");
        if (headless) {
            options.addArguments("--headless");
        }
        WebDriver driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        return driver;
    }

    private static WebDriver createRemoteChromeDriver(String gridAddress) throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--incognito");
        options.addArguments("--disable-popup-blocking");
        WebDriver driver = new RemoteWebDriver(new URL(gridAddress), options);
        driver.manage().deleteAllCookies();
        return driver;
    }

    private static WebDriver createLocalFirefoxDriver() {
        System.setProperty("webdriver.gecko.driver", "src/main/resources/drivers/geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.disable_beforeunload", true);
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        return driver;
    }

    private static WebDriver createRemoteFirefoxDriver(String gridAddress) throws MalformedURLException {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.disable_beforeunload", true);
        WebDriver driver = new RemoteWebDriver(new URL(gridAddress), options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        return driver;
    }
}

