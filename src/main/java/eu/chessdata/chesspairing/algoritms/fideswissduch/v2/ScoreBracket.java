package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.List;

public class ScoreBracket {
	private final FideSwissDutch fideSwissDutch;
	private final Double bracketScore;
	private final List<Player> bracketPlayers;

	public ScoreBracket(FideSwissDutch fideSwissDutch, Double bracketScore) {
		this.fideSwissDutch = fideSwissDutch;
		this.bracketPlayers = new ArrayList<>();

		this.bracketScore = bracketScore;
	}

	public FideSwissDutch getFideSwissDutch() {
		return fideSwissDutch;
	}

	public Double getBracketScore() {
		return bracketScore;
	}

	public List<Player> getBracketPlayers() {
		return bracketPlayers;
	}

	public void addPlayer(Player player) {
		if (this.bracketPlayers.contains(player)) {
			throw new IllegalStateException("Trining to add same player 2 times");
		}

		player.setFloatingState(getFloatingState(player));
		this.bracketPlayers.add(player);
	}

	private FloatingState getFloatingState(Player player) {
		FloatingState floatingState = FloatingState.STANDARD;

		Double points = player.getPairingPoints();
		if (points < this.bracketScore) {
			floatingState = FloatingState.UPFLOATER;

		} else if (points > this.bracketScore) {
			floatingState = FloatingState.DOWNFLOATER;

		}
		return floatingState;
	}
}
