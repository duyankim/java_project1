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

	// webdriver
	public WebDriver driver;
	public String url;
	private JavascriptExecutor js;
	WebDriverWait wait1;
	WebDriverWait wait2;

	// properties
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:\\Users\\kim\\Documents\\project\\chromedriver.exe";
	public static String TEST_URL = "https://www1.president.go.kr/petitions/?c=0&only=2&page=1&order=1";
	public static int LIST = 7;

	public static String voteStr;
	public static String startDateStr;
	public static String endDateStr;
	public static String contents;

	public static void main(String[] args) {

		RunSelenium test = new RunSelenium();
		test.run();
	}

	public void run() {
		try {
			driver.get(TEST_URL);
			getElements();
			Thread.sleep(20000);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
			driver.quit();
		}
	}

	public RunSelenium() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.setCapability("ignoreProtectedModeSettings", true);
		driver = new ChromeDriver(options);
	}

	public void getElements() {
		int i, p = 1;
		js = (JavascriptExecutor) driver;

		driver.manage().window().maximize();
		WebElement popup = driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/table/tbody/tr/td[2]/div"));
		popup.click();

		try {
			while (p < 22) {
				for (i = 1; i <= LIST; i++) {
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					wait1 = new WebDriverWait(driver, 10);

					getCategory(i);
					getVote(i);
					getTitle(i);

					driver.get("https://www1.president.go.kr/petitions/" + getArticleId(i));
					driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
					// clickPost(i);
					// waitLoading();

					getStartDate(i);
					getEndDate(i);
					getContent(i);

					System.out.println();
					System.out.println("******************************************");

					driver.navigate().back();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

				}
				nextBtn(p);
				p++;
			}
		} catch (org.openqa.selenium.ElementClickInterceptedException e1) {
			waitLoading();
		} catch (org.openqa.selenium.UnhandledAlertException e2) {
			System.out.println("잠시후 다시 이용해주세요");
		}

	}

	public String getArticleId(int postOrder) {
		// 게시물 번호 가져오기
		WebElement articleId;
		js.executeScript("scroll(0, 300)");
		articleId = driver.findElement(By.xpath(
				"/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["
						+ postOrder + "]/div/div[1]"));
		System.out.println("articleId: " + articleId.getText());
		return articleId.getText();
	}

	public String getCategory(int postOrder) {
		// 카테고리 가져오기
		WebElement category;
		category = driver.findElement(By.xpath(
				"/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["
						+ postOrder + "]/div/div[2]"));
		System.out.println("category: " + category.getText());
		return category.getText();
	}

	public String getVote(int postOrder) {
		// 공감수 가져오기
		WebElement vote;
		vote = driver.findElement(By.xpath(
				"/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["
						+ postOrder + "]/div/div[5]"));
		voteStr = vote.getText();
		voteStr = voteStr.substring(0, voteStr.length() - 1);
		System.out.println("vote: " + voteStr);
		return voteStr;
	}

	public String getTitle(int postOrder) {
		// 글의 제목 가져오기
		WebElement title;
		title = driver.findElement(By.xpath(
				"/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["
						+ postOrder + "]/div/div[3]/a"));
		js.executeScript("arguments[0].scrollIntoView()", title);
		System.out.println("Post title: " + title.getText());
		return title.getText();
	}

	public String getStartDate(int postOrder) {
		// 시작일 가져오기
		WebElement startDate;
		try {
			startDate = driver.findElement(
					By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div[1]/div/div[1]/div/div[2]/ul/li[2]"));
			startDateStr = startDate.getText();
			startDateStr = startDateStr.substring(5);
			System.out.println("startDate: " + startDateStr);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			System.out.println("비공개처리됨");
		}
		return startDateStr;
	}

	public String getEndDate(int postOrder) {
		// 종료일 가져오기
		WebElement endDate;
		try {
			endDate = driver.findElement(
					By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div[1]/div/div[1]/div/div[2]/ul/li[3]"));
			endDateStr = endDate.getText();
			endDateStr = endDateStr.substring(5);
			System.out.println("endDate: " + endDateStr);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			System.out.println("비공개처리됨");
		}
		return endDateStr;
	}

	public String getContent(int postOrder) {
		// 글의 내용 가져오기
		js.executeScript("scroll(0, 60)");
		WebElement content;
		try {
			content = driver.findElement(
					By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div[1]/div/div[1]/div/div[4]/div[2]"));
			contents = content.getText();
			System.out.println("Post contents : " + contents);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			System.out.println("비공개처리됨");
		}

		return contents;
	}

	public void clickPost(int postOrder) {
		// 리스트에서 게시물 클릭하기
		WebElement post;
		post = driver.findElement(By.xpath(
				"/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[2]/div[2]/ul/li["
						+ postOrder + "]/div/div[3]/a"));
		js.executeScript("arguments[0].click();", post);
	}

	public void waitLoading() {
		// 로딩 될 때까지 기다리기
		By loadingImage = By.id("loading image ID");
		wait2 = new WebDriverWait(driver, 10);
		wait2.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
	}

	public void nextBtn(int p) {
		js.executeScript("scroll(0, 500)");

		// 페이지가 10, 20, 30 등으로 next버튼을 눌러야 할 경우 next버튼 누르기
		if (p % 10 == 0) {
			next10(p);
		} else {
			// 다음 페이지 클릭하기
			ClickNextPage(p);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}

	public void next10(int p) {
		WebElement nextBtn;
		nextBtn = driver.findElement(By.xpath(
				"/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[4]/div/div[2]/a"));
		js.executeScript("arguments[0].click();", nextBtn);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	public void ClickNextPage(int p) {
		// 다음 페이지 클릭하기
		WebElement page;
		int aNum = (p % 10) + 1;
		int divNum = (p / 10) + 1;
		page = driver.findElement(
				By.xpath("/html/body/div[3]/div[2]/section[2]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[4]/div/div["
						+ divNum + "]/a[" + aNum + "]"));
		page.click();
		js.executeScript("arguments[0].click();", page);
	}
}
