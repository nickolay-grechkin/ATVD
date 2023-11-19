package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;


public class NTUTest {
    private WebDriver chromeDriver;

    private static final String baseUrl = "https://www.nmu.org.ua/ua/";

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
    public void testHeaderExists() {
        WebElement header = chromeDriver.findElement(By.id("heder"));

        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnForStudents() {
        WebElement forStudentButton = chromeDriver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a"));

        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = "content/student_life/students/";
        chromeDriver.get(baseUrl + studentPageUrl);

        WebElement searchButton = chromeDriver.findElement(By.tagName("input"));

        Assert.assertNotNull(searchButton);

        System.out.println(String.format("Name attribute: %s", searchButton.getAttribute("name")) +
                String.format("\nId attribute: %s", searchButton.getAttribute("id")) +
                String.format("\nType attribute: %s", searchButton.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchButton.getAttribute("value")) +
                String.format("\nPosition: (%d;%d)", searchButton.getLocation().x, searchButton.getLocation().y) +
                String.format("\nSize: %dx%d", searchButton.getSize().height, searchButton.getSize().width));

        searchButton.click();

        WebElement searchField = chromeDriver.findElement(By.id("gsc-i-id1"));
        String expectedValue = "I need info";
        searchField.sendKeys(expectedValue);

        String inputValue = searchField.getAttribute("value");

        Assert.assertEquals(expectedValue, inputValue);

        searchField.sendKeys(Keys.ENTER);

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), studentPageUrl);
    }

    @Test
    public void testSlider() {
        WebElement nextButton = chromeDriver.findElement(By.className("next"));

        WebElement nextButtonCss = chromeDriver.findElement(By.cssSelector("a.next"));

        Assert.assertEquals(nextButton, nextButtonCss);

        WebElement previousButton = chromeDriver.findElement(By.className("prev"));

        for (int i = 0; i < 20; i++) {
            if (nextButton.getAttribute("class").contains("disabled")) {
                Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
                Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
            } else {
                nextButton.click();
                Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
            }
        }
    }
}
