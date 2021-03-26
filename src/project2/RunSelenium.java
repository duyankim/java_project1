package project2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class RunSelenium {

	//webdriver
	public WebDriver driver;
	public WebDriver element;
	public String url;
	
	//properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:\\Users\\kim\\Documents\\project\\chromedriver.exe";
	public static String TEST_URL = "https://www1.president.go.kr/petitions/?c=0&only=1&page=1&order=2";
	
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
		WebElement x = driver.findElement(By.xpath("//*[@id=\"cont_view\"]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li[1]/div/div[3]/a"));
		String s = x.getText();
		
		System.out.println("Text content: "+ s);
		GetNouns.main(s);
	}
	
	public void run() {
		try {
			driver.get(TEST_URL);
			Thread.sleep(3000);
			getElements();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}
	}

}
 