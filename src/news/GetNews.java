package data0630.news;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class GetNews {

	public static WebDriver driver;
	static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_PATH = "C:\\Users\\kim\\jarLib\\chromedriver.exe";
	static String writeFileName = "C:\\Users\\kim\\Documents\\data\\stock\\news.csv";

	public static void main(String[] args) {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		driver = new ChromeDriver();
		GetNews news = new GetNews();
		String[][] stockcode = news.readFile();
		news.crawl(stockcode);
	}

	public String[][] readFile() {
		String file = "C:\\Users\\kim\\Documents\\data\\stock\\stockcode.csv";
		CSVReader csvReader;
		String[][] all = new String[200][2];

		try {
			csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String[] nextLine;
			csvReader.readNext();
			int i = 0;
			while ((nextLine = csvReader.readNext()) != null) {
				all[i][0] = String.valueOf(nextLine[0]);
				all[i][1] = String.valueOf(nextLine[1]);
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return all;
	}

	public void crawl(String[][] stockcode) {
		List<String[]> result = new ArrayList<String[]>();

		CSVWriter cw = null;
		try {
			cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(writeFileName), "MS949"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			int k = 0;
			while (k < stockcode.length) {
				String base_url = "https://search.naver.com/search.naver?where=news&query=" + stockcode[k][0]
						+ "&sm=tab_opt&sort=1&photo=0&field=0&pd=4&ds=&de=&docid=&related=0&mynews=0&office_type=0&office_section_code=0&news_office_checked=&nso=so%3Add%2Cp%3A1d&is_sug_officeid=0";
				driver.get(base_url);
				Thread.sleep(2000);
				System.out.println(k + "|" + stockcode[k][0]);
				result = getElements(base_url, stockcode[k][0]);
				
				String disabled;
				// 다음 페이지가 활성화되어 있으면 다음페이지로 가는 화살표를 클릭합니다
				try {
					disabled = driver
							.findElement(By.cssSelector("#main_pack > div.api_sc_page_wrap > div > a.btn_next"))
							.getAttribute("aria-disabled");
					if (disabled.equals("false")) {
						driver.findElement(
								By.cssSelector("#main_pack > div.api_sc_page_wrap > div > a.btn_next")).click();
						Thread.sleep(2000);
						String next_url = driver.getCurrentUrl();
						
						List<String[]> tempList2 = getElements(next_url, stockcode[k][0]);
						for (String[] newsData : result) {
							tempList2.add(newsData);
						}
						
					} else if (disabled.equals("true")) {
						// 다음 페이지 화살표가 활성화 되어있지 않을 때는 페이지 넘김 처리를 하지 않습니다.
					}
				} catch (NoSuchElementException e) {
				}
				k++;
				
				for (String[] r : result) {
					cw.writeNext(r);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}

		try {
			cw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String[]> getElements(String url, String code) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Elements pressNames = doc.getElementsByClass("info press");
		Elements titles = doc.getElementsByClass("news_tit");
		Elements infos = doc.getElementsByClass("info_group");
		
		Date date = new Date();
		int beforeHour;
		
		String ahref, newshref, naverNewsLink, newsDate = null, newsTime, pressName, newsLink, newsTitle;
		List<String[]> result = new ArrayList<String[]>();
				
		for (int i = 0; i < titles.size(); i++) {
			String time = infos.get(i).getElementsByTag("span").text();
			try {
				if (time.contains("일")) time = "24시간 전";
				beforeHour = Integer.parseInt(time.substring(0, time.length()-4));
			} catch (NumberFormatException nfe){
				time = infos.get(i).getElementsByTag("span").last().text();
				beforeHour = Integer.parseInt(time.substring(0, time.length()-4));
			}
			
			ahref = String.valueOf(titles.get(i).select("[href]"));
			int classNameIdx = ahref.substring(3).indexOf(' ');
			
			newshref = String.valueOf(infos.get(i).getElementsByTag("a").last().select("[href]"));
			int classNameIdx2 = newshref.substring(3).indexOf(' ');
			naverNewsLink = newshref.substring(9,classNameIdx2+2);
			
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    cal.add(Calendar.HOUR, -beforeHour);
		    newsTime = df.format(cal.getTime());
		    newsDate = df2.format(cal.getTime());
		    
		    pressName = pressNames.get(i).text();
		    newsLink = ahref.substring(9,classNameIdx+2);
		    if (!naverNewsLink.contains("naver")) naverNewsLink="";
		    newsTitle = titles.get(i).text();
		    
		    String[] newsData = {code, newsDate, newsTime, pressName, newsTitle, newsLink, naverNewsLink};
		    result.add(newsData);
		}
		return result;
	}

	public void writeFile(String path, String[] data) {

		try {
			CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(path), "MS949"));
			cw.writeNext(data);
			cw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
