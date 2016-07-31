package eu.chessdata.chessparing.algoritms;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chessparing.Tools;
import eu.chessdata.chessparing.model.ChessparingTournament;
import eu.chessdata.chessparing.model.ChessparingTournamentTest;

public class FideSwissDutchAlgorithmTest {

	@Test
	public void firstRoundTest1() throws UnsupportedEncodingException {
		InputStream inputStream = ChessparingTournamentTest.class
				.getResourceAsStream("/fideSwissDutchAlgorithmTest/zeroRounds.json");
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		Gson gson = Tools.getGson();
		ChessparingTournament tournament = gson.fromJson(reader, ChessparingTournament.class);
		tournament.setTotalRounds(7);
		tournament.setName("FirstRoundTest1 (just a name)");
		tournament.setDescription("This is the description. It could be quite long if you so desire. ");
		assertTrue("Not the expected file content", tournament.getName().contains("FirstRoundTest1"));
		assertTrue("Tournament sould contain 0 rounds", tournament.getRounds().size() == 0);
		FideSwissDutchAlgorithm algoritm = new FideSwissDutchAlgorithm();
		tournament = algoritm.generateNextRound(tournament);
		assertTrue("Tournament should contain only one round", tournament.getRounds().size()==1);
		//TestUtils.writeToFile(tournament, "firstRound.json");
	}
}
