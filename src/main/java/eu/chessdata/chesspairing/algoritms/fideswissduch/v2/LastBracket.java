package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import eu.chessdata.chesspairing.Tools;

public class LastBracket extends ScoreBracket{


	public LastBracket(FideSwissDutch fideSwissDutch, Double bracketScore) {
		super(fideSwissDutch, bracketScore);
	}

	@Override
	public boolean pareBraket(ScoreBracket nextBraket) {
		if (nextBraket != null){
			throw new IllegalStateException("Last bracket should never have a nextBracket");
		}
		this.sortPlayers();
		Integer size  = this.bracketPlayers.size();
		Integer group[] = new Integer[size];
		for (int i=0;i<size;i++){
			group[i]=i;
		}
		Set<Integer[]>permutations = Tools.getPermutations(group);
		List<PairingResult>validPairings = new ArrayList<>();
		for (Integer[]permutation:permutations){
			PairingResult permResult = new PairingResult(bracketPlayers, permutation);
			if (permResult.isOk()){
				validPairings.add(permResult);
			}
		}
		if (validPairings.size()==0){
			throw new IllegalStateException("No posible parings for the las bracket. Time to think of backtracking");
		}
		
		throw new IllegalStateException("Please finish this. Time to sort valid permutations");
	}
	
	
}
