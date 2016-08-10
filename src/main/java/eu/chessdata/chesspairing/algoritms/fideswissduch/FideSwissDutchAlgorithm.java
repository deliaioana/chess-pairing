package eu.chessdata.chesspairing.algoritms.fideswissduch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.chessdata.chesspairing.Tools;
import eu.chessdata.chesspairing.algoritms.comparators.ByElo;
import eu.chessdata.chesspairing.algoritms.comparators.ByEloReverce;
import eu.chessdata.chesspairing.algoritms.comparators.ByInitialOrderIdReverce;
import eu.chessdata.chesspairing.model.ChesspairingResult;
import eu.chessdata.chesspairing.model.ChesspairingColour;
import eu.chessdata.chesspairing.model.ChesspairingGame;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.PairingSummary;

public class FideSwissDutchAlgorithm implements Algorithm {
	protected ChesspairingTournament mTournament;

	/**
	 * groups of players The key
	 */
	protected List<String> presentPlayerKeys;
	protected Map<String, Double> currentPoints;
	protected Map<Double, Map<String, ChesspairingPlayer>> groupsByResult;
	protected List<Double> orderedGroupKeys;
	// playerKey to color string
	protected Map<String, List<ChesspairingColour>> playerKeystoColorHistory;
	// playerKey to partners keys
	protected Map<String, List<String>> partnerHistory;
	// playerKey to upfloat counts
	protected Map<String, Integer> upfloatCounts;
	protected Map<String, Integer> downfloatCounts;
	protected Map<String, String> currentDownfloaters;
	protected ChesspairingGame buyGame;

	/**
	 * perform basic initializations and computations before the actual paring
	 * algorithm is started
	 */
	public ChesspairingTournament generateNextRound(ChesspairingTournament tournament) {
		this.mTournament = tournament;
		this.mTournament.setParringSummary(Tools.buildParringStarted());
		// more tan 1 players
		if (mTournament.getPlayers().size() < 2) {
			throw new IllegalStateException("Please ad at least 2 players or more");
		}

		// more rounds than totalRounds? For the moment I do not want to deal
		// with this use case
		if (mTournament.getTotalRounds() <= mTournament.getRounds().size()) {
			throw new IllegalStateException("You are trying to generate more rounds than totalRounds");
		}

		boolean validationOk = validateOrder();
		if (!validationOk) {
			return mTournament;
		}

		List<ChesspairingRound> rounds = this.mTournament.getRounds();
		if (rounds.size() <= 0) {
			generateFirstRound();
			return this.mTournament;
		}

		// make sure that the next round can be generated
		if (!canIGenerateNextRound()) {
			mTournament.getParringSummary().setShortMessage(PairingSummary.PARRING_NOT_OK);
			mTournament.getParringSummary().setLongMessage("You can not generate the next round!");
			return mTournament;
		}
		prepareNextRound();
		int roundNumber = mTournament.getRounds().size();
		computeInitialTournamentState(roundNumber);

		// computeCurrentResults(roundNumber);
		// computeCollorHistory(roundNumber);
		// computePartnersHistory(roundNumber);
		// computeUpfloatCounts(roundNumber);

		computeNextRound();
		throw new UnsupportedOperationException("Please implement this");
	}

	/**
	 * utility class that helps to set up vital properties required at next
	 * round computation
	 */
	protected void computeInitialTournamentState(int roundNumber) {
		computeCurrentResults(roundNumber);
		computeCollorHistory(roundNumber);
		computePartnersHistory(roundNumber);
		computeUpfloatCounts(roundNumber);
		computeDlownFloatCounts(roundNumber);
	}

	/**
	 * compute upfloat counts for the previous rounds
	 * 
	 * @param roundNumber
	 */
	protected void computeUpfloatCounts(int roundNumber) {
		this.upfloatCounts = new HashMap<>();
		for (String playerKey : this.presentPlayerKeys) {
			this.upfloatCounts.put(playerKey, new Integer(0));
		}

		for (int i = 1; i < roundNumber; i++) {
			for (ChesspairingPlayer upfloater : getRound(i).getUpfloaters()) {
				Integer count = this.upfloatCounts.get(upfloater.getPlayerKey()) + 1;
				this.upfloatCounts.put(upfloater.getPlayerKey(), count);
			}
		}
	}

