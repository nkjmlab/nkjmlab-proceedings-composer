package org.nkjmlab.proceedings;

import org.junit.Before;
import org.junit.Test;

public class ProceedingsComposerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		ProceedingsComposer.main(new String[] { "sample/proceedings.csv" });
	}

}
