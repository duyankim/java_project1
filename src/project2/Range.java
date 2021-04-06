package project2;

public class Range {
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:\\Users\\kim\\Documents\\project\\chromedriver.exe";
	public static String TEST_URL = "https://www1.president.go.kr/petitions/?c=0&only=2&page=1&order=1";
	public static int LIST = 7;
	RunSelenium selenium = new RunSelenium();
	
	public Range() {

	}
	
	public void run() {
		
	}
	
	public void firstId() {
		selenium.getArticleId(1);
	}
	
	public void lastId() {
		selenium.getArticleId(7);
	}
	

}
