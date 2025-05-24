package com.naturalia.backend;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.pageobjects.PageObjectCreateStay;

import java.time.Duration;

import static org.junit.Assert.*;

public class NaturaliaTests {
    private WebDriver driver;
    private final String BASE_URL = "http://localhost:5173/create";

    @Before
    public void setUp() {
//        System.setProperty("webdriver.chrome.driver", "/Users/williamesteban/Desktop/selenio/chromedriver-mac-x64/chromedriver");
        driver = new ChromeDriver();
    }

    @Test
    public void testCreateStaySuccess() throws InterruptedException {
        driver.get(BASE_URL);

        PageObjectCreateStay form = new PageObjectCreateStay(driver);
        form.enterName("test2");
        form.enterLocation("Testlandia");
        form.selectType("GLAMPING");
        form.enterPrice("150");
        form.uploadFakeImage();
        form.submitForm();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement success = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".success"))
        );
        assertTrue(success.isDisplayed());

    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
