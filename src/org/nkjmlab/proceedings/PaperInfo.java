package org.nkjmlab.proceedings;

import java.io.File;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;

@CsvEntity
public class PaperInfo {

	@CsvColumn(name = "title")
	public String title;

	@CsvColumn(name = "author")
	public String author;

	@CsvColumn(name = "filePath")
	public String filePath;

	public int startPage = -1;

	public String toTocItem() {
		String str = "<div class='paper'>\n" + "<div class='title'>"
				+ "<a href='papers/" + new File(filePath).getName() + "'>"
				+ title + "</a></div>\n" + "<div class='author'>" + author
				+ "</div>\n" + "<div class='start_page'>" + startPage
				+ "</div>\n" + "</div>\n";
		return str;
	}
}
