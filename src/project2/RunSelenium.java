package project2;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

public class RunSelenium {

	//webdriver
	public WebDriver driver;
	public String url;
	
	//properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:\\Users\\kim\\Documents\\project\\chromedriver.exe";
	public static String TEST_URL = "https://www1.president.go.kr/petitions/?c=0&only=2&page=1&order=2";
	
	public static void main(String[] args) {
		
		RunSelenium test = new RunSelenium();
		test.run();
	}
	
	public RunSelenium() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		ChromeOptions options= new ChromeOptions();
		options.setCapability("ignoreProtectedModeSettings", true);
		driver = new ChromeDriver(options);
	}
	
	public void getElements() {
		int i, list;
		list = 7;
		for (i=1; i <= list; i++) {
			WebElement post = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li[" + i + "]/div/div[3]/a"));
			post.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement postTitle = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div[1]/div/div[1]/div/h3"));
			System.out.println("Post title: " + postTitle.getText());
			
			WebElement postContents = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div[1]/div/div[1]/div/div[4]/div[4]"));
			System.out.println("Post contents : " + postContents.getText());
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			driver.navigate().back();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		}

		//WorkflowNounExtractor.main(y.getText());
	}
	
	public void run() {
		try {
			driver.get(TEST_URL);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			//Thread.sleep(3000);
			getElements();
			Thread.sleep(20000);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
			driver.quit();
		}
	}

}
 