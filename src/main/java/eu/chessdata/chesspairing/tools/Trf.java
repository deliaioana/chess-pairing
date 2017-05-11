package eu.chessdata.chesspairing.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private int numberOfTeams;
	private String typeOfTournament;
	private String chifArbiter;
	private String deputyChifArbiters;
	private String datesOfTheRounds;

	public static String buildTrf(ChesspairingTournament tournament) {

		Trf trf = new Trf(tournament);
		return trf.buildTrf();
	}

	public Trf(ChesspairingTournament chesspTournament) {
		this.dataIdentificationNumber = "no-dataIdentificationNumber-Implemented";
		this.tournamentName = chesspTournament.getName();
		this.city = chesspTournament.getCity();
		this.federation = chesspTournament.getFederation();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		this.dateOfStart = dateFormat.format(chesspTournament.getDateOfStart());
		this.dateOfEnd = dateFormat.format(chesspTournament.getDateOfEnd());
		
		this.numberOfPlayers = chesspTournament.getPlayers().size();
		this.numberOfTeams = 0;
		this.typeOfTournament = chesspTournament.getTypeOfTournament();
		this.chifArbiter = chesspTournament.getChifArbiter();
		this.deputyChifArbiters = chesspTournament.getDeputyChifArbiters();
		//this.city = tournament.get
	}
	
	private String buildTrf(){
		StringBuilder sb = new StringBuilder();
		sb.append("012 "+ this.tournamentName+"\n");
		sb.append("022 " + this.city+"\n");
		return sb.toString();
	}

}
