package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class PairingTool {
	private final FideSwissDutch fideSwissDutch;
	protected final Set<Player> players;
	private final int generationRoundId;
	protected final Map<Double, ScoreBracket> scoreBrackets;
	protected final List<Double> order;
	protected final List<Game> resultGames;

	public PairingTool(FideSwissDutch fideSwissDutch) {
		this.fideSwissDutch = fideSwissDutch;
		this.players = new HashSet<>();
		this.generationRoundId = fideSwissDutch.getGenerationRoundId();
		this.scoreBrackets = new HashMap<>();
		this.order = new ArrayList<>();
		this.resultGames = new ArrayList<>();
	}

	public void computeGames() {
		if (this.generationRoundId == 1) {
			throw new IllegalStateException("Please implement first round");
		}
		initializePlayers();
		initializeScoreBrackets();
		pairBrackets();
	}

	/**
	 * for each player it puts him in his default bracket
	 */
	protected void initializeScoreBrackets() {
		int lastRound = this.fideSwissDutch.getGenerationRoundId() - 1;
		for (Player player : this.players) {
			String key = player.getPlayerKey();
			Double score = this.fideSwissDutch.getPairingPoints(lastRound, key);
			if (!this.scoreBrackets.containsKey(score)) {
				ScoreBracket braket = new ScoreBracket(fideSwissDutch, score);
				this.scoreBrackets.put(score, braket);
			}
			ScoreBracket braket = this.scoreBrackets.get(score);
			// ad the player to the braket

			braket.addPlayer(player);
		}

		// sort the brackets
		for (Entry<Double, ScoreBracket> entry : this.scoreBrackets.entrySet()) {
			Double key = entry.getKey();
			this.order.add(key);
		}

		// set the order
		Collections.sort(order, Collections.reverseOrder());
	}

	/**
	 * Cycles all the brackets and initializes pairing
	 */
	protected void pairBrackets() {
		for (Double key : this.order) {
			pairBracketStandard(key);
		}
	}

	protected void pairBracketStandard(Double key) {
		ScoreBracket bracket = this.scoreBrackets.get(key);

		if (this.order.indexOf(key) < (this.order.size() - 1)) {
			// not last round
			int indexOfKey = this.order.indexOf(key);
			Double nextKey = this.order.get(indexOfKey + 1);
			ScoreBracket nextBraket = this.scoreBrackets.get(nextKey);
			bracket.pareBraket(nextBraket);
		} else {
			bracket.pareBraket(null);
		}
	}

	/**
	 * it initializes only the present players;
	 */
	protected void initializePlayers() {
		// set the present players
		List<ChesspairingPlayer> chesspairingPlayers = this.fideSwissDutch.getPresentPlayersList();
		for (ChesspairingPlayer chesspairingPlayer : chesspairingPlayers) {
			int initialRanking = this.fideSwissDutch.getInitialRanking(chesspairingPlayer.getPlayerKey());
			Player player = new Player(chesspairingPlayer, initialRanking);
			if (!this.players.contains(player)) {

				this.players.add(player);
			}
		}

		// set the pairing points
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Player player : players) {
				int roundNumber = this.fideSwissDutch.getGenerationRoundId() - 1;
				player.setPairingPoints(this.fideSwissDutch.getPairingPoints(roundNumber, player.getPlayerKey()));
			}
		}

		// set the paring history
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Player player : players) {
				String key = player.getPlayerKey();
				List<String> list = this.fideSwissDutch.getOponents(key);
				List<String> history = player.getPlayersHistory();
				history.addAll(list);
			}
		}

		// set the points
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Player player : players) {
				String key = player.getPlayerKey();

				int roundNr = this.generationRoundId - 1;
				Double pairingPoints = this.fideSwissDutch.getPairingPoints(roundNr, key);
				player.setPairingPoints(pairingPoints);
			}
		}

		// compute the color preference

		if (this.generationRoundId > 1) {
			for (Player player : players) {
				String key = player.getPlayerKey();
				List<Integer> list = this.fideSwissDutch.getColorHistory(key);
				List<Integer> history = player.getColourHistory();
				history.addAll(list);
			}
		}
	}

	/**
	 * gets the player by player key
	 * 
	 * @param playerKey
	 * @return
	 */
	public Player get(String key) {
		for (Player player : players) {
			String playerKey = player.getPlayerKey();
			if (key.equals(playerKey)) {
				return player;
			}
		}
		throw new IllegalStateException("Player not in the players set");
	}

	/**
	 * it updates the result games.
	 */
	protected void updateResultGames() {
		List<Game> games = new ArrayList<>();
		for (Entry<Double, ScoreBracket> set : scoreBrackets.entrySet()) {
			ScoreBracket braket = set.getValue();
			if (null == braket)
				continue;
			PairingResult result = braket.getBracketResult();
			// boolean ok = result.isOk();
			if (null == result) {
				continue;
			}
			if (!result.isOk()) {
				continue;
			}
			List<Game> breaketGames = result.getGames();
			if (null == breaketGames || breaketGames.size() == 0) {
				continue;
			}

			for (Game game : breaketGames) {
				int i = 0;
				if (null == game) {
					throw new IllegalStateException("This braket games list contains a null game" + i++);
				}
			}

			games.addAll(breaketGames);
		}
		// sort the games

		Collections.sort(games, Game.byPoints);
		this.resultGames.clear();
		this.resultGames.addAll(games);
	}

	/**
	 * it counts all the present players from the present players list and then it makes sure that the 
	 * players are the same as all the players from pared resultGames list
	 * 
	 * it will throws an error if something is not OK!
	 */
	public void makeSureAllPlayersGotPared() {
		// TODO Auto-generated method stub
		Set<Player> presentPlayers = new HashSet<>();
		presentPlayers.addAll(this.players);
		if (presentPlayers.size() != this.players.size()){
			throw new IllegalStateException("duplicate players in the players list");
		}
		
		for (Game game: resultGames){
			if (null == game){
				throw new IllegalStateException("Null game in result. This is serious! Go debug write now! :)");
			}
			if (!presentPlayers.remove(game.getWhite())){
				throw new IllegalStateException(game.getWhite().getPlayerKey()+" is not a present player");
			}
			if (!game.isBuyGame()){
				if (!presentPlayers.remove(game.getBlack())){
					throw new IllegalStateException(game.getBlack().getPlayerKey()+" is not a present player");
				}
			}
		}
		
		if (presentPlayers.size()!= 0){
			StringBuffer sb = new StringBuffer();
			for (Player player:presentPlayers){
				sb.append(", "+ player.getPlayerKey());
			}
			throw new IllegalStateException("Not all the players have bean pared: "+sb.toString());
		}
	}

}
