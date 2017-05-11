package eu.chessdata.chesspairing.tools;

import java.util.Comparator;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class SortTool {

	public static final Comparator<ChesspairingPlayer> playerByElo=new Comparator<ChesspairingPlayer>(){

		@Override
		public int compare(ChesspairingPlayer o1, ChesspairingPlayer o2) {
			Integer elo1 = o1.getElo();
			Integer elo2 = o2.getElo();
			return -1 * elo1.compareTo(elo2);
		}
		
	};
}
