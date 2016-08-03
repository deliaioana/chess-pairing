package eu.chessdata.chesspairing.algoritms.fideswissduch;

import eu.chessdata.chesspairing.model.ChesspairingTournament;

public interface Algorithm {
	public ChesspairingTournament generateNextRound(ChesspairingTournament tournament);
}
