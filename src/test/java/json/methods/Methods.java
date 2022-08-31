package json.methods;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import json.driver.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.json.TypeToken;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Methods extends BaseTest {
    protected WebDriverWait webWait;
    protected WebDriver driver;
    protected JavascriptExecutor jsDriver;

    public Map<String, Object> elementMap;
    private final static String PATH = "element";

    public Methods() {
        webWait = BaseTest.webWait;
        driver = BaseTest.driver;
        jsDriver = BaseTest.jsDriver;
        initElementMap(getFileList());
    }

    public void moveToElement(WebElement element) {
        try {
            Actions action = new Actions(driver);
            action.scrollToElement(element).build().perform();
        } catch (Exception e) {
            jsDriver.executeScript("arguments[0].scrollIntoView(true);", element);

        }
    }

    public WebElement findElement(String str) {

        WebElement element = webWait(str);
        if (!isVisible(str)) {
            moveToElement(element);
        }
        return element;

    }

    public WebElement webWait(String str){
        By by = getByTypeWithMap(str);
        return webWait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public List<WebElement> findElements(String str) {
        By by=getByTypeWithMap(str);
        return webWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public void sendKeys(String by, String str) {
        WebElement element = findElement(by);
        element.clear();
        try {
            element.sendKeys(str);
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value='" + str + "';", findElement(by));
        }

    }

    public String getText(String by) {
        return findElement(by).getText();
    }

    public void click(String by) {
        try {
            findElement(by).click();
        } catch (ElementNotInteractableException e) {
            clickWithJS(by);
        }
        waitBySeconds(1);
    }

    public void click(WebElement element) {
        try {
            element.click();
        } catch (ElementNotInteractableException e) {
            clickWithJS(element);
        }
        waitBySeconds(1);
    }

    public String getValue(String str){
        return findElement(str).getAttribute("value");
    }


    public void clickWithJS(String by) {
        jsDriver.executeScript("arguments[0].click();", findElement(by));
    }

    public void clickWithJS(WebElement element) {
        jsDriver.executeScript("arguments[0].click();", element);
    }

    public void hoverToElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public List<String> getHrefs(String by) {
        List<WebElement> elemets = findElements(by);
        List<String> hrefs = new ArrayList<>();
        for (WebElement element : elemets) {
            hrefs.add(element.getAttribute("href"));
        }
        return hrefs;
    }

    public void randPickAndAddFav(List<String> str, String button1, String button2) {
        Random rand = new Random();
        int number;
        for (int i = 0; i < 10; i++) {
            number = rand.nextInt(str.size());
            str.remove(number);
        }

    }

    public WebElement randPick(String by){
        List<WebElement> elements=findElements(by);
        Random rand = new Random();
        return elements.get(rand.nextInt(elements.size()));
    }

    public void randPickVisibleOnes(String titles1,String titles2){
        List<WebElement> elements1 = findElements(titles1);
        System.out.println(elements1.size());
        List<WebElement> elements2 = findElements(titles2);
        System.out.println(elements2.size());
        elements2.addAll(elements1);
        System.out.println(elements2.size());
        Random rand = new Random();
        int random;
        while (true){
            random=rand.nextInt(elements2.size());
            if (elements2.get(random).isDisplayed()){
                elements2.get(random).click();
                break;
            }
        }
    }

    public int findMaxPrice(List<WebElement> itemsWithPrice) {
        List<String> prices = new ArrayList<>();
        String itemPrice;
        double max = 0.1;
        double step;
        int j = 0;
        for (int i = 0; i < itemsWithPrice.size(); i++) {
            itemPrice = itemsWithPrice.get(i).getText().trim();
            itemPrice = itemPrice.replace("TL", "");
            itemPrice = itemPrice.replace(".", "");
            prices.add(i, itemPrice.replace(",", "."));
            System.out.println(prices.get(i));
            System.out.println(Double.parseDouble(prices.get(i)));
            step = Double.parseDouble(prices.get(i));
            if (max < step) {
                max = step;
                j = i;
            }
        }
        return j;
    }

    public void getTextAndTrim(WebElement beforeTrim) {
        String afterTrim = beforeTrim.getText().trim();

    }

    public void waitBySeconds(long seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isVisible(String by) {
        try {
            driver.findElement(getByTypeWithMap(by));
            return true;
        }
        catch (Exception e) {
           return false;
        }
    }

    public void isVisibleAndClick(String by) {
        if (isVisible(by)) {
            click(by);
        }
    }

    public void selectVisibleOne(String first, String second) {
        if (isVisible(first)) {
            click(first);
        } else if (isVisible(second)) {
            click(second);
        }
    }

    public void switchFrameWithIndex(int i) {
        driver.switchTo().frame(i);
    }

    public void switchParentFrame() {
        driver.switchTo().parentFrame();
    }

    public void check(String by, String text) {
        if (!isVisible(by)) {
            throw new NullPointerException("element bulunamadı");
        }
    }

    public void checkCurrentUrl(String text) {
        Assertions.assertTrue(driver.getCurrentUrl().contains(text));
    }

    public boolean isDisplayed(String by) {
        return findElement(by).isDisplayed();
    }

    public By getByTypeWithMap(String keyword) {
        Elements elements = (Elements) elementMap.get(keyword);
        Map<String, By> map = initByMap(elements.getValue());
        return map.getOrDefault(elements.getLocatorType(), null);
    }

    public Map<String, By> initByMap(String locatorValue) {
        Map<String, By> map = new HashMap<>();
        map.put("id", By.id(locatorValue));
        map.put("css", By.cssSelector(locatorValue));
        map.put("xpath", By.xpath(locatorValue));
        map.put("name",By.name(locatorValue));
        return map;
    }

    public void initElementMap(File[] fileList) {
        elementMap = new ConcurrentHashMap<>();
        Type elementType = new TypeToken<List<Elements>>() {
        }.getType();
        Gson gson = new Gson();
        List<Elements> elementInfoList;
        for (File file : fileList) {
            try {
                elementInfoList = gson.fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream().forEach(elements -> elementMap.put(elements.getKeyword(), elements));
            } catch (FileNotFoundException e) {
            }
        }
    }

    public File[] getFileList() {
        File[] fileList = new File(this.getClass().getClassLoader().getResource(PATH).getFile())
                .listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));
        if (fileList == null) {
            throw new NullPointerException("Belirtilen dosya bulunamadı.");
        }
        return fileList;
    }




    public static class Elements{
        protected String keyword;
        protected String type;
        protected String value;

        public String getKeyword() {
            return this.keyword;
        }
        public String getLocatorType() {
            return this.type;
        }
        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "Elements{" +
                    "keyword='" + keyword + '\'' +
                    ", type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }


}
