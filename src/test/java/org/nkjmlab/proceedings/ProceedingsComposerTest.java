package org.nkjmlab.proceedings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProceedingsComposerTest {

	@BeforeAll
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		ProceedingsComposer.main(new String[] { "sample/proceedings.csv" });
	}

}
