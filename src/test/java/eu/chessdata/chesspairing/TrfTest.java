package eu.chessdata.chesspairing;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.TestUtils;
import eu.chessdata.chesspairing.tools.Trf;

public class TrfTest {

	@Test
	public void test1() {
		ChesspairingTournament tournament = TestUtils.loadFile("/trf/test1.json");
		
		String trf = Trf.buildTrf(tournament);
		System.out.println(trf);
	}

	
}
