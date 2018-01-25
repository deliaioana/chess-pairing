package eu.chessdata.chesspairing.algoritms.javafo;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.algoritms.fideswissduch.Algorithm;
import eu.chessdata.chesspairing.algoritms.fideswissduch.v2.FideSwissDutchTest;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.TestUtils;
import eu.chessdata.chesspairing.tools.Tools;

public class JavafoWrappTest {
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
		ChesspairingTournament tournament = TestUtils.loadFile("/jafafoWrapp/test1.json");
		Algorithm algorithm = new JavafoWrapp();
		ChesspairingTournament nextRoundTournament = algorithm.generateNextRound(tournament);
		TestUtils.writeToFile(nextRoundTournament, "test1NextRound.json");
	}

}
