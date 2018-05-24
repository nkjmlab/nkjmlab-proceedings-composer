package org.nkjmlab.proceedings;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;

@CsvEntity
public class PaperDescription {

	@CsvColumn(name = "title")
	private String title;

	@CsvColumn(name = "author")
	private String author;

	@CsvColumn(name = "filePath")
	private String filePath;

	public int startPage = -1;

	public String toTocItem() {
		String str = "<div class='paper'>\n" + "<div class='title'>"
				+ "<a href='" + filePath + "'>"
				+ getTitle() + "</a></div>\n" + "<div class='author'>" + getAuthor()
				+ "</div>\n" + "<div class='start-page'>" + startPage
				+ "</div>\n" + "</div>\n";
		return str;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filename) {
		this.filePath = filename;
	}
}
