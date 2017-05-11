package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.tools.Tools;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PlayerTest {
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
	
	private Player buildPlayer(Player source){
		ChesspairingPlayer chesspairingPlayer = new ChesspairingPlayer();
		chesspairingPlayer.setName(source.getName());
		chesspairingPlayer.setPlayerKey(source.getPlayerKey());
		chesspairingPlayer.setInitialOrderId(1);
		
		Player player = new Player(chesspairingPlayer,1);
		return player;
	}

	@Test
	public void test1(){
		FideSwissDutch algorithm = new FideSwissDutch();
		ChesspairingTournament dataTournament = loadFile("/fideswissdutchTest/v2/player/test1.json");
				
		algorithm.initializeAlgorithm(dataTournament);
		PairingTool pairingTool = new PairingTool(algorithm);
		
		//section of what should happen in coputeGames
		pairingTool.initializePlayers();//step1
		Set<Player> players = pairingTool.players;
		Player player = players.iterator().next();
		System.out.println("key = " + player.getPlayerKey());
		
		Player omolog = buildPlayer(player);
		
		if (!player.equals(omolog)){
			throw new IllegalStateException("the players are not equal and they should be");
		}
		
		int initialSize = players.size();
		players.remove(omolog);
		int secondSize = players.size();
		if (initialSize == secondSize){
			throw new IllegalStateException("The player was not removed from the set");
		}
		
		System.out.println("End test 1");
	}
	
	
	@Test
	public void equalsContract() {
	    EqualsVerifier.forClass(Player.class)
	    .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
	}
	
}
