package settings;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class Settings {
    protected ChromeDriver driver;
    ChromeOptions ops = new ChromeOptions();


    @Before
    public void setupAndLogin() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(ops);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://demo.agrocontrol.net/login");
        WebElement username = driver.findElementByName("email");
        WebElement password = driver.findElementByName("password");
        username.sendKeys("test");
        password.sendKeys("test");
        password.submit();
    }

    @After
    public void quit() {
        driver.quit();
    }
}
