package org.nkjmlab.proceedings;

import java.io.File;

import org.nkjmlab.gdata.spreadsheet.client.GoogleSpreadsheetService;
import org.nkjmlab.gdata.spreadsheet.client.GoogleSpreadsheetServiceFactory;
import org.nkjmlab.gdata.spreadsheet.client.WorksheetServiceClient;

public class ProceedingsComposerExecutorWithGoogleSpreadsheet {

	private static GoogleSpreadsheetService factory = GoogleSpreadsheetServiceFactory
			.create(new File(System.getProperty("user.home"), "priv/google-api.json"));

	public static void main(String[] args) {
		WorksheetServiceClient worksheet = factory
				.createWorksheetServiceClient("JAWS2016プログラム.xlsx", "proceedings");

		new ProceedingsComposer().compose(worksheet.rows(PaperInfo.class));
	}

}
