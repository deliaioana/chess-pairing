package eu.chessdata.chesspairing;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class ToolsTest {

	@Test
	public void initialSplitList() {
		List<List<Integer>> splitIds = Tools.initialSplitList(4);
		List<Integer> s1 = splitIds.get(0);
		List<Integer> s2 = splitIds.get(1);
		for (int i = 0; i < 2; i++) {
			Assert.assertTrue(s1.get(i) == s2.get(i) - 2);
		}
	}
	
	@Test
	public void getPermutations(){
		Integer[] intArray = {1,2,3,4,5,6,7,8,9,10};
		Set<Integer[]> perms = Tools.getPermutations(intArray);
		Assert.assertTrue("size should be grater than 0", perms.size()>0);
		for (Integer[] array:perms){
			StringBuffer sb = new StringBuffer();
			for (Integer i:array){
				sb.append(String.valueOf(i)+" ");
			}
			System.out.println(sb.toString());
		}
		
		System.out.println("End getPermutations");
	}

}
