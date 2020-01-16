package task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Agrocontrol extends Settings {
    private AgrocontrolPage agPage = new AgrocontrolPage();

    @Test
    public void getReport() throws IOException, InterruptedException {
        Actions move = new Actions(driver);
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper jackson = new ObjectMapper();
        JSONObject nameJSON = new JSONObject();
        File changedFile = new File("src/test/resources/changed.json");
        File actualFile = FileUtils.getFile("src/test/resources/actual.json");
        File standardFile = FileUtils.getFile("src/test/resources/standard.json");
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
        String standardStr = FileUtils.readFileToString(standardFile, "utf-8");
        JSONObject json = JSON.parseObject(standardStr);
        List<WebElement> data = driver.findElements(agPage.dataTable);
        List<WebElement> name = driver.findElements(agPage.nameTable);
        for (int j = 0; j < data.size(); j++) {
            try {
                nameJSON.put(name.get(j).getText(), data.get(j).getText());
                mapper.writeValue(actualFile, nameJSON);
                String actualStr = FileUtils.readFileToString(actualFile, "utf-8");
                JSONObject actualJson = JSON.parseObject(actualStr);
                JsonNode beforeNode = jackson.readTree(String.valueOf(json));
                JsonNode afterNode = jackson.readTree(String.valueOf(actualJson));
                JsonNode patchNode = JsonDiff.asJson(beforeNode, afterNode);
                String diff = patchNode.toString();
                mapper.writeValue(changedFile, diff);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}