package eu.chessdata.chesspairing.algoritms.fideswissduch;

import eu.chessdata.chesspairing.model.ChessparingTournament;

public interface Algorithm {
	public ChessparingTournament generateNextRound(ChessparingTournament tournament);
}
