package eu.chessdata.chesspairing;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Date;

import org.junit.Test;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.TestUtils;
import eu.chessdata.chesspairing.tools.SortTool;
import eu.chessdata.chesspairing.tools.Trf;

public class TrfTest {

	/**
	 * used to order first time players in order
	 */
	@Test
	public void test1() {
		ChesspairingTournament tournament = TestUtils.loadFile("/trf/test1.json");
		//sort the players
		Collections.sort(tournament.getPlayers(), SortTool.playerByElo);
		int i=1;
		for (ChesspairingPlayer player:tournament.getPlayers()){
			player.setInitialOrderId(i++);
		}
		tournament.setCity("Iasi");
		tournament.setDateOfStart(new Date());
		tournament.setDateOfEnd(new Date());
		
		TestUtils.writeToFile(tournament, "TrfTestTest1Ordered.json");
	}

	@Test
	public void test2(){
		ChesspairingTournament tournament = TestUtils.loadFile("/trf/test2.json");
		String trf = Trf.buildTrf(tournament);
		System.out.println(trf);
	}
}
