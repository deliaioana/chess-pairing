package eu.chessdata.chesspairing.algoritms.comparators;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class ChainedComparator implements Comparator<ChesspairingPlayer>{
	
	private List<Comparator<ChesspairingPlayer>> listComparators;
	
	@SafeVarargs
	public ChainedComparator(Comparator<ChesspairingPlayer>...comparators) {
		this.listComparators = Arrays.asList(comparators);
	}

	@Override
	public int compare(ChesspairingPlayer p1, ChesspairingPlayer p2) {
		
		for (Comparator<ChesspairingPlayer> comparator: listComparators) {
			int result = comparator.compare(p1, p2);
			if (result != 0) {
				return result;
			}
		}
		
		return 0;
	}

}
