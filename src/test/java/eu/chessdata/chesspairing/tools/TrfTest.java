package eu.chessdata.chesspairing.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingSex;
import eu.chessdata.chesspairing.model.ChesspairingTitle;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.TestUtils;
import javafo.api.JaVaFoApi;

public class TrfTest {

	/**
	 * used to order first time players in order and add some test specific data for
	 * future tests
	 */
	@Test
	public void test1() {
		ChesspairingTournament tournament = TestUtils.loadFile("/trf/test1.json");
		// sort the players
		Collections.sort(tournament.getPlayers(), SortTool.playerByElo);
		int i = 1;
		for (ChesspairingPlayer player : tournament.getPlayers()) {
			player.setInitialOrderId(i++);
			player.setBirthDate(new Date());
			player.setSex(ChesspairingSex.MAN);
			String name = player.getName();
			if (name.equals("Delia Ioana") || name.equals("Maria") || name.equals("Lacramioara")
					|| name.equals("Mihaela") || name.equals("Delia Dinu")) {
				player.setSex(ChesspairingSex.WOMAN);
				player.setFederation("ROU");
			}
			if (name.equals("Cemil")) {
				player.setTitle(ChesspairingTitle.GRANDMASTER);
				player.setFederation("BEL");
				player.setFideNumber("13456781");
			}
			if (name.equals("Calin Constantin") || name.equals("Delia Ioana")) {
				player.setTitle(ChesspairingTitle.CANDIDATE_MASTER);
				player.setFederation("ROU");
			}
			if (name.equals("Rares")) {
				player.setTitle(ChesspairingTitle.INTERNATIONAL_MASTER);
				player.setFederation("ROU");
				player.setFideNumber("13456782");
			}
			if (name.equals("Bogdan Marian")) {
				player.setTitle(ChesspairingTitle.FIDE_MASTER);
				player.setFederation("ROU");
				player.setFideNumber("13456783");
			}

		}
		tournament.setCity("Iasi");
		tournament.setDateOfStart(new Date());
		tournament.setDateOfEnd(new Date());
		tournament.setTypeOfTournament("Individual: Swiss-System");
		TestUtils.writeToFile(tournament, "TrfTestTest1Ordered.json");
	}

	@Test
	public void test2() {
		ChesspairingTournament tournament = TestUtils.loadFile("/trf/test2.json");
		String trf = Trf.getTrf(tournament);
		InputStream inputStream = new ByteArrayInputStream(trf.getBytes());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JaVaFoApi.exec(1000, "Hello World", inputStream, outputStream);
		String resoult = outputStream.toString();
		System.out.println(trf);
		System.out.println(resoult);
	}

}
