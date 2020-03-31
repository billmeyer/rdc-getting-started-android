package com.saucelabs.example;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Simple TestNG test which demonstrates being instantiated via a DataProvider in order to supply multiple browser combinations.
 *
 * @author Bill Meyer
 */
public class TestBase
{
    protected static final boolean realDeviceTesting = false;
    protected static final String userName = System.getenv("SAUCE_USERNAME");
    protected static final String accessKey = System.getenv("SAUCE_ACCESS_KEY");

    /**
     * ThreadLocal variable which contains the  {@link WebDriver} instance which is used to perform browser interactions with.
     */
    private ThreadLocal<AndroidDriver> androidDriverThreadLocal = new ThreadLocal<>();

    /**
     * DataProvider that explicitly sets the browser combinations to be used.
     *
     * @param testMethod
     * @return Two dimensional array of objects with browser, version, and platform information
     */
    @DataProvider(name = "hardCodedDevices", parallel = true)
    public static Object[][] sauceDeviceDataProvider(Method testMethod)
    {
        /**
         * Create an array of test OS/Browser/Screen Resolution combinations we want to test on.
         * @see https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options#TestConfigurationOptions-SpecifyingtheScreenResolution
         */

        // @formatter:off
        if (realDeviceTesting == true)
        {
                return new Object[][] {
                        new Object[]{"Android", "Google.*", "9"}
                };
        }
        else
        {
            return new Object[][]{
                    new Object[]{"Android", "Android GoogleAPI Emulator", "9.0"},
                    new Object[]{"Android", "Android GoogleAPI Emulator", "10.0"}
            };
        }
        // @formatter:on
    }

    protected void annotateJob(String text)
    {
        /**
         * Example of using the JavascriptExecutor to annotate the job execution as it runs
         *
         * @see https://wiki.saucelabs.com/display/DOCS/Annotating+Tests+with+Selenium%27s+JavaScript+Executor
         */

        androidDriverThreadLocal.get().executeScript("sauce:context=" + text);
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the platformName,
     * platformVersion and deviceName parameters, and which is configured to run against ondemand.saucelabs.com, using
     * the userName and access key populated by the authentication instance.
     *
     * @param platformName    Represents the platformName to be used as part of the test run.
     * @param platformVersion Represents the platformVersion of the platformName to be used as part of the test run.
     * @param deviceName      Represents the operating system to be used as part of the test run.
     * @param methodName      Represents the name of the test case that will be used to identify the test on Sauce.
     * @return
     * @throws MalformedURLException if an error occurs parsing the url
     */
    protected AndroidDriver createDriver(String platformName, String platformVersion, String deviceName, String methodName)
    throws MalformedURLException
    {
        URL url = null;
        DesiredCapabilities caps = new DesiredCapabilities();

        // set desired capabilities to launch appropriate platformName on Sauce
        // For real device testing, connect to one URL using a certain set of credentials...
        url = new URL("https://" + userName + ":" + accessKey + "@ondemand.us-west-1.saucelabs.com/wd/hub");
        caps.setCapability("appiumVersion", "1.16.0");
        caps.setCapability("app", "sauce-storage:LoanCalc.apk");
        caps.setCapability("automationName", "uiautomator2");
        caps.setCapability("platformName", platformName);
        caps.setCapability("platformVersion", platformVersion);
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("name", methodName);

        // Launch the remote platformName and set it as the current thread

        long start = System.currentTimeMillis();
        AndroidDriver driver = new AndroidDriver(url, caps);
        long stop = System.currentTimeMillis();
        info(driver, "Device allocation took %d secs\n", (stop - start) / 1000);

        androidDriverThreadLocal.set(driver);

        return androidDriverThreadLocal.get();
    }

    /**
     * Method that gets invoked after test.
     * Sets the job status (PASS or FAIL) and closes the browser.
     */
    @AfterMethod
    public void tearDown(ITestResult result)
    throws Exception
    {
        AppiumDriver driver = androidDriverThreadLocal.get();

        if (driver != null)
        {
            boolean success = result.isSuccess();

            reportSauceLabsMobileResult(driver, success);
            driver.quit();
        }
    }

    /**
     * Logs the given line in the job’s commands list. No spaces can be between sauce: and context.
     */
    public static void info(RemoteWebDriver driver, String format, Object... args)
    {
        System.out.printf(format, args);
        return; // Not currently implemented
//        String msg = String.format(format, args);
//        ((JavascriptExecutor) driver).executeScript("sauce:context=" + msg);
    }

    public static void reportSauceLabsMobileResult(RemoteWebDriver driver, boolean status)
    {
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (status ? "passed" : "false"));
    }
}
