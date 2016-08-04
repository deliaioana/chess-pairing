package eu.chessdata.chesspairing.algoritms.comparators;

import java.util.Comparator;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class ByInitialOrderIdReverce implements Comparator<ChesspairingPlayer> {

	@Override
	public int compare(ChesspairingPlayer o1, ChesspairingPlayer o2) {
		if (o1 == null) {
			throw new IllegalStateException("Player o1 is null");
		}
		if (o2 == null) {
			throw new IllegalStateException("Player o2 is null");
		}
		// use the default integer compare for help
		return -1 * Integer.compare(o1.getInitialOrderId(), o2.getInitialOrderId());
	}
}
