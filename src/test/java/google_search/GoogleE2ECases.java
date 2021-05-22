package google_search;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static google_search.Constants.*;
import static google_search.XpathConstants.*;

public class GoogleE2ECases {


    private WebDriver webDriver;

    @BeforeTest
    public void openGooglePage() {
        System.setProperty(CHROME_SELENIUM_DRIVER, DRIVER_PATH);
        webDriver = new ChromeDriver();
        webDriver.navigate().to(GOOGLE_PATH);
        webDriver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void searchBoxPresent() {
        assert webDriver.findElements(By.xpath(SEARCH_BOX_XPATH)).size() > 0;
    }

    @Test(priority = 2)
    public void searchButtonPresent() {
        assert webDriver.findElements(By.xpath(SEARCH_BUTTON_X_PATH)).size() > 0;
    }

    @Test(priority = 3)
    public void feelingLuckyButtonPresent() {
        assert webDriver.findElements(By.xpath(IMFEELINGLUCKY_XPATH)).size() > 0;

    }

    @Test(priority = 4)
    public void micOptionPresent() {
        assert webDriver.findElements(By.xpath(MIC_BUTTON_XPATH)).size() > 0;

    }

    @Test(priority = 5)
    public void isClearButtonPresentWithoutAnyText() {
        assert !webDriver.findElement(By.xpath(CLEAR_BUTTON_XPATH)).isDisplayed();
    }

    @Test(priority = 6)
    public void isClearButtonPresentWithText() {
        webDriver.findElement(By.xpath(SEARCH_BOX_XPATH)).sendKeys(SEARCH_CORRECT_SPELLING_KEYWORD);
        assert webDriver.findElement(By.xpath(CLEAR_BUTTON_XPATH)).isDisplayed();
    }

    @Test(priority = 7)
    public void areSuggestionsWorking() {
        List<WebElement> allSuggestions = webDriver.findElements(By.tagName("li"));
        for (WebElement element : allSuggestions) {
            if (StringUtils.isNotEmpty(element.getText())) {
                assert StringUtils.containsIgnoreCase(element.getText(), SEARCH_CORRECT_SPELLING_KEYWORD);
            }

        }
    }

    @Test(priority = 8)
    public void isSubmitWorking() {
        webDriver.findElement(By.xpath(SEARCH_BUTTON_X_PATH)).submit();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert webDriver.findElement(By.name("q")).getAttribute("value").equals(SEARCH_CORRECT_SPELLING_KEYWORD);
    }

    @Test(priority = 9)
    public void correctSpellingSearch() {
        assert webDriver.findElements(By.xpath(INCORRECT_SPELLING_TEXT_XPATH)).size() == 0;
        validateLinksKeywords();
    }

    @Test(priority = 10)
    public void incorrectSpellingSearch() {
        WebElement searchBox = webDriver.findElement(By.name("q"));
        searchBox.clear();
        searchBox.sendKeys(SEARCH_INCORRECT_SPELLING_KEYWORD);
        searchBox.submit();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert webDriver.findElements(By.xpath(INCORRECT_SPELLING_TEXT_XPATH)).size() > 0;
        validateLinksKeywords();
    }
    @Test(priority = 11)
    public void isPaginationWorking() {
        assert webDriver.findElement(By.xpath(PAGINATION_XPATH)).isDisplayed();
    }
    private void validateLinksKeywords() {
        WebElement searchedLinks = webDriver.findElement(By.id("search"));
        int count = 0;
        List<WebElement> allLinks = searchedLinks.findElements(By.tagName("a"));
        //Checking if at least 5 links contains the main keyword
        for (WebElement link : allLinks) {
            if (StringUtils.isNotBlank(link.getText()) && StringUtils.isNotBlank(link.getAttribute("href")) && count <= 5) {
                count++;
                assert StringUtils.containsIgnoreCase(link.getText(), SEARCH_CORRECT_SPELLING_KEYWORD) || StringUtils.containsIgnoreCase(link.getAttribute("href"), SEARCH_CORRECT_SPELLING_KEYWORD);
            }
        }
    }
}