	/**
	 * compute the downfloat counts from the previous rounds
	 * 
	 * @param roundNumber
	 */
	protected void computeDlownFloatCounts(int roundNumber) {
		this.downfloatCounts = new HashMap<>();
		for (String playerKey : this.presentPlayerKeys) {
			this.downfloatCounts.put(playerKey, new Integer(0));
		}

		for (int i = 1; i < roundNumber; i++) {
			for (ChesspairingPlayer downflaoter : getRound(i).getDownfloaters()) {
				Integer count = this.downfloatCounts.get(downflaoter.getPlayerKey()) + 1;
				this.downfloatCounts.put(downflaoter.getPlayerKey(), count);
			}
		}
	}

	/**
	 * compute the partner history for the previous rounds,
	 * 
	 * @param roundNumber
	 */
	private void computePartnersHistory(int roundNumber) {
		this.partnerHistory = new HashMap<>();
		for (String playerKey : this.presentPlayerKeys) {
			List<String> newPartnerList = new ArrayList<>();
			this.partnerHistory.put(playerKey, newPartnerList);
		}

		// iterate over rounds and collect the data
		for (int i = 1; i < roundNumber; i++) {
			for (ChesspairingGame game : getRound(i).getGames()) {
				if (game.getResult() != ChesspairingResult.BYE) {
					String whiteKey = game.getWhitePlayer().getPlayerKey();
					String blackKey = game.getBlackPlayer().getPlayerKey();
					utilAddPartnerToHistory(whiteKey, blackKey);
					utilAddPartnerToHistory(blackKey, whiteKey);
				}
			}
		}
	}

	/**
	 * utility function used to populate the partners history
	 * 
	 * @param playerKey
	 * @param parnerKey
	 */
	private void utilAddPartnerToHistory(String playerKey, String parnerKey) {
		if (playerKey.equals(parnerKey)) {
			throw new IllegalStateException(playerKey + " was paired against himself! ");
		}
		List<String> partners = this.partnerHistory.get(playerKey);
		if (partners.contains(parnerKey)) {
			// for the moment just throw exception
			throw new IllegalStateException(playerKey + " was paired with " + parnerKey + " more then once");
		}
		partners.add(parnerKey);
	}

	/**
	 * compute the color history considering the previous rounds, for the moment
	 * I'm just ignoring buys
	 * 
	 * @param roundNumber
	 */
	private void computeCollorHistory(int roundNumber) {
		this.playerKeystoColorHistory = new HashMap<>();
		// create the arrays for the present players
		for (Entry<String, Double> entry : currentPoints.entrySet()) {
			String key = entry.getKey();
			List<ChesspairingColour> newList = new ArrayList<>();
			playerKeystoColorHistory.put(key, newList);
		}

		for (int i = 1; i < roundNumber; i++) {
			for (ChesspairingGame game : getRound(i).getGames()) {
				// ignoring the buy
				if (game.getResult() != ChesspairingResult.BYE) {
					String whiteKey = game.getWhitePlayer().getPlayerKey();
					if (playerKeystoColorHistory.containsKey(whiteKey)) {
						playerKeystoColorHistory.get(whiteKey).add(ChesspairingColour.WHITE);
					}
					String blackKey = game.getBlackPlayer().getPlayerKey();
					if (playerKeystoColorHistory.containsKey(blackKey)) {
						playerKeystoColorHistory.get(blackKey).add(ChesspairingColour.BLACK);
					}
				}
			}
		}
	}

