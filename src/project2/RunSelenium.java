package project2;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RunSelenium {

	//webdriver
	public WebDriver driver;
	public String url;
	private JavascriptExecutor js;
	WebDriverWait wait;
	WebDriverWait wait1;
	
	//properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:\\Users\\kim\\Documents\\project\\chromedriver.exe";
	public static String TEST_URL = "https://www1.president.go.kr/petitions/?c=0&only=2&page=1&order=1";
	WebElement articleId;
	WebElement category;
	WebElement vote;
	WebElement startDate;
	WebElement endDate;
	WebElement post;
	WebElement title;
	WebElement content;
	
	
	public static void main(String[] args) {
		
		RunSelenium test = new RunSelenium();
		test.run();
	}
	
	public RunSelenium() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		ChromeOptions options= new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.setCapability("ignoreProtectedModeSettings", true);
		driver = new ChromeDriver(options);
	}
	
	public void getElements() {
		int i, p=1, list;
		list = 7;
		js = (JavascriptExecutor) driver;
		
		driver.manage().window().maximize();
		WebElement popup = driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/table/tbody/tr/td[2]/div"));
		popup.click();
		
		while (p < 10) {
			for (i=1; i <= list; i++) {
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				wait1 = new WebDriverWait(driver, 10);

				//게시물 번호 가져오기
				js.executeScript("scroll(0, 300)");
				
				articleId = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["+ i +"]/div/div[1]"));
				System.out.println("articleId: " + articleId.getText());
				
				//카테고리 가져오기
				category = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["+ i +"]/div/div[2]"));
				System.out.println("category: " + category.getText());
				
				//공감수 가져오기
				vote = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["+ i +"]/div/div[5]"));
				System.out.println("vote: " + vote.getText());
				
				//글의 제목 가져오기
				title = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["+ i +"]/div/div[3]/a"));
				js.executeScript("arguments[0].scrollIntoView()", title);
				System.out.println("Post title: " + title.getText());
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
				//리스트에서 게시물 클릭하기
				post = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li[" + i + "]/div/div[3]/a"));
				js.executeScript("arguments[0].click();", post);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				
				//로딩 될 때까지 기다리기
				By loadingImage = By.id("loading image ID");
				wait = new WebDriverWait(driver, 10);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
				

				//시작일 가져오기
				startDate = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div[1]/div/div[1]/div/div[2]/ul/li[2]"));
				System.out.println("startDate: " + startDate.getText());
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
				//종료일 가져오기
				endDate = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div[1]/div/div[1]/div/div[2]/ul/li[3]"));
				System.out.println("endDate: " + endDate.getText());
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
				//글의 내용 가져오기
				js.executeScript("scroll(0, 60)");
				content = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div[1]/div/div[1]/div/div[4]/div[2]"));
				System.out.println("Post contents : " + content.getText());
				
				System.out.println();
				System.out.println("******************************************");
				
				driver.navigate().back();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			}
			//다음 페이지 클릭하기
			js.executeScript("scroll(0, 300)");
			WebElement page = driver.findElement(By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[4]/div/div[1]/a["+(p+1)+"]"));
			page.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			p++;
		}
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
 