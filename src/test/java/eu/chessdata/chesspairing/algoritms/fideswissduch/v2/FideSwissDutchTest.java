package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.algoritms.fideswissduch.Algorithm;
import eu.chessdata.chesspairing.model.ChesspairingByeValue;
import eu.chessdata.chesspairing.model.ChesspairingGame;
import eu.chessdata.chesspairing.model.ChesspairingResult;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.TestUtils;
import eu.chessdata.chesspairing.tools.Tools;

public class FideSwissDutchTest {
	private FideSwissDutch algorithm = new FideSwissDutch();

	private ChesspairingTournament loadFile(String fileProjectPath) {
		InputStream inputStream = FideSwissDutchTest.class.getResourceAsStream(fileProjectPath);
		Reader reader;
		try {
			reader = new InputStreamReader(inputStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e.getMessage());
		}
		Gson gson = Tools.getGson();
		ChesspairingTournament tournament = gson.fromJson(reader, ChesspairingTournament.class);
		return tournament;
	}

	@Test
	public void test1() {
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/test1a.json");
		// configure the buy value
		dataTournament.setChesspairingByeValue(ChesspairingByeValue.HALF_A_POINT);

		// make sure that wee have a game that contains as a result buy
		boolean containsBuy = false;
		for (ChesspairingRound round : dataTournament.getRounds()) {
			for (ChesspairingGame game : round.getGames()) {
				if (game.getResult() == ChesspairingResult.BYE) {
					containsBuy = true;
				}
			}
		}
		if (!containsBuy) {
			throw new IllegalStateException("Please fix the data. This test requirs a buy");
		}

		// add one more round with no games
		ChesspairingRound round = new ChesspairingRound();
		round.setPresentPlayers(dataTournament.getPlayers());
		round.setRoundNumber(dataTournament.getRounds().size() + 1);
		dataTournament.getRounds().add(round);

		FideSwissDutch algorithm = new FideSwissDutch();

		ChesspairingTournament newRoundTournament = algorithm.generateNextRound(dataTournament);
		/*
		 * if (newRoundTournament == null){ throw new
		 * IllegalStateException("Null tournament from test This is OK"); }
		 */
		System.out.println("End test 1");
	}

	@Test
	public void test2() {
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/v02-test02.json");
		dataTournament.setName("Test 2 tournament");
		@SuppressWarnings("unused")
		ChesspairingTournament newRoundTournament = algorithm.generateNextRound(dataTournament);

		Double p1 = algorithm.getRoundPoints(1, "-KRLCD2IWvYYNA2WUD5J");
		Double p2 = algorithm.getRoundPoints(2, "-KRLCD2IWvYYNA2WUD5J");
		System.out.println("points ello100 = " + p1 + ", " + p2);
		assertTrue(p1 == 1.0);
		assertTrue(p2 == 2.0);

		Double f2 = algorithm.getRoundPoints(2, "-KKtt3zvO9vrdn5YoAz5");
		assertTrue(f2 == 1.5);
		System.out.println("Florin points round 2 = " + f2);
		System.out.println("End test 2 ");
	}

	@Test
	public void test3() {
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/v02-test03.json");
		dataTournament.setName("Test 3 tournament");
		@SuppressWarnings("unused")
		ChesspairingTournament newRoundTournament = algorithm.generateNextRound(dataTournament);

		Double f2 = algorithm.getPairingPoints(2, "-KKtt3zvO9vrdn5YoAz5");

		System.out.println("Florin points round 2 = " + f2);
		assertTrue(f2 == 1.0);
	}
	
	@Test
	public void test4 (){
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/v02-test04.json");
		dataTournament.setName("Test 4 tournament");
		ChesspairingTournament newRoundTournament = algorithm.generateNextRound(dataTournament);
	}

}
