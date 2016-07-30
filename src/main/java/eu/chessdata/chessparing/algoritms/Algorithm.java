package eu.chessdata.chessparing.algoritms;

import eu.chessdata.chessparing.model.ChessparingTournament;

public interface Algorithm {
	public ChessparingTournament generateNextRound(ChessparingTournament tournament);
}
