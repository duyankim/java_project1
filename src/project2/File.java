package project2;

import java.io.IOException;
import java.io.PrintWriter;

public class File {

	public File() {

	}

	public void writeFile() throws IOException {
		PrintWriter pw = new PrintWriter("C:\\Users\\kim\\Desktop\\out.csv");
		String data = "articleId, category, vote, title, startDate, endDate, content";

		pw.println(data);

		pw.close();
	}

	public void run() {
		try {
			writeFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
