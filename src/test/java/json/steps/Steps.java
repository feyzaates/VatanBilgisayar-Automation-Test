package json.steps;

import com.thoughtworks.gauge.Step;
import json.driver.BaseTest;
import json.methods.Methods;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;


public class Steps extends BaseTest {
    public Methods methods;
    JavascriptExecutor js = (JavascriptExecutor) driver;
    String value;

    public Steps() {
        methods = new Methods();
    }

    @Step("<sec> second wait")
    public void waitBySeconds(long second) throws RuntimeException {
        methods.waitBySeconds(second);
    }

    @Step("<key> click")
    public void clickById(String str) {
        methods.click(str);
    }

    @Step("<key> sendKeys <send>")
    public void sendKey(String str, String key) {
        methods.sendKeys(str, key);
    }

    @Step("<key> pick randomly and hover")
    public void pickAndHover(String str){
        WebElement element = methods.randPick(str);
        methods.hoverToElement(element);
    }

    @Step("<key> and <key> pick randomly and click")
    public void pickAndClick(String str1,String str2){
        methods.randPickVisibleOnes(str1,str2);
    }
    @Step("<key> pick randomly, hover and click")
    public void randHoverClick(String str){
        WebElement element = methods.randPick(str);
        methods.hoverToElement(element);
        element.click();
    }
    @Step("<key> pick randomly and click")
    public void randClick(String str){
        if (!(methods.isVisible("empty"))){
            WebElement element = methods.randPick(str);
            methods.click(element);
        }
    }
    @Step("<key> get value")
    public void getVal(String str){
        methods.webWait(str);
        this.value= methods.getValue(str);
    }

    @Step("<key> send value")
    public void paste(String str){
        methods.sendKeys(str,this.value);
    }


    @Step("<key> hover")
    public void hover(String str) {
        methods.hoverToElement(methods.findElement(str));
    }

    @Step("<key> sendKeys with js <key>")
    public void sendKeysWithJs(String str, String text) {
        js.executeScript("arguments[0].value='" + text + "';", methods.findElement(str));
    }

    @Step("<key> click if is visible")
    public void clickIfIsVisible(String str) {
        methods.isVisibleAndClick(str);
    }

    @Step("<key> is visible <key> pick randomly and click")
    public void clickRandIsVisible(String check,String str){
        if (methods.isVisible(check)){
            randClick(str);
        }
    }


    @Step("<index> switch the frame")
    public void switchFrame(int index) {
        methods.switchFrameWithIndex(0);
    }

    @Step("parent frame")
    public void parentFrame() {
        methods.switchParentFrame();
    }


}