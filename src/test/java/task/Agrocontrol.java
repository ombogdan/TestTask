package task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Agrocontrol extends Settings {
    private AgrocontrolPage agPage = new AgrocontrolPage();

    @Test
    public void getReport() throws InterruptedException, IOException {
        Actions move = new Actions(driver);
        ObjectMapper mapper = new ObjectMapper();
        File changedFile = new File("src/test/resources/changed.json");
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
        File actualFile = FileUtils.getFile("src/test/resources/standard.json");
        String str = FileUtils.readFileToString(actualFile, "utf-8");
        JSONObject json = JSON.parseObject(str);
        JSONArray arry = JSONArray.parseArray(json.get("actualData").toString());
        List<String> etalonDataArray = new ArrayList<>();
        for (int i = 0; i < arry.size(); i++) {
            JSONObject obj = (JSONObject) arry.get(i);
            etalonDataArray.add(obj.getString("Объект"));
            etalonDataArray.add(obj.getString("Ср.расход л/100км (в движении)"));
            etalonDataArray.add(obj.getString("Стоянки"));
            etalonDataArray.add(obj.getString("Время в движении"));
            etalonDataArray.add(obj.getString("Нач.уровень(л)"));
            etalonDataArray.add(obj.getString("Ср.скорость (км/час)"));
            etalonDataArray.add(obj.getString("Потрачено топлива(л)"));
            etalonDataArray.add(obj.getString("Слито(л)"));
            etalonDataArray.add(obj.getString("Потрачено топлива(л) (в движении)"));
            etalonDataArray.add(obj.getString("Пробег(км)"));
            etalonDataArray.add(obj.getString("Заправлено(л)"));
            etalonDataArray.add(obj.getString("Заправлено по АЗС(л)"));
            etalonDataArray.add(obj.getString("Ср.расход л/100км"));
            etalonDataArray.add(obj.getString("Остановки"));
            etalonDataArray.add(obj.getString("Остаток в баке(л)"));
            etalonDataArray.add(obj.getString("Макс.скорость (км/час)"));
            etalonDataArray.add(obj.getString("Моточасы"));
        }
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