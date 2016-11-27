package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PairingResultTest {

	@Test
	public void staticPairingResult() {
		PairingResult noGood = PairingResult.notValid();
		assertTrue(noGood.isOk()==false);
	}

}
