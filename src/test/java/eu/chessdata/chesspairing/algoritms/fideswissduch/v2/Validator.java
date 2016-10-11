package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class Validator {
	public static void validateParingToolState(PairingTool pairingTool){
		samePlayersInBrakets(pairingTool);
	}
	
	/**
	 * makes sure that the total players are the same players as in the brakets
	 * @param pairingTool
	 */
	private static void samePlayersInBrakets(PairingTool pairingTool){
		//copy the set
		Set<Player> players = new HashSet<>();
		for (Player player: pairingTool.players){
			players.add(player);
		}
		
		for (Entry<Double,ScoreBracket> set: pairingTool.scoreBrackets.entrySet()){
			ScoreBracket braket = set.getValue();
			for (Player player: braket.getBracketPlayers()){
				if (!players.remove(player)){
					throw new IllegalStateException("Braket player not in thhe players list");
				}
			}
		}
		
		//size should be zero
		if (0 != players.size()){
			throw new IllegalStateException("Threre are players not asigned to any brakets");
		}
	}
}
