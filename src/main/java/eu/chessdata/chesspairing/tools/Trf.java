package eu.chessdata.chesspairing.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingTournament;

/**
 * Class with public static methods that are used for exporting a tournament as
 * a trf
 * 
 * @author bogda
 *
 */
public class Trf {

	/**
	 * I have never saw a model of trf using this Position 1 - 3
	 * Data-Identification-number (??2 for tournament data)
	 */
	@SuppressWarnings("unused")
	private String dataIdentificationNumber;
	private String tournamentName;
	private String city;
	private String federation;
	private String dateOfStart;
	private String dateOfEnd;
	private int numberOfPlayers;
	private int numberOfRatedPlayers;
	private int numberOfTeams;
	private String typeOfTournament;
	private String chifArbiter;
	private String deputyChifArbiters;
	private String datesOfTheRounds;
	private String timesPerMovesGame;

	public static String getTrf(ChesspairingTournament tournament) {

		Trf trf = new Trf(tournament);
		return trf.buildTrf();
	}

	public Trf(ChesspairingTournament chesspTournament) {
		this.dataIdentificationNumber = "no-dataIdentificationNumber-Implemented";
		this.tournamentName = chesspTournament.getName();
		this.city = chesspTournament.getCity();
		if (chesspTournament.getFederation() != null) {
			this.federation = chesspTournament.getFederation();
		} else {
			this.federation = "";
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		this.dateOfStart = dateFormat.format(chesspTournament.getDateOfStart());
		this.dateOfEnd = dateFormat.format(chesspTournament.getDateOfEnd());

		this.numberOfPlayers = chesspTournament.getPlayers().size();

		// number of rated players
		this.numberOfPlayers = 0;
		for (ChesspairingPlayer player : chesspTournament.getPlayers()) {
			if (player.getElo() != 0) {
				this.numberOfPlayers++;
			}
		}

		this.numberOfTeams = 0;
		if (chesspTournament.getTypeOfTournament() != null) {
			this.typeOfTournament = chesspTournament.getTypeOfTournament();
		} else {
			this.typeOfTournament = "";
		}
		this.chifArbiter = chesspTournament.getChifArbiter();
		this.deputyChifArbiters = chesspTournament.getDeputyChifArbiters();

		this.timesPerMovesGame = "moves/time, increment";
	}

	private String buildTrf() {
		StringBuilder sb = new StringBuilder();
		sb.append("012 " + this.tournamentName + "\n");
		sb.append("022 " + this.city + "\n");
		sb.append("032 " + this.federation + "\n");
		sb.append("042 " + this.dateOfStart + "\n");
		sb.append("052 " + this.dateOfEnd + "\n");
		sb.append("062 " + this.numberOfPlayers + "\n");
		sb.append("072 " + this.numberOfRatedPlayers + "\n");
		sb.append("082 " + this.numberOfTeams + "\n");
		sb.append("092 " + this.typeOfTournament + "\n");
		sb.append("102 " + this.chifArbiter + "\n");
		sb.append("112 " + this.deputyChifArbiters + "\n");
		sb.append("122 " + this.timesPerMovesGame);

		// XXR 9 ? to be clarified what it means
		sb.append("XXR 9" + "\n");

		return sb.toString();
	}

}
