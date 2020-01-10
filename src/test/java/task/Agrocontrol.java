package task;

import com.alibaba.fastjson.JSONObject;
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
        BufferedReader etalonBuffered = new BufferedReader(new FileReader(etalonFile));
        String etalonLine = etalonBuffered.readLine();
        Type etalonlList = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> etalonDataArray = gson.fromJson(etalonLine, etalonlList);
        List<WebElement> data = driver.findElements(agPage.dataTable);
        List<WebElement> name = driver.findElements(agPage.nameTable);
        JSONObject nameJSON = new JSONObject();
        for (int j = 0; j < data.size(); j++) {
            try {
                if (!etalonDataArray.get(j).equals(data.get(j).getText())) {
                    nameJSON.put(name.get(j).getText(), data.get(j).getText());
                    mapper.writeValue(changedFile, nameJSON);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}