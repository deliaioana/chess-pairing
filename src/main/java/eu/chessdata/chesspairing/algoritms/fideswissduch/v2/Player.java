package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.comparators.ComparatorChain;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class Player {
	protected static final Comparator<Player> comparator = buildChain();
	
	protected static final Comparator<Player> byPoints = new Comparator<Player>() {
		@Override
		public int compare(Player o1, Player o2) {
			Double po1 = o1.getPairingPoints();
			Double po2 = o2.getPairingPoints();
			return -1 * po1.compareTo(po2);
		}
	};

	protected static final Comparator<Player> byElo = new Comparator<Player>() {
		@Override
		public int compare(Player o1, Player o2) {
			Integer e1 = o1.getElo();
			Integer e2 = o2.getElo();
			return -1 * e1.compareTo(e2);
		}
	};

	protected static final Comparator<Player> byInitialRanking = new Comparator<Player>() {
		@Override
		public int compare(Player o1, Player o2) {
			Integer r1 = o1.getInitialRanking();
			Integer r2 = o2.getInitialRanking();
			return r1.compareTo(r2);
		}
	};

	protected String playerKey;
	protected String name;
	protected Integer initialRanking;
	protected Integer elo;
	protected List<Integer> colourHistory;
	protected List<String> playersHistory;
	protected Double pairingPoints;
	protected FloatingState floatingState;
	protected boolean wasBuy;

	public Player(ChesspairingPlayer player, int initialRanking) {
		this.playerKey = player.getPlayerKey();
		this.name = player.getName();
		this.initialRanking = initialRanking;
		this.elo = player.getElo();
		this.playersHistory = new ArrayList<>();
		this.colourHistory = new ArrayList<>();
		this.pairingPoints = 0.0;
		this.wasBuy = false;
	}

	private static Comparator<Player> buildChain() {
		ComparatorChain<Player> comparatorChain = new ComparatorChain<>();
		comparatorChain.addComparator(Player.byPoints);
		comparatorChain.addComparator(Player.byElo);
		comparatorChain.addComparator(Player.byInitialRanking);
		return comparatorChain;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.playerKey);
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayerKey() {
		return playerKey;
	}

	public void setPlayerKey(String playerKey) {
		this.playerKey = playerKey;
	}

	public List<Integer> getColourHistory() {
		return colourHistory;
	}

	public void setColourHistory(List<Integer> colourHistory) {
		this.colourHistory = colourHistory;
	}

	public Integer getInitialRanking() {
		return initialRanking;
	}

	public void setInitialRanking(Integer initialRanking) {
		this.initialRanking = initialRanking;
	}

	public Integer getElo() {
		return elo;
	}

	public void setElo(Integer elo) {
		this.elo = elo;
	}

	public List<String> getPlayersHistory() {
		return playersHistory;
	}

	public void setPlayersHistory(List<String> playersHistory) {
		this.playersHistory = playersHistory;
	}

	public Double getPairingPoints() {
		return pairingPoints;
	}

	public void setPairingPoints(Double pairingPoints) {
		this.pairingPoints = pairingPoints;
	}

	public FloatingState getFloatingState() {
		return floatingState;
	}

	public void setFloatingState(FloatingState floatingState) {
		this.floatingState = floatingState;
	}

	public Integer computeHistoryValue() {
		Integer value = 0;
		for (Integer item : this.colourHistory) {
			value += item;
		}
		return value;
	}

	/**
	 * Players prefers white if it played black last time if no games played
	 * then it prefersWhite so returns true
	 * 
	 * @param playerA
	 * @return
	 */
	protected boolean preferesWhite() {
		if (colourHistory.size() == 0) {
			return true;
		}
		Integer lastColor = colourHistory.get(colourHistory.size() - 1);

		if (lastColor == 1) {
			// last game was white
			return false;
		} else {
			// last game was black
			return true;
		}
	}

	/**
	 * Player prefers black if it played white last game if no games played it
	 * returns false;
	 * 
	 * @param player
	 * @return
	 */
	protected boolean prefersBlack() {
		if (colourHistory.size() == 0) {
			return false;
		}
		Integer lastColor = colourHistory.get(colourHistory.size() - 1);
		if (lastColor == -1) {
			// last game was black
			return false;
		} else {
			// last game was white
			return true;
		}
	}

	/**
	 * player can play white if the new color history is in -2 and 2 range and
	 * will not play the white color 3 times in the row
	 * 
	 * @param player
	 * @return true if it can and false if can not
	 */
	protected boolean canPlayWhite() {
		int history = computeHistoryValue() + 1;
		if ((-2 <= history) && (history <= 2)) {
			if (colourHistory.size() >= 2) {
				// 2 times white would mean 1+1;
				int k = colourHistory.size() - 2;
				int tailVal = colourHistory.get(k) + colourHistory.get(k + 1);
				if (tailVal == 2) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * player can play black if the new color history is in -2 2 range and will
	 * not play the black color 3 times in the row
	 * 
	 * @param player
	 * @return true if it can and false if can not
	 */
	protected boolean canPlayBlack() {
		int history = computeHistoryValue() - 1;
		if ((-2 <= history) && (history <= 2)) {
			if (colourHistory.size() >= 2) {
				// 2 times black would mean -1 -1
				int k = colourHistory.size() - 2;
				int tailVal = colourHistory.get(k) + colourHistory.get(k + 1);
				if (tailVal == -2) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerKey == null) ? 0 : playerKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerKey == null) {
			if (other.playerKey != null)
				return false;
		} else if (!playerKey.equals(other.playerKey))
			return false;
		return true;
	}

	public boolean wasBuy() {
		return wasBuy;
	}
}
