package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreBracket {
	private final FideSwissDutch fideSwissDutch;
	private final Double bracketScore;
	private final Set<String> bracketPlayers;
	private final Set<String> downfloters;

	public ScoreBracket(FideSwissDutch fideSwissDutch, List<String> playerKeys, Double bracketScore) {
		this.fideSwissDutch = fideSwissDutch;
		this.bracketPlayers = new HashSet<>();
		for (String key : playerKeys) {
			if (this.bracketPlayers.contains(key)) {
				throw new IllegalStateException("Same player added 2 times");
			}
			this.bracketPlayers.add(key);
		}
		this.downfloters = new HashSet<>();
		this.bracketScore = bracketScore;
	}

	public FideSwissDutch getFideSwissDutch() {
		return fideSwissDutch;
	}

	public Double getBracketScore() {
		return bracketScore;
	}

	public Set<String> getBracketPlayers() {
		return bracketPlayers;
	}

	public Set<String> getDownfloters() {
		return downfloters;
	}
	
	
}
