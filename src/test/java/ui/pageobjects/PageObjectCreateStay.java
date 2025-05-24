package ui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageObjectCreateStay {
    private WebDriver driver;

    public PageObjectCreateStay(WebDriver driver) {
        this.driver = driver;
    }

    public void enterName(String name) {
        driver.findElement(By.name("name")).sendKeys(name);
    }

    public void enterLocation(String location) {
        driver.findElement(By.name("location")).sendKeys(location);
    }

    public void selectType(String typeValue) {
        WebElement dropdown = driver.findElement(By.name("type"));
        dropdown.findElement(By.cssSelector("option[value='" + typeValue + "']")).click();
    }

    public void enterPrice(String price) {
        driver.findElement(By.name("pricePerNight")).sendKeys(price);
    }

    public void uploadFakeImage() {
        WebElement upload = driver.findElement(By.cssSelector("input[type='file']"));
        upload.sendKeys("/Users/williamesteban/Desktop/ImageTest.png"); // Cambiar por una imagen válida local
    }

    public void submitForm() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public WebElement getSuccessMessage() {
        return driver.findElement(By.cssSelector(".success"));
    }

    public WebElement getErrorMessage() {
        return driver.findElement(By.cssSelector(".error"));
    }
}
