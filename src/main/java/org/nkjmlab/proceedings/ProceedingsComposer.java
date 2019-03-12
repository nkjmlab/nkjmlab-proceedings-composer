package org.nkjmlab.proceedings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	private static org.apache.logging.log4j.Logger log = org.nkjmlab.util.log4j.LogManager
			.getLogger();

	private static final String CSV_FILE_NAME = "toc.csv";
	private final File resourceDir;
	private final File outputDir;
	private final File tocHtmlFile;

	public static void main(String[] args) {
		if (args.length != 2) {
			log.debug("debug");
			log.info("args should be two.");
			System.out.println("[USAGE]");
			System.out.println("compose.bat confFile outputRootDir");
			System.out.println("e.g. compose.bat sample-proceedings-src/ sample-proceedings/");
			return;
		}

		File proceedingsSrcDir = new File(args[0]);
		File outputRootDir = new File(args[1]);
		new ProceedingsComposer(proceedingsSrcDir, outputRootDir).compose();

	}

	public ProceedingsComposer(File proceedingsSrcDir, File outputRootDir) {
		this.outputDir = outputRootDir;
		this.resourceDir = proceedingsSrcDir;
		this.tocHtmlFile = new File(outputDir, "index.html");
	}

	public void compose() {
		try {
			FileUtils.copyDirectory(resourceDir, outputDir);
			Files.delete(Paths.get(outputDir.toString(), CSV_FILE_NAME));
			generateTocFile();
			log.info("Composed proceedings is in " + outputDir.getAbsolutePath());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void generateTocFile() {
		try {
			Document tocFile = Jsoup.parse(tocHtmlFile, "UTF-8");
			int offset = 1;
			for (PaperDescription p : CsvUtils.readList(PaperDescription.class,
					new File(resourceDir, CSV_FILE_NAME))) {
				p.startPage = offset;
				log.debug("Start page {}", p.startPage);
				log.debug(p.toTocItem());
				tocFile.getElementById("toc").append(p.toTocItem());
				File pdfFile = new File(resourceDir, p.getFilePath());
				try (PDDocument doc = PDDocument.load(pdfFile)) {
					offset += procPdf(doc, p, offset);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			org.nkjmlab.util.io.FileUtils.write(tocHtmlFile.toPath(), tocFile.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private int procPdf(PDDocument doc, PaperDescription p, int offset)
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
		doc.save(new File(outputDir, p.getFilePath()));
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

}
