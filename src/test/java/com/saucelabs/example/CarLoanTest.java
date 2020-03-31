package com.saucelabs.example;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

public class CarLoanTest extends TestBase
{
    /**
     * Runs a simple test verifying if the comment input is functional.
     *
     * @throws InvalidElementStateException
     */
    @Test(dataProvider = "hardCodedDevices")
    public void calculateCarLoan(String platformName, String deviceName, String platformVersion, Method method)
    throws MalformedURLException
    {
        long time1, time2, time3, time4;

        AndroidDriver driver = createDriver(platformName, platformVersion, deviceName, method.getName());
        time1 = System.currentTimeMillis();

        WebElement etLoanAmount = driver.findElement(By.id("io.billmeyer.loancalc:id/etLoanAmount"));
        WebElement etEditInterest = driver.findElement(By.id("io.billmeyer.loancalc:id/etEditInterest"));
        WebElement etSalesTax = driver.findElement(By.id("io.billmeyer.loancalc:id/etSalesTax"));
        WebElement etTerm = driver.findElement(By.id("io.billmeyer.loancalc:id/etTerm"));
        WebElement etDownPayment = driver.findElement(By.id("io.billmeyer.loancalc:id/etDownPayment"));
        WebElement etTradeIn = driver.findElement(By.id("io.billmeyer.loancalc:id/etTradeIn"));
        WebElement etFees = driver.findElement(By.id("io.billmeyer.loancalc:id/etFees"));
        WebElement btnCalculate = driver.findElement(By.id("io.billmeyer.loancalc:id/btnCalculate"));
        WebElement tvLoanTotal = driver.findElement(By.id("io.billmeyer.loancalc:id/tvLoanTotal"));
        WebElement tvMonthlyPayment = driver.findElement(By.id("io.billmeyer.loancalc:id/tvMonthlyPaymentVal"));
        WebElement tvTotalInterest = driver.findElement(By.id("io.billmeyer.loancalc:id/tvLoanInterestVal"));
        WebElement tvTotalCost = driver.findElement(By.id("io.billmeyer.loancalc:id/tvLoanTotalCostVal"));

        time2 = System.currentTimeMillis();

        // Set the input values for our loan calculation...
        etLoanAmount.sendKeys("25000");
        etEditInterest.sendKeys("3.42");
        etSalesTax.sendKeys("8");
        etTerm.sendKeys("60");
        etDownPayment.sendKeys("500");
        etTradeIn.sendKeys("7500");
        etFees.sendKeys("300");
        driver.getScreenshotAs(OutputType.FILE);

        time3 = System.currentTimeMillis();

        btnCalculate.click();
        driver.getScreenshotAs(OutputType.FILE);

        // Check if within given time the correct result appears in the designated field.
        ExpectedCondition<Boolean> expected = ExpectedConditions.textToBePresentInElement(tvLoanTotal, "20,370.97");
        expected = ExpectedConditions.textToBePresentInElement(tvMonthlyPayment, "339.52");
        expected = ExpectedConditions.textToBePresentInElement(tvLoanTotal, "20,370.97");
        expected = ExpectedConditions.textToBePresentInElement(tvTotalInterest, "1,670.97");
        expected = ExpectedConditions.textToBePresentInElement(tvTotalCost, "28,370.97");

        time4 = System.currentTimeMillis();

        System.out.printf("Locating elements took %.2f secs\n", (time2 - time1) / 1000f);
        System.out.printf("Populating elements took %.2f secs\n", (time3 - time2) / 1000f);
        System.out.printf("Asserting results took %.2f secs\n", (time4 - time3) / 1000f);
        System.out.printf("Total test execution took %.2f secs\n", (time4 - time1) / 1000f);

        WebDriverWait wait = new WebDriverWait(driver, 30);
        try
        {
            wait.until(expected);
        }
        catch (Throwable t)
        {
            System.err.println("Expected Condition Not Met: " + t.getMessage());
        }
    }
}