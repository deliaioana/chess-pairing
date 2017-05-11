package eu.chessdata.chesspairing;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.tools.Tools;

public class Api {
	
	public static String serializeTournament(ChesspairingTournament tournament){
		Gson gson = Tools.getGson();
		String stringTournament = gson.toJson(tournament);
		return stringTournament;
	}
	
	public static ChesspairingTournament deserializeTournament(String jSonTournament){
		Gson gson = Tools.getGson();
		ChesspairingTournament tournament = gson.fromJson(jSonTournament, ChesspairingTournament.class);
		return tournament;
	}
	
	
}
