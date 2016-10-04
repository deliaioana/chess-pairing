package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

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
		dataTournament.setName("Test 4 tournament");
		algorithm.initializeAlgorithm(dataTournament);
		
		PairingTool pairingTool = new PairingTool(algorithm);
		pairingTool.initializePlayers();
	}

}
