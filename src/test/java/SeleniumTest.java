import io.github.bonigarcia.seljup.SeleniumExtension;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.xml.transform.Source;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@ExtendWith(SeleniumExtension.class)
public class SeleniumTest {
    private final String SCREENSHOTS = "./src/test/onDemandScreenshots";

    EdgeDriver driver;


    public SeleniumTest(EdgeDriver driver) {
        this.driver = driver;
        DesiredCapabilities dC = new DesiredCapabilities();

        dC.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        System.setProperty("sel.jup.screenshot.at.the.end.of.tests", "whenfailure");
        System.setProperty("sel.jup.screenshot.format", "png");
        System.setProperty("sel.jup.output.folder", "./src/test/failureScreenShots");
    }

    public static void TakeSnapshot(WebDriver webDriver, String fileWithPath) throws Exception {
        //convert the web driver object into a screenshot
        TakesScreenshot takeSnapshot = ((TakesScreenshot)webDriver);

        //call the getscreenshot method as a file
        File source = takeSnapshot.getScreenshotAs(OutputType.FILE);

        //Move the file to the destination
        File destFile = new File(fileWithPath);

        //Copy the file at the destination
        FileUtils.copyFile(source, destFile);
    }

    @Test
    @DisplayName("test_facebook_logo")
    void test_fb_logo(TestInfo testInfo) throws Exception {
        //assert stuff
        driver.get("https://www.facebook.com");
        driver.manage().window().maximize();

        //act/find
        WebElement fbLogo;
        fbLogo = driver.findElement(By.className("fb_logo"));

        String method = testInfo.getDisplayName();
        TakeSnapshot(driver, SCREENSHOTS + "\\" + method + "_" + System.currentTimeMillis() + ".png");

        //act
        assertThat(fbLogo.isDisplayed(), is(true));

        try{
            Thread.sleep(5000);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        driver.quit();
    }
}
