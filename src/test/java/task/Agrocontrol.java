package task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import settings.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Agrocontrol extends Settings {
    private AgrocontrolPage agPage = new AgrocontrolPage();

    @Test
    public void getReport() throws InterruptedException, IOException {
        Actions move = new Actions(driver);
        ObjectMapper mapper = new ObjectMapper();
        Gson gson = new Gson();
        File actualFile = new File("C:\\Users\\omelc\\IdeaProjects\\agroTest\\src\\test\\java\\data\\actual.json");
        File changedFile = new File("C:\\Users\\omelc\\IdeaProjects\\agroTest\\src\\test\\java\\data\\changed.json");
        File etalonFile = new File("C:\\Users\\omelc\\IdeaProjects\\agroTest\\src\\test\\java\\data\\etalon.json");
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
        Action action = move.dragAndDropBy(slider, 0, 10).build();
        action.perform();
        List<WebElement> data = driver.findElements(By.xpath("//*[@id=\"reportResultTemplate\"]/div/div/div/div[3]/div/div[1]/table/tbody/tr/td[2]"));
        List<String> actualDataArray = new ArrayList<>();
        for (WebElement p : data) {
            actualDataArray.add(String.valueOf(p.getText()));
            try {
                mapper.writeValue(actualFile, actualDataArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedReader etalonBuffered = new BufferedReader(new FileReader(etalonFile));
        String etalonLine = etalonBuffered.readLine();
        Type etalonlList = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> etalonDataArray = gson.fromJson(etalonLine, etalonlList);
        List<String> changedDataArray = new ArrayList<>();
        for (int j = 0; j < etalonDataArray.size(); j++) {
            if (!etalonDataArray.get(j).equals(actualDataArray.get(j))) {
                changedDataArray.add(actualDataArray.get(j));
                mapper.writeValue(changedFile, changedDataArray);
            }
        }
    }
}