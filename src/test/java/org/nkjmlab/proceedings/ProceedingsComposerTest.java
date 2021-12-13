package org.nkjmlab.proceedings;

import org.junit.jupiter.api.Test;

class ProceedingsComposerTest {

  public void setUp() throws Exception {}

  @Test
  public void test() {
    ProceedingsComposer.main(new String[] {"sample-proceedings-src/", "sample-proceedings/"});
  }

}
