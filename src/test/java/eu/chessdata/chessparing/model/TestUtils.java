package eu.chessdata.chessparing.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.chessdata.chessparing.Tools;

public class TestUtils {
	/**
	 * Create the generatedFiles folder in the rout of the project this folder
	 * is ignored by git and is meant to put the generated files while
	 * building this project
	 */
	public static void createIfNotPresentGeneratedFilesFolder() {
		
		
		File generatedFiles =  new File(Tools.GENERATED_FILES);
		String absolutePath = generatedFiles.getAbsolutePath();
		if (generatedFiles.exists()){
			return;
		}
		boolean folderCreated = generatedFiles.mkdirs();
		if (!folderCreated){
			throw new IllegalStateException("Not able to create folder: "+absolutePath);
		}
		return;
	}

	public static ChessparingTournament buildTournament(String tournamentName) {
		ChessparingTournament tournament = new ChessparingTournament();
		tournament.setName(tournamentName);
		tournament.setPlayers(buildPlayers());
		return tournament;
	}

	public static List<ChessparingPlayer> buildPlayers() {
		List<ChessparingPlayer> players = new ArrayList<ChessparingPlayer>();
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

	public static ChessparingPlayer buildPlayer(String playerName, int elo) {
		ChessparingPlayer player = new ChessparingPlayer();
		player.setName(playerName);
		player.setElo(elo);
		player.setPlayerKey(String.valueOf(elo) + playerName);
		return player;
	}
	
	
}
