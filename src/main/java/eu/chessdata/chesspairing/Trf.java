package eu.chessdata.chesspairing;

import eu.chessdata.chesspairing.model.ChesspairingTournament;

/**
 * Class with public static methods that are used for exporting a tournament as a trf
 * 
 * @author bogda
 *
 */
public class Trf {
	public static String buildTrf(ChesspairingTournament tournament){
		return "my trf";
	}
	
	/**
	 * I have never saw a model of trf using this
	 * Position 1  - 3  Data-Identification-number (??2 for tournament data)
	 */
	@SuppressWarnings("unused")
	private String dataIdentificationNumber;
	private String tournamentName;
	private String city;
	private String federation;
	private String dateOfStart;
	private String dateOfEnd;
	private String numberOfPlayers;
	private String numberOfTeams;
	private String typeOfTournament;
	private String chifArbiter;
	private String deputyChifArbiters;
	private String datesOfTheRounds;
	
	
}
