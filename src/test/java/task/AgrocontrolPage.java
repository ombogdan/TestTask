package task;

import org.openqa.selenium.By;

class AgrocontrolPage {
    By reportPage = By.xpath("//*[@id=\"b-menu-1\"]/ul[1]/li[7]/a");
    By reportBy = By.xpath("//*[@id=\"leftColCont\"]/div/div/form/div[1]/div/div[1]/a/span[2]");
    By group = By.xpath("//*[@id=\"leftColCont\"]/div/div/form/div[8]/div/div[1]/a");
    By object = By.xpath("//*[@id=\"leftColCont\"]/div/div/form/div[8]/div/div[2]/a/span[1]");
    By date = By.xpath("//*[@id=\"leftColCont\"]/div/div/form/div[23]/div/p/input");
    By generateBtn = By.xpath("//*[@id=\"footerButtons\"]/button[2]");
    By data = By.className("ng-binding");
}
