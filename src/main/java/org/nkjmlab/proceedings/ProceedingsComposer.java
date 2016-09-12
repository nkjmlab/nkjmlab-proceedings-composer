package org.nkjmlab.proceedings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.manager.CsvEntityManager;

public class ProceedingsComposer {
	protected static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger();

	public static void main(String[] args) {

		if (args.length != 1) {
			log.debug("\n[USAGE]");
			log.debug("compose.bat filename");
			log.debug("ex. compose.bat sample/proceedings.csv");
			return;
		}

		String csvFile = args[0];

		ProceedingsComposer composer = new ProceedingsComposer();
		composer.compose(csvFile);
	}

	public void compose(String csvFile) {
		compose(readPapers(csvFile));
	}

	public void compose(List<PaperInfo> papersInfo) {
		try {
			Path outDir = new File(
					"proceedings-" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
							.format(new Date())).toPath();
			Files.createDirectories(
					new File(outDir.toFile(), "/papers/").toPath());
			copyCssFile(outDir);

			File outFile = new File(outDir.toFile(), "index.html");
			FileUtils.copyInputStreamToFile(
					getClass().getResourceAsStream("/index.html"), outFile);

			Document document = Jsoup.parse(outFile, "UTF-8");
			procPdfs(document, papersInfo, outDir);

			writeResult(document, outFile);
			log.debug("Composed proceedings is in " + outDir);
		} catch (IOException e) {
			log.error(e, e);
			throw new RuntimeException();
		}
	}

	private void procPdfs(Document document, List<PaperInfo> papersInfo, Path outDir) {
		int offset = 1;
		for (PaperInfo p : papersInfo) {
			p.startPage = offset;
			log.debug("Start page {}", p.startPage);
			log.debug(p.toTocItem());
			document.getElementById("toc").append(p.toTocItem());
			offset += procPdf(p, offset, outDir);
		}
	}

	private int procPdf(PaperInfo p, int offset, Path outDir) {
		try (PDDocument doc = PDDocument.load(new File(p.getFilepath()))) {
			List<?> allPages = doc.getDocumentCatalog().getAllPages();
			PDFont font = PDType1Font.HELVETICA_BOLD;
			float fontSize = 9.0f;

			for (int i = 0; i < allPages.size(); i++) {
				String footerMsg = String.valueOf(offset + i);
				PDPage page = (PDPage) allPages.get(i);
				PDRectangle pageSize = page.findMediaBox();
				float fotterMsgWidth = font.getStringWidth(footerMsg) * fontSize
						/ 1000f;
				float pageWidth = pageSize.getWidth();
				double centeredXPosition = (pageWidth - fotterMsgWidth) / 2f;
				double bottomYPosition = 30;

				PDPageContentStream contentStream = new PDPageContentStream(doc,
						page, true, true, true);
				contentStream.beginText();
				contentStream.setFont(font, fontSize);
				contentStream.setNonStrokingColor(0, 0, 0);
				contentStream.setTextTranslation(centeredXPosition,
						bottomYPosition);
				contentStream.drawString(footerMsg);
				contentStream.endText();
				contentStream.close();
			}
			doc.save(new File(new File(outDir.toFile(), "papers"),
					new File(p.getFilepath()).getName()));
			return allPages.size();
		} catch (IOException | COSVisitorException e) {
			log.error(e, e);
			throw new RuntimeException();
		}
	}

	private void writeResult(Document document, File outFile) {
		try (FileWriter writer = new FileWriter(outFile)) {
			writer.write(document.toString());
		} catch (IOException e) {
			log.error(e, e);
			throw new RuntimeException();
		}

	}

	private void copyCssFile(Path outDir) {
		try {

			FileUtils.copyInputStreamToFile(
					ProceedingsComposer.class
							.getResourceAsStream("/css/proceedings.css"),
					new File(outDir.toFile(), "proceedings.css"));
		} catch (IOException e) {
			log.error(e, e);
		}

	}

	public List<PaperInfo> readPapers(String csvFile) {
		CsvConfig cfg = new CsvConfig();
		cfg.setQuoteDisabled(false); // デフォルトでは無効となっている囲み文字を有効にします。
		cfg.setIgnoreEmptyLines(true); // 空行を無視するようにします。
		cfg.setIgnoreLeadingWhitespaces(true); // 項目値前のホワイトスペースを除去します。
		cfg.setIgnoreTrailingWhitespaces(true); // 項目値後のホワイトスペースを除去します。
		CsvEntityManager manager = new CsvEntityManager(cfg);
		try {
			return manager.load(PaperInfo.class).from(new File(csvFile));
		} catch (IOException e) {
			log.error(e, e);
			throw new RuntimeException();
		}
	}
}
