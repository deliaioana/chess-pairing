package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.Tools;
import eu.chessdata.chesspairing.model.ChesspairingTournament;

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
		for (Player player: bracketPlayers){
			System.out.println("\t: " + player.getName()+": \t"+player.getElo());
		}
		
		Player player = pairingTool.get("-KMDl3KITCII80fhtdjL");
		System.out.println(player);
		System.out.println("End test 1: "+ player.getName());
	}

	@Test
	public void test2(){
		FideSwissDutch algorithm = new FideSwissDutch();
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/pairingTool/test1.json");
		dataTournament.setName("Test 2 sort bracket");
		algorithm.initializeAlgorithm(dataTournament);
		
		PairingTool pairingTool = new PairingTool(algorithm);
		pairingTool.initializePlayers();
		
		pairingTool.initializeScoreBrackets();
		ScoreBracket scoreBracket = pairingTool.scoreBrackets.get(2.0);
		
		List <Player> players = scoreBracket.getBracketPlayers();
		for (Player player: players ){
				System.out.println(player.getPairingPoints()+", " + player.getName()+","+player.getElo()+","+player.getInitialRanking());
		}
		
		scoreBracket.sortPlayers();
		
		System.out.println("Sorted list: ");
		List <Player> sortedPlayers = scoreBracket.getBracketPlayers();
		for (Player player: sortedPlayers ){
				System.out.println(player.getPairingPoints()+", " + player.getName()+","+player.getElo()+","+player.getInitialRanking());
		}
	}
}
