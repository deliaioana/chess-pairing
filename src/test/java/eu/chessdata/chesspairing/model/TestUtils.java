package eu.chessdata.chesspairing.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.Tools;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingTournament;

public class TestUtils {
	/**
	 * Create the generatedFiles folder in the rout of the project this folder
	 * is ignored by git and is meant to put the generated files while building
	 * this project
	 */
	public static void createIfNotPresentGeneratedFilesFolder() {

		File generatedFiles = new File(Tools.GENERATED_FILES);
		String absolutePath = generatedFiles.getAbsolutePath();
		if (generatedFiles.exists()) {
			return;
		}
		boolean folderCreated = generatedFiles.mkdirs();
		if (!folderCreated) {
			throw new IllegalStateException("Not able to create folder: " + absolutePath);
		}
		return;
	}

	public static ChesspairingTournament buildTournament(String tournamentName) {
		ChesspairingTournament tournament = new ChesspairingTournament();
		tournament.setName(tournamentName);
		tournament.setPlayers(buildPlayers());
		return tournament;
	}

	public static List<ChesspairingPlayer> buildPlayers() {
		List<ChesspairingPlayer> players = new ArrayList<ChesspairingPlayer>();
		players.add(buildPlayer("Bogdan Marian", 1450));
		players.add(buildPlayer("Lacramioara", 1320));
		players.add(buildPlayer("Calin Constantin", 1550));
		players.add(buildPlayer("Cezar Andrei", 1200));
		players.add(buildPlayer("Rares", 1600));
		players.add(buildPlayer("Catalin Ioan", 1750));
		players.add(buildPlayer("Maria", 1700));
		players.add(buildPlayer("Delia Ioana", 1750));
		return players;
	}

	public static ChesspairingPlayer buildPlayer(String playerName, int elo) {
		ChesspairingPlayer player = new ChesspairingPlayer();
		player.setName(playerName);
		player.setElo(elo);
		player.setPlayerKey(String.valueOf(elo) + playerName);
		return player;
	}

	/**
	 * Useful tool in debugging when sometimes you just want to look at a
	 * tournament in clear text instead of the debugging console
	 * 
	 * @param tournament
	 *            is the tournament that will be serialized to file
	 * @param fileName
	 *            is where the tournament will be serialized
	 */
	public static void writeToFile(ChesspairingTournament tournament, String fileName) {
		createIfNotPresentGeneratedFilesFolder();
		String filePath = Tools.GENERATED_FILES + "/" + fileName;
		try {
			Writer writer = new FileWriter(filePath);
			Gson gson = Tools.getGson();
			gson.toJson(tournament, writer);
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException("Not able to write to file: " + filePath + "\n" + e.getMessage());
		}

	}
}