	/**
	 * compute all players results from the previous rounds and orders the
	 * results in the descending order
	 * 
	 * @param roundNumber
	 */
	private void computeCurrentResults(int roundNumber) {

		// reset playersByResult;
		this.groupsByResult = new HashMap<>();
		this.orderedGroupKeys = new ArrayList<>();

		// players key
		this.currentPoints = new HashMap<>();

		for (int i = 1; i < roundNumber; i++) {
			ChesspairingRound round = getRound(i);
			List<ChesspairingGame> games = round.getGames();
			for (ChesspairingGame game : games) {

				String whiteKey = game.getWhitePlayer().getPlayerKey();
				Double whitePoints = this.currentPoints.get(whiteKey);
				if (whitePoints == null) {
					whitePoints = 0.0;
					this.currentPoints.put(whiteKey, whitePoints);
				}
				if (game.getResult() == ChesspairingResult.WHITE_WINS) {
					whitePoints = whitePoints + 1;
				}
				if (game.getResult() == ChesspairingResult.BYE) {
					whitePoints = whitePoints + 0.5;
					// go to the next game
					continue;
				}

				String blackKey = game.getBlackPlayer().getPlayerKey();
				Double blackPoints = this.currentPoints.get(blackKey);
				if (blackPoints == null) {
					blackPoints = 0.0;
					this.currentPoints.put(blackKey, blackPoints);
				}
				if (game.getResult() == ChesspairingResult.BLACK_WINS) {
					blackPoints = blackPoints + 1;
				}
				if (game.getResult() == ChesspairingResult.DRAW_GAME) {
					whitePoints = whitePoints + 0.5;
					blackPoints = blackPoints + 0.5;
				}
				this.currentPoints.put(whiteKey, whitePoints);
				this.currentPoints.put(blackKey, blackPoints);
			}
		}

		// collect only the points from the present players;
		List<ChesspairingPlayer> allPlayers = mTournament.getPlayers();
		for (ChesspairingPlayer player : allPlayers) {
			if (!player.isPresent()) {
				this.currentPoints.remove(player.getPlayerKey());
			}
		}

		// set the present players
		this.presentPlayerKeys = new ArrayList<>();
		for (Entry<String, Double> entry : this.currentPoints.entrySet()) {
			presentPlayerKeys.add(entry.getKey());
		}
		if (presentPlayerKeys.size() <= 0) {
			throw new IllegalStateException("No present players. Please set atleast one player as present");
		}

		// put the results in playersByResult
		for (Entry<String, Double> entry : currentPoints.entrySet()) {
			// if playersByResult group does not exist then create it
			Double result = entry.getValue();
			if (!this.groupsByResult.containsKey(result)) {
				Map<String, ChesspairingPlayer> newGroup = new HashMap<>();
				this.groupsByResult.put(result, newGroup);
				this.orderedGroupKeys.add(result);
			}
			Map<String, ChesspairingPlayer> group = groupsByResult.get(result);
			String playerKey = entry.getKey();
			ChesspairingPlayer player = getPlayer(playerKey);
			group.put(playerKey, player);
		}
		// order the results
		Collections.reverse(this.orderedGroupKeys);
	}

	/**
	 * get a reference to the round by round number;
	 * 
	 * @param roundNumber
	 * @return
	 */
	private ChesspairingRound getRound(int roundNumber) {
		List<ChesspairingRound> rounds = mTournament.getRounds();
		for (ChesspairingRound round : rounds) {
			if (round.getRoundNumber() == roundNumber) {
				return round;
			}
		}
		throw new IllegalStateException(
				"Tournament in inconsistent state! Not able to find roundNumber = " + roundNumber);
	}

	/**
	 * create the next round and copy the players presence
	 */
	private void prepareNextRound() {
		ChesspairingRound round = new ChesspairingRound();
		int roundNumber = mTournament.getRounds().size() + 1;
		round.setRoundNumber(roundNumber);
		List<ChesspairingPlayer> players = new ArrayList<>();
		// round.setRoundNumber(roundNumber);
		for (ChesspairingPlayer player : mTournament.getPlayers()) {
			if (player.isPresent()) {
				players.add(player);
			}
		}
		round.setPresentPlayers(players);
		mTournament.getRounds().add(round);
	}

