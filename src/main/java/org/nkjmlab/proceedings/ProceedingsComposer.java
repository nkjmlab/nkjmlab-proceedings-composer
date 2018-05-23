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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nkjmlab.util.csv.CsvUtils;

public class ProceedingsComposer {
	static final String PAPERS_DIR = "papers";
	protected static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger();

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("[USAGE]");
			System.out.println("compose.bat confFile outputRootDir");
			System.out.println("e.g. compose.bat sample/proceedings.csv tmp/");
			return;
		}

		String csvFile = args[0];
		String outputRootDir = args[1];
		new ProceedingsComposer().compose(csvFile, outputRootDir);

	}

	public void compose(String csvFile, String outputRootDir) {
		try {
			compose(CsvUtils.readList(PaperDescription.class, new File(csvFile)), outputRootDir);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public void compose(List<PaperDescription> papersInfo, String outputRootDir) {
		try {
			Path outDir = new File(outputRootDir,
					"proceedings-" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
							.format(new Date())).toPath();
			Files.createDirectories(
					new File(outDir.toFile(), PAPERS_DIR).toPath());
			FileUtils.copyInputStreamToFile(
					ProceedingsComposer.class
							.getResourceAsStream("/css/proceedings.css"),
					new File(outDir.toFile(), "proceedings.css"));

			File outFile = new File(outDir.toFile(), "index.html");
			FileUtils.copyInputStreamToFile(
					getClass().getResourceAsStream("/index.html"), outFile);

			Document tocFile = generateTocFile(papersInfo, outFile, outDir);
			writeResult(tocFile, outFile);
			log.debug("Composed proceedings is in " + outDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Document generateTocFile(List<PaperDescription> papersInfo, File outFile,
			Path outDir) {
		try {
			Document tocFile = Jsoup.parse(outFile, "UTF-8");
			int offset = 1;
			for (PaperDescription p : papersInfo) {
				p.startPage = offset;
				log.debug("Start page {}", p.startPage);
				log.debug(p.toTocItem());
				tocFile.getElementById("toc").append(p.toTocItem());
				try (PDDocument doc = PDDocument.load(new File(p.getFilePath()))) {
					offset += procPdf(doc, p, offset, outDir);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tocFile;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private int procPdf(PDDocument doc, PaperDescription p, int offset, Path outDir)
			throws IOException {
		PDPageTree allPages = doc.getDocumentCatalog().getPages();
		final PDFont font = PDType1Font.HELVETICA_BOLD;
		final float fontSize = 9.0f;

		for (int i = 0; i < doc.getNumberOfPages(); i++) {
			String footerMsg = String.valueOf(offset + i);
			PDPage page = allPages.get(i);

			try (PDPageContentStream content = new PDPageContentStream(doc, page,
					AppendMode.APPEND, true, true)) {
				content.beginText();
				content.setFont(font, fontSize);
				content.setNonStrokingColor(0, 0, 0);
				content.setTextMatrix(calcMatrix(page, font, fontSize, footerMsg));
				content.showText(footerMsg);
				content.endText();
			}
		}
		doc.save(new File(new File(outDir.toFile(), PAPERS_DIR),
				new File(p.getFilePath()).getName()));
		return doc.getNumberOfPages();
	}

	private Matrix calcMatrix(PDPage page, PDFont font, float fontSize, String footerMsg) {
		try {
			float fotterMsgWidth = font.getStringWidth(footerMsg) * fontSize
					/ 1000f;
			final float pageWidth = page.getMediaBox().getWidth();
			final float centeredXPosition = (pageWidth - fotterMsgWidth) / 2f;
			final float bottomYPosition = 30;
			return Matrix.getTranslateInstance(centeredXPosition, bottomYPosition);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeResult(Document document, File outFile) {
		try (FileWriter writer = new FileWriter(outFile)) {
			writer.write(document.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
