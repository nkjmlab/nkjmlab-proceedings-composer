package org.nkjmlab.proceedings;

import java.io.File;

public class ProceedingsComposerExecutorSample {

	public static void main(String[] args) {
		new ProceedingsComposer(new File("sample-proceedings-src/"),
				new File("sample-proceedings/")).compose();
	}

}
