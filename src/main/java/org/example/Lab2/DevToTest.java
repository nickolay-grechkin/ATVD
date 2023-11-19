package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class DevToTest {
    private WebDriver chromeDriver;

    private static final String baseUrl = "https://dev.to/";

    @BeforeTest(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeTest(dependsOnMethods = "setUp")
    public void preconditions() {
        if (chromeDriver == null) {
            throw new IllegalStateException("WebDriver is not initialized.");
        }
        chromeDriver.get(baseUrl);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        if (chromeDriver != null) {
            chromeDriver.quit();
        }
    }

    @Test
    public void testHeaderElementExists() {
        WebElement header = chromeDriver.findElement(By.className("crayons-header__container"));

        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnCreateAccountButton() {
        WebElement createPostButton = chromeDriver.findElement(By.className("c-cta"));

        Assert.assertNotNull(createPostButton);
        createPostButton.click();

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testInputValue() {
        WebElement input = chromeDriver.findElement(By.className("crayons-header--search-input"));
        String expectedValue = "How to learn Java language?";
        input.sendKeys(expectedValue);

        String inputValue = input.getAttribute("value");

        Assert.assertEquals(inputValue, expectedValue);
    }
}
