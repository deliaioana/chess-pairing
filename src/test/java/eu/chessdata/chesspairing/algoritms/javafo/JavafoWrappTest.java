package eu.chessdata.chesspairing.algoritms.javafo;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.Tools;
import eu.chessdata.chesspairing.algoritms.fideswissduch.v2.FideSwissDutchTest;
import eu.chessdata.chesspairing.model.ChesspairingTournament;

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
	public void test() {
		ChesspairingTournament tournament = loadFile("/jafafoWrapp/test1.json");
		fail("Not yet implemented");
	}

}