	/**
	 * it tests for the proper rounds identification (not to have 2 rounds with
	 * the same id) and if there is place to create one more round
	 * 
	 * @return true if a new round can be generated;
	 */
	private boolean canIGenerateNextRound() {
		int totalRounds = mTournament.getTotalRounds();
		int currentRouns = mTournament.getRounds().size();

		List<Integer> roundNumbers = new ArrayList<>();
		for (ChesspairingRound round : mTournament.getRounds()) {
			if (roundNumbers.contains(round.getRoundNumber())) {
				// you have 2 rounds with the same id
				return false;
			}
			roundNumbers.add(round.getRoundNumber());
		}

		if (currentRouns < totalRounds) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * For the moment I will sort the players in the descending order by elo and
	 * group them 2 by 2 I'm still wondering if I should group the first with
	 * the last and so on.
	 */
	private void generateFirstRound() {
		if (this.mTournament.getRounds().size() > 0) {
			throw new IllegalStateException("Tournament allready contains round 1");
		}

		Collections.sort(mTournament.getPlayers(), new ByEloReverce());
		List<ChesspairingGame> games = new ArrayList<>();
		List<ChesspairingPlayer> players = mTournament.getPlayers();
		int count = 0;
		ChesspairingGame game = new ChesspairingGame();
		game.setTableNumber(0);
		for (ChesspairingPlayer player : players) {
			count++;
			if (count % 2 == 1) {
				int tableNumber = game.getTableNumber() + 1;
				game = new ChesspairingGame();
				game.setTableNumber(tableNumber);
				game.setWhitePlayer(player);
				if (count == players.size()) {
					game.setResult(ChesspairingResult.BYE);
					games.add(game);
				}
			} else {
				game.setBlackPlayer(player);
				game.setResult(ChesspairingResult.NOT_DECIDED);
				games.add(game);
			}
		}
		ChesspairingRound round = new ChesspairingRound();
		round.setRoundNumber(1);
		round.setGames(games);

		// is the first round so wee can create all new data
		List<ChesspairingRound> rounds = new ArrayList<>();

		// add the round
		rounds.add(round);

		// and wee set the rounds
		mTournament.setRounds(rounds);

		PairingSummary firstRoundOk = new PairingSummary();
		firstRoundOk.setShortMessage(PairingSummary.PARRING_OK);
		firstRoundOk.setLongMessage("First round was generated");
		mTournament.setParringSummary(firstRoundOk);
	}

	/**
	 * if players with no initialOrderId or the same initialOrderId then do not
	 * pare and set the paring message accordingly. No initial order is
	 * considered smaller or equal to 0
	 * 
	 * @return true if validation is OK and false otherwise
	 */
	private boolean validateOrder() {
		// players with no id
		List<String> playersNoId = new ArrayList<>();
		List<ChesspairingPlayer> players = mTournament.getPlayers();
		for (ChesspairingPlayer player : players) {
			if (player.getInitialOrderId() <= 0) {
				playersNoId.add(player.getName());
			}
		}
		if (playersNoId.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("You have players with no initialOrderId: ");
			for (String name : playersNoId) {
				sb.append(name + ", ");//
			}
			int id = sb.lastIndexOf(", ");
			sb.replace(id, sb.length(), "");
			PairingSummary parringSummary = new PairingSummary();
			parringSummary.setShortMessage(PairingSummary.PARRING_NOT_OK);
			parringSummary.setLongMessage(sb.toString());
			mTournament.setParringSummary(parringSummary);
			return false;
		}

		// players with the same id
		Map<Integer, ChesspairingPlayer> map = new HashMap<>();
		for (ChesspairingPlayer player : players) {
			if (map.containsKey(player.getInitialOrderId())) {
				StringBuffer sb = new StringBuffer();
				sb.append("You have players with the same initialOrderId: ");
				sb.append(map.get(player.getInitialOrderId()).getName() + " and ");
				sb.append(player.getName());
				PairingSummary parringSummary = new PairingSummary();
				parringSummary.setShortMessage(PairingSummary.PARRING_NOT_OK);
				parringSummary.setLongMessage(sb.toString());
				mTournament.setParringSummary(parringSummary);
				return false;
			} else {
				map.put(player.getInitialOrderId(), player);
			}
		}
		return true;
	}

	private ChesspairingPlayer getPlayer(String playerKey) {
		for (ChesspairingPlayer player : mTournament.getPlayers()) {
			if (player.getPlayerKey().equals(playerKey)) {
				return player;
			}
		}
		throw new IllegalStateException("You are searching by the wrong key: " + playerKey);
	}

	private void computeNextRound() {
		this.currentDownfloaters = new HashMap<>();

		/**
		 * start the iteration over groups in the descending order. In order to
		 * avoid thread weird behaviour because the group keys wee copy the keys
		 * before wee iterate
		 */
		List<Double> copyGroupKeys = new ArrayList<>(orderedGroupKeys);

		// while no need to downfloat then keep downfloating
		boolean someoneWasDownfloated = true;
		while (someoneWasDownfloated) {
			someoneWasDownfloated = false;
			for (Double groupKey : copyGroupKeys) {
				Map<String, ChesspairingPlayer> group = groupsByResult.get(groupKey);
				int size = group.size();
				// if modulo 2 != 0 then find a downfloater
				if ((size % 2) != 0) {
					someoneWasDownfloated = true;
					downfloatSomeoneInGroup(groupKey);
				}
			}
		}
		

		for (Double groupKey: copyGroupKeys){
			boolean paringOK = pareGroup(groupKey);
			if (!paringOK){
				throw new IllegalStateException("What to do when group was not able to be pared?");
			}
		}
	}
	
	/**
	 * It tries to pare a group
	 * 
	 * @param groupKey
	 * @return
	 */
	private boolean pareGroup(Double groupKey){
		Map<String,ChesspairingPlayer> group = this.groupsByResult.get(groupKey);
		
		List<ChesspairingPlayer> players = new ArrayList<>();
		for (Entry<String, ChesspairingPlayer> entry : group.entrySet()) {
			players.add(entry.getValue());
		}
		//order the group
		Collections.sort(players, new ByInitialOrderIdReverce());
		Collections.sort(players, new ByElo());
		//by points just in case it was a downfloater in the group
		Collections.sort(players, new Comparator<ChesspairingPlayer>() {
			@Override
			public int compare(ChesspairingPlayer o1, ChesspairingPlayer o2) {
				Double pointsO1 = currentPoints.get(o1.getPlayerKey());
				Double pointsO2 = currentPoints.get(o2.getPlayerKey());
				return Double.compare(pointsO1, pointsO2);
			}
		});
		if (players.size()%2 != 0){
			throw new IllegalStateException("You should have resulved groups count before");
		}
		//first set the initial order
		
		for (int i=0;i<players.size();i++){
			
		}
		
		//try natural paring
		throw new IllegalStateException("Please finish pareGroup");
	}

	/**
	 * 
	 * 
	 * @return
	 */
	private boolean downfloatSomeoneInGroup(Double groupKey) {
		int lastIndex = this.orderedGroupKeys.size() - 1;
		int thisIndex = this.orderedGroupKeys.indexOf(groupKey);
		Map<String, ChesspairingPlayer> group = groupsByResult.get(groupKey);

		// order the players
		List<ChesspairingPlayer> players = new ArrayList<>();
		for (Entry<String, ChesspairingPlayer> entry : group.entrySet()) {
			players.add(entry.getValue());
		}

		Collections.sort(players, new ByElo());
		Collections.sort(players, new ByInitialOrderIdReverce());
		Collections.sort(players, new Comparator<ChesspairingPlayer>() {
			@Override
			public int compare(ChesspairingPlayer o1, ChesspairingPlayer o2) {
				int countO1 = 0;
				if (currentDownfloaters.containsKey(o1.getPlayerKey())) {
					countO1++;
				}
				int countO2 = 0;
				if (currentDownfloaters.containsKey(o2.getPlayerKey())) {
					countO2++;
				}
				return Integer.compare(countO1, countO2);
			}
		});

		for (ChesspairingPlayer player : players) {
			if (!currentDownfloaters.containsKey(player.getPlayerKey())) {
				if (lastIndex == thisIndex) {
					/**
					 * this is the player that will get a buy set as buy and
					 * remove from group
					 */
					setAsBuy(player);
					group.remove(player.getPlayerKey());
					return true;
				} else {
					// increase player downfloat count
					String key = player.getPlayerKey();
					int count = downfloatCounts.get(key) + 1;
					downfloatCounts.put(key, count);
					// move player to the next group
					Double nextKey = this.orderedGroupKeys.get(thisIndex + 1);
					Map<String, ChesspairingPlayer> nextGroup = groupsByResult.get(nextKey);
					group.remove(key);
					nextGroup.put(key, player);
					return true;
				}

			}
		}

		throw new IllegalStateException("You should allway have bean able to downfloat someone. Please investigate");
	}

	private void setAsBuy(ChesspairingPlayer player) {
		if (this.buyGame != null) {
			throw new IllegalStateException("You should have set a player as buy only one time!");
		}
		this.buyGame = new ChesspairingGame();
		this.buyGame.setWhitePlayer(player);
		this.buyGame.setResult(ChesspairingResult.BYE);
	}
}
