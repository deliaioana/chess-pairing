package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.IllegalSelectorException;

import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.Tools;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;

public class FideSwissDutchTest {

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
		System.out.println("End test");
	}

}
