package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertStoreSpi;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.Tools;
import eu.chessdata.chesspairing.model.ChesspairingGame;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.TestUtils;

public class PairingToolTest {

	private ChesspairingTournament loadFile(String fileProjectPath) {
		InputStream inputStream = PairingToolTest.class.getResourceAsStream(fileProjectPath);
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
		FideSwissDutch algorithm = new FideSwissDutch();
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/pairingTool/test1.json");
		dataTournament.setName("Test 1 tournament");
		algorithm.initializeAlgorithm(dataTournament);

		PairingTool pairingTool = new PairingTool(algorithm);
		pairingTool.initializePlayers();
		assertTrue(10 == pairingTool.players.size());

		pairingTool.initializeScoreBrackets();
		ScoreBracket scoreBracket = pairingTool.scoreBrackets.get(2.0);
		assertTrue("Bracket should not be null", scoreBracket != null);

		List<Player> bracketPlayers = scoreBracket.getBracketPlayers();
		for (Player player : bracketPlayers) {
			System.out.println("\t: " + player.getName() + ": \t" + player.getElo());
		}

		Player player = pairingTool.get("-KMDl3KITCII80fhtdjL");
		System.out.println(player);
		System.out.println("End test 1: " + player.getName());
	}

	@Test
	/**
	 * it replicates the logic inside computeGames with extra texts between
	 * steps
	 */
	public void test2() {
		FideSwissDutch algorithm = new FideSwissDutch();
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/pairingTool/test1.json");
		dataTournament.setName("Test 2 sort bracket");
		algorithm.initializeAlgorithm(dataTournament);

		PairingTool pairingTool = new PairingTool(algorithm);
		// step1
		pairingTool.initializePlayers();

		// step 2
		pairingTool.initializeScoreBrackets();
		ScoreBracket scoreBracket = pairingTool.scoreBrackets.get(2.0);

		List<Player> players = scoreBracket.getBracketPlayers();
		for (Player player : players) {
			System.out.println(player.getPairingPoints() + ", " + player.getName() + "," + player.getElo() + ","
					+ player.getInitialRanking());
		}

		// scoreBracket.sortPlayers();
		// System.out.println("Sorted list: ");
		// List<Player> sortedPlayers = scoreBracket.getBracketPlayers();
		// for (Player player : sortedPlayers) {
		// System.out.println(player.getPairingPoints() + ", " +
		// player.getName() + "," + player.getElo() + ","
		// + player.getInitialRanking());
		// }

		pairingTool.pairBrackets();

	}

	@Test
	/**
	 * I'm using this test just to get a handle on a file with allot of
	 * tournament players and with rigged round 1
	 */
	public void test3ConstructTournament() {
		ChesspairingTournament myTournament = TestUtils.rigFirstRoundOn23();
		TestUtils.writeToFile(myTournament, "test4.json");
		System.out.println("End test 3");
	}

	@Test
	public void test4UnitTests() {
		FideSwissDutch algorithm = new FideSwissDutch();
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/pairingTool/test4.json");

		algorithm.initializeAlgorithm(dataTournament);
		PairingTool pairingTool = new PairingTool(algorithm);

		// section of what should happen in coputeGames
		pairingTool.initializePlayers();// step1
		pairingTool.initializeScoreBrackets();// step2
		pairingTool.debugListBrackets();

		pairingTool.pairBrackets();

		pairingTool.updateResultGames();
		pairingTool.makeSureAllPlayersGotPared();
		int size = pairingTool.resultGames.size();
		assertTrue("result size " + size + " != 12", size == 12);

	}

	/**
	 * mainly meant to develop infrastructure to capture tournament state before
	 * errors occurred.
	 */
	// @Test
	/*
	 * public void test5PareUntillItFails() { ChesspairingTournament
	 * dataTournament =
	 * loadFile("/fideswissdutchTest/v2/pairingTool/test5.json"); int
	 * totalRounds = dataTournament.getTotalRounds(); int k =
	 * dataTournament.getRounds().size(); try { while (k < totalRounds) {
	 * dataTournament = (new
	 * FideSwissDutch()).generateNextRound(dataTournament); k++; } } catch
	 * (IllegalStateException e) { throw new
	 * IllegalStateException("Time to sotre the tournament state"); } }
	 */
}
