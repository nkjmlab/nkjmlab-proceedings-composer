package org.nkjmlab.proceedings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.manager.CsvEntityManager;

public class ProceedingsComposer {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("\n[USAGE]");
			System.out.println("compose.bat filename");
			System.out.println("ex. compose.bat sample/proceedings.csv");
			return;
		}

		String csvFile = args[0];

		ProceedingsComposer composer = new ProceedingsComposer();
		try {
			String resourcesDir = "src/main/resources/css/";

			String outDir = "proceedings-"
					+ new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
							.format(new Date()) + "/";

			composer.compose(csvFile, resourcesDir, outDir);
		} catch (COSVisitorException | IOException e) {
			e.printStackTrace();
		}
	}

	private void compose(String csvFile, String resourcesDir, String outDir)
			throws IOException, COSVisitorException {

		Files.createDirectories(Paths.get(outDir + "/papers/"));

		Files.copy(Paths.get(resourcesDir + "proceedings.css"),
				Paths.get(outDir + "proceedings.css"));

		CsvConfig cfg = new CsvConfig();
		cfg.setQuoteDisabled(false); // デフォルトでは無効となっている囲み文字を有効にします。
		cfg.setIgnoreEmptyLines(true); // 空行を無視するようにします。
		cfg.setIgnoreLeadingWhitespaces(true); // 項目値前のホワイトスペースを除去します。
		cfg.setIgnoreTrailingWhitespaces(true); // 項目値後のホワイトスペースを除去します。
		CsvEntityManager manager = new CsvEntityManager(cfg);
		List<PaperInfo> papers = manager.load(PaperInfo.class).from(
				new File(csvFile));

		FileWriter writer = new FileWriter(new File(outDir + "index.html"));
		writer.write("<!DOCTYPE html>\n");
		writer.write("<head><link rel='stylesheet' href='proceedings.css' />\n<title>Proceedings</title>\n</head>\n<body>\n");
		writer.write("<section id='proceedings'>\n");
		writer.write("<header></header>\n");
		writer.write("<section class='toc'>\n");
		int offset = 1;
		for (PaperInfo p : papers) {
			p.startPage = offset;
			System.out.println("Start page " + p.startPage);
			// System.out.println(p.toTocItem());
			writer.write(p.toTocItem());

			File f = new File(p.filePath);

			PDDocument doc = PDDocument.load(f);

			List<?> allPages = doc.getDocumentCatalog().getAllPages();
			PDFont font = PDType1Font.HELVETICA_BOLD;
			float fontSize = 9.0f;

			for (int i = 0; i < allPages.size(); i++) {
				String footerMsg = String.valueOf(offset + i);
				PDPage page = (PDPage) allPages.get(i);
				PDRectangle pageSize = page.findMediaBox();
				float fotterMsgWidth = font.getStringWidth(footerMsg)
						* fontSize / 1000f;
				float pageWidth = pageSize.getWidth();
				double centeredXPosition = (pageWidth - fotterMsgWidth) / 2f;
				double bottomYPosition = 30;

				PDPageContentStream contentStream = new PDPageContentStream(
						doc, page, true, true, true);
				contentStream.beginText();
				contentStream.setFont(font, fontSize);
				contentStream.setNonStrokingColor(0, 0, 0);
				contentStream.setTextTranslation(centeredXPosition,
						bottomYPosition);
				contentStream.drawString(footerMsg);
				contentStream.endText();
				contentStream.close();
			}
			doc.save(new File(outDir + "papers/"
					+ new File(p.filePath).getName()));
			doc.close();
			offset += allPages.size();
		}
		writer.write("</section>\n");
		writer.write("<footer></footer>\n");
		writer.write("</section>\n");
		writer.write("</body>\n");
		writer.close();
		System.out.println("Composed proceedings is in " + outDir);
	}
}
