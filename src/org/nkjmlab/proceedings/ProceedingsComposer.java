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

		ProceedingsComposer composer = new ProceedingsComposer();
		try {
			String resourcesDir = "resources/";

			String outDir = "proceedings/"
					+ new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
							.format(new Date()) + "/";

			if (!Files.exists(Paths.get(resourcesDir + "proceedings.csv"))) {
				System.err.println("You must create '" + resourcesDir
						+ "proceedings.csv'.");
				System.err.println("Sample is '" + resourcesDir
						+ "proceedings.sample.csv'");
				return;
			}

			composer.compose(resourcesDir, outDir);
		} catch (COSVisitorException | IOException e) {
			e.printStackTrace();
		}
	}

	private void compose(String resourcesDir, String outDir)
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
				new File(resourcesDir + "proceedings.csv"));

		FileWriter writer = new FileWriter(new File(outDir + "index.html"));
		writer.write("<link rel='stylesheet' href='proceedings.css' />");
		int offset = 1;
		for (PaperInfo p : papers) {
			p.startPage = offset;
			System.out.println("Start page " + p.startPage);
			// System.out.println(p.toTocItem());
			writer.write(p.toTocItem());

			File f = new File(resourcesDir + p.fileName);

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
			doc.save(new File(outDir + "papers/" + p.fileName));
			doc.close();
			offset += allPages.size();
		}
		writer.close();
		System.out.println("Composed proceedings is in " + outDir);
	}
}
