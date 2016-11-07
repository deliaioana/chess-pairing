package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.comparators.ComparatorChain;

import eu.chessdata.chesspairing.Tools;

public class ScoreBracket {
	private final FideSwissDutch fideSwissDutch;
	private final Double bracketScore;
	protected final List<Player> bracketPlayers;
	@SuppressWarnings("unused")
	private PairingResult bracketResult;
	protected Boolean lastBracket;
	private ScoreBracket nextBracket;

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

	protected void sortPlayers() {
		ComparatorChain<Player> comparatorChain = new ComparatorChain<>();
		comparatorChain.addComparator(Player.byPoints);
		comparatorChain.addComparator(Player.byElo);
		comparatorChain.addComparator(Player.byInitialRanking);

		Collections.sort(this.bracketPlayers, comparatorChain);
	}

	/**
	 * main algorithm for pairing bracket if nextBraket is null then this is the
	 * last bracket
	 * 
	 * @param lastRound
	 * @param nextBraket
	 */
	public boolean pareBraket(ScoreBracket nextBraket) {
		System.out.println("Pairing bracket: " + this.bracketScore);
		// compute initial state
		if (nextBraket == null) {
			this.lastBracket = true;
			pareLastBracket();
		}
		this.lastBracket=false;
		this.nextBracket = nextBraket;

		int playersCount = this.bracketPlayers.size();

		this.sortPlayers();

		// set the initial pairs
		int size = playersCount / 2;
		Integer groupA[] = new Integer[size];
		Integer groupB[] = new Integer[size];
		for (int i = 0; i < size; i++) {
			groupA[i] = i;
			groupB[i] = i + size;
		}

		PairingResult pairingResult = new PairingResult(bracketPlayers, groupA, groupB);
		if (pairingResult.isOk()) {
			processResult(pairingResult);
			return true;
		}

		Set<Integer[]> permutations = Tools.getPermutations(groupB);
		for (Integer[] permutation : permutations) {
			PairingResult permResult = new PairingResult(bracketPlayers, groupA, permutation);
			if (permResult.isOk()) {
				processResult(permResult);
				return true;
			}
		}

		if (this.bracketResult == null) {
			throw new IllegalStateException(
					"For the moment this error is only for degugging please fix the above aloritm for bracket 1.0. It should have some results");
		}
		for (Game game : this.bracketResult.getGames()) {
			if (game.getBlack() == null) {
				System.out.println("buy:" + game.getWhite().getPlayerKey());
			} else {
				System.out.println(game.getWhite().getPlayerKey() + " vs " + game.getBlack().getPlayerKey());
			}
		}
		return false;
	}

	/**
	 * set the result downfloat not paired if last round set as buy not paired.
	 * make sure that all the players have bean paired
	 */
	private void processResult(PairingResult result) {
		this.bracketResult = result;

		// make sure that all players have bean paired
		Set<Player> set = new HashSet<>();
		set.addAll(this.bracketPlayers);
		if (set.size() != bracketPlayers.size()) {
			throw new IllegalStateException("duplicate in bracketPlayers");
		}

		// identify not paired player;
		List<Game> games = result.getGames();
		for (Game game : games) {
			Player white = game.getWhite();
			Player black = game.getBlack();
			if (!set.remove(white)) {
				throw new IllegalStateException("The set did not contained white player");
			}
			if (!set.remove(black)) {
				throw new IllegalStateException("The set did not contained black player");
			}
		}
		if (set.size() > 1) {
			throw new IllegalStateException("Not all the players have bean pared");
		}
		if (set.size() == 1) {
			Player notPared = set.iterator().next();
			//<debug>
			if (this.lastBracket == null){
				throw new IllegalStateException("last Bracket boolean not initialized");
			}
			//</degug>
			/**
			 * if not last bracket downfloat else set as buy
			 */
			if (!this.lastBracket) {
				// not last bracket: time to downfloat
				downfloat(notPared);
			} else {
				// time to create buy
				throw new IllegalStateException("Please implement this? I tend to beleve that I should never reach this point");
			}
		}
	}

	/**
	 * downfloats the player and makes sure that this player initially belonged
	 * to this bracket
	 * 
	 * @param player
	 */
	private void downfloat(Player player) {
		if (!this.bracketPlayers.contains(player)) {
			throw new IllegalStateException("Player does not belong to this bracket");
		}

		this.nextBracket.addPlayer(player);
		player.setFloatingState(FloatingState.DOWNFLOATER);
		boolean ok = this.bracketPlayers.remove(player);
		if (!ok) {
			throw new IllegalStateException("For some reason I was not able to remove player");
		}
	}

	public boolean pareLastBracket() {
		LastBracket theLastBracket = new LastBracket(this.fideSwissDutch, this.bracketScore);
		theLastBracket.pareBraket(null);
		throw new IllegalStateException("Please finish pareLastBracket");
	}
}
