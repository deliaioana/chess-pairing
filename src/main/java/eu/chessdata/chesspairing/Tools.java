package eu.chessdata.chesspairing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	 * It returns to lists of integer that represents the initial split of the
	 * ids of a list of size listSize
	 * 
	 * @param listSize
	 * @return
	 */
	public static List<List<Integer>> initialSplitList(int listSize) {
		// test that the listSize can be divided by 2
		int half = listSize / 2;
		if (listSize % 2 != 0) {
			throw new IllegalStateException("listSize should be multiple of 2 and listSize = " + listSize);
		}

		List<Integer> S1 = new ArrayList<>();
		List<Integer> S2 = new ArrayList<>();

		for (int i = 0; i < half; i++) {
			S1.add(i);
			S2.add(i + half);
		}
		List<List<Integer>> splitLists = new ArrayList<>();
		splitLists.add(S1);
		splitLists.add(S2);
		if (S1.size() + S2.size() != listSize) {
			throw new IllegalStateException("Someting is worng with my basic math understanding");
		}
		return splitLists;
	}

	public static Set<Integer[]> getPermutations(Integer[] intArray) {
		if (intArray == null) {
			throw new IllegalStateException("You are trying to generate permutations of a null array");
		}
		
		/*StringBuffer sb = new StringBuffer();
		for (Integer i: intArray){
			sb.append(String.valueOf(i)+" ");
		}
		System.out.println("start perms for: " + sb.toString());*/
		
		

		Set<Integer[]> permutations = new HashSet<>();

		/**
		 * the base case
		 */
		if (intArray.length == 0) {
			//permutations.add(new Integer[0]);
			return permutations;
		}

		/**
		 * select the first item and then sub permutation of the remaining items
		 * and then insert the first item into each position of each sub
		 * permutation recursively
		 */
		int first = intArray[0];
		Integer[] remainder = Arrays.copyOfRange(intArray, 1, intArray.length);
		Set<Integer[]> subPerms = getPermutations(remainder);
		for (Integer[] subPerm : subPerms) {
			for (int i = 0; i <= subPerm.length; i++) {
				Integer[] newPerm = Arrays.copyOf(subPerm, subPerm.length + 1);
				for (int j = newPerm.length - 1; j > i; --j) {
					newPerm[j] = newPerm[j - 1];
				}
				newPerm[i] = first;
				permutations.add(newPerm);
			}
		}

		return permutations;
	}
}
