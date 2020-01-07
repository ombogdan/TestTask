package task;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import settings.Settings;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertTrue;

public class Agrocontrol extends Settings {
    private AgrocontrolPage agPage = new AgrocontrolPage();


    @Test
    public void searchCourse() throws InterruptedException {
        driver.findElement(agPage.reportPage).click();
        driver.findElement(agPage.reportBy).click();
        driver.findElement(By.xpath("//*[contains(text(), 'По топливу')]")).click();
        driver.findElement(agPage.group).click();
        driver.findElement(By.xpath("//*[contains(text(), 'Трактора')]")).click();
        driver.findElement(agPage.object).click();
        driver.findElement(By.xpath("//*[contains(text(), 'МТЗ 22184ВН')]")).click();
        driver.findElement(agPage.date).clear();
        driver.findElement(agPage.date).sendKeys("2020-01-02 12:00");
        Thread.sleep(1000);
        driver.findElement(agPage.generateBtn).click();
        WebElement slider = driver.findElement(By.className("hsplitter"));
        Actions move = new Actions(driver);
        Action action = (Action) move.dragAndDropBy(slider, 0, 10).build();
        action.perform();
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"reportResultTemplate\"]/div/div/div/div[3]/div/div[1]/table/tbody/tr/td[2]\n"));

        System.out.println(rows.toString());

        driver.quit();
    }
}