package eu.chessdata.chesspairing;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.model.PairingSummary;

public class Tools {
	public static final String GENERATED_FILES = "generatedFiles";
	private static Gson gson = buildGson();

	public static Gson getGson() {
		return Tools.gson;
	}

	private static Gson buildGson() {
		return new Gson();
	}

	public static PairingSummary buildParringStarted() {
		PairingSummary parringSummary = new PairingSummary();
		parringSummary.setShortMessage(PairingSummary.PARRING_NOT_PERFORMED);
		parringSummary.setLongMessage("Parring was not performed");
		return parringSummary;
	}
	
	/**
	 * It returns to lists of integer that represents the initial split of the ids of a list
	 * of size listSize
	 * @param listSize
	 * @return
	 */
	public static List<List<Integer>> initialSplitList(int listSize){
		//test that the listSize can be divided by 2
		int half = listSize/2;
		if (listSize % 2 != 0){
			throw new IllegalStateException("listSize should be multiple of 2 and listSize = " + listSize);
		}
		
		List<Integer> S1 = new ArrayList<>();
		List<Integer> S2 = new ArrayList<>();
		
		for (int i=0;i<half;i++){
			S1.add(i);
			S2.add(i+half);
		}
		List<List<Integer>> splitLists = new ArrayList<>();
		splitLists.add(S1);
		splitLists.add(S2);
		if (S1.size()+S2.size() != listSize){
			throw new IllegalStateException("Someting is worng with my basic math understanding");
		}
		return splitLists;
	}
}
