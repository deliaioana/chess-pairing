package eu.chessdata.chessparing;

import com.google.gson.Gson;

import eu.chessdata.chessparing.model.ChessparingTournament;

public class Api {
	
	public static String serializeTournament(ChessparingTournament tournament){
		Gson gson = Tools.getGson();
		String stringTournament = gson.toJson(tournament);
		return stringTournament;
	}
	
	public static ChessparingTournament deserializeTournament(String jSonTournament){
		Gson gson = Tools.getGson();
		ChessparingTournament tournament = gson.fromJson(jSonTournament, ChessparingTournament.class);
		return tournament;
	}
}
