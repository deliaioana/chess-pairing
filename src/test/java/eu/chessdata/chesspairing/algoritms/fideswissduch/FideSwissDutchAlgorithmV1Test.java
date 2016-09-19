package eu.chessdata.chesspairing.algoritms.fideswissduch;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.Tools;
import eu.chessdata.chesspairing.model.ChesspairingGame;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.ChesspairingTournamentTest;
import eu.chessdata.chesspairing.model.PairingSummary;

public class FideSwissDutchAlgorithmV1Test extends FideSwissDutchAlgorithmV1{

	@Test
	public void firstRoundTest1() throws UnsupportedEncodingException {
		InputStream inputStream = ChesspairingTournamentTest.class
				.getResourceAsStream("/fideSwissDutchAlgorithmTest/zeroRounds.json");
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		Gson gson = Tools.getGson();
		ChesspairingTournament tournament = gson.fromJson(reader, ChesspairingTournament.class);
		tournament.setTotalRounds(7);
		tournament.setName("FirstRoundTest1 (just a name)");
		tournament.setDescription("This is the description. It could be quite long if you so desire. ");
		assertTrue("Not the expected file content", tournament.getName().contains("FirstRoundTest1"));
		assertTrue("Tournament sould contain 0 rounds", tournament.getRounds().size() == 0);
		FideSwissDutchAlgorithmV1 algoritm = new FideSwissDutchAlgorithmV1();
		tournament = algoritm.generateNextRound(tournament);
		assertTrue("Tournament should contain only one round", tournament.getRounds().size() == 1);
		// TestUtils.writeToFile(tournament, "firstRound.json");
	}

	@Test
	public void testValiateOrder1() throws UnsupportedEncodingException {
		InputStream inputStream = ChesspairingTournamentTest.class
				.getResourceAsStream("/fideSwissDutchAlgorithmTest/testValiateOrder1.json");
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		Gson gson = Tools.getGson();
		ChesspairingTournament tournament = gson.fromJson(reader, ChesspairingTournament.class);
		FideSwissDutchAlgorithmV1 algorithm = new FideSwissDutchAlgorithmV1();
		tournament = algorithm.generateNextRound(tournament);
		PairingSummary parringSummary = tournament.getParringSummary();

		assertTrue(parringSummary.getShortMessage().equals(PairingSummary.PARRING_NOT_OK));
		// System.out.println(parringSummary.getLongMessage());
		// TestUtils.writeToFile(tournament, "testValiateOrder1.json");
	}

	@Test
	public void testValiateOrder2() throws UnsupportedEncodingException {
		InputStream inputStream = ChesspairingTournamentTest.class
				.getResourceAsStream("/fideSwissDutchAlgorithmTest/testValiateOrder2.json");
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		Gson gson = Tools.getGson();
		ChesspairingTournament tournament = gson.fromJson(reader, ChesspairingTournament.class);
		FideSwissDutchAlgorithmV1 algorithm = new FideSwissDutchAlgorithmV1();
		tournament = algorithm.generateNextRound(tournament);
		PairingSummary parringSummary = tournament.getParringSummary();

		assertTrue(parringSummary.getShortMessage().equals(PairingSummary.PARRING_NOT_OK));
		// System.out.println(parringSummary.getLongMessage());
	}

	/**
	 * Starting point for the second round. It is more of an integration test
	 * because it will probably end up testing allot of components witch are not
	 * yet created. I'm really in a hurry of having an functional peering
	 * algorithm at hand. 
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testPareSecondround01() throws UnsupportedEncodingException {
		InputStream inputStream = ChesspairingTournamentTest.class
				.getResourceAsStream("/fideSwissDutchAlgorithmTest/testPareSecondRound01.json");
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		Gson gson = Tools.getGson();
		ChesspairingTournament tournament = gson.fromJson(reader, ChesspairingTournament.class);
		
		//all players should be present
		List<ChesspairingPlayer> players = tournament.getPlayers();
		int presentPlayers = 0;
		for (ChesspairingPlayer player: players){
			if (player.isPresent()){
				presentPlayers++;
			}
		}
		assertTrue("all players should be present", players.size()==presentPlayers);
		//TestUtils.writeToFile(tournament, "testPareSecondRound01.json");
		
		//FideSwissDutchAlgorithm algorithm = new FideSwissDutchAlgorithm();
		this.mTournament = tournament;
		
		
		tournament = this.generateNextRound(tournament);
		assertTrue("Tournament should have 2 rounds", tournament.getRounds().size()==2);
		ChesspairingRound round2 = tournament.getRounds().get(1);
		List<ChesspairingGame> games = round2.getGames();
		//System.out.println(games);
	}
	
	/**
	 * This is the first bug detected on the mobile application. The algorithm is fixed now
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDebugChessData01() throws UnsupportedEncodingException{
		InputStream inputStream = FideSwissDutchAlgorithmV1Test.class
				.getResourceAsStream("/fideSwissDutchAlgorithmTest/debug-chess-data-01.json");
		
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		Gson gson = Tools.getGson();
		ChesspairingTournament tournament = gson.fromJson(reader, ChesspairingTournament.class);
		FideSwissDutchAlgorithmV1 algorithm = new FideSwissDutchAlgorithmV1();
		ChesspairingTournament paredTournament = algorithm.generateNextRound(tournament);
		//just to avoid eclipse warning use the stuff
		paredTournament.getName();
	}
	
	@Test
	public void debugChessData02()throws UnsupportedEncodingException{
		InputStream inputStream = FideSwissDutchAlgorithmV1Test.class
				.getResourceAsStream("/fideSwissDutchAlgorithmTest/debug-chess-data-02.json");
		
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		Gson gson = Tools.getGson();
		ChesspairingTournament tournament = gson.fromJson(reader, ChesspairingTournament.class);
		FideSwissDutchAlgorithmV1 algorithm = new FideSwissDutchAlgorithmV1();
		ChesspairingTournament paredTournament = algorithm.generateNextRound(tournament);
		
		Set<String> players = new HashSet<>();
		
		//get the 3rd round
		List<ChesspairingRound> rounds = paredTournament.getRounds();
		ChesspairingRound round = rounds.get(2);
		List<ChesspairingGame> games = round.getGames();
		
		//make sure that the same player does not get peered more then one time
		for (ChesspairingGame game: games){
			//white player
			ChesspairingPlayer player = game.getWhitePlayer();
			String whiteKey = player.getPlayerKey();
			if (players.contains(whiteKey)){
				throw new IllegalStateException(player.getName()+"Has allready bean pared");
			}
			players.add(whiteKey);
			player = game.getBlackPlayer();
			if (player != null){
				String blackKey = player.getPlayerKey();
				if (players.contains(blackKey)){
					throw new IllegalStateException(player.getName()+"Has allready bean pared");
				}
				players.add(blackKey);
			}
		}
		
		System.out.println("End test debugChessData02 " + paredTournament.getName());
		
	}
}
