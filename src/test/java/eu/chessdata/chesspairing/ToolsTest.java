package eu.chessdata.chesspairing;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.paukov.combinatorics.Generator;

import eu.chessdata.chesspairing.tools.Tools;

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
		Date date = new Date();
		System.out.println("Start: " + date.toString());
		Integer[] intArray = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		Generator<Integer> gen = Tools.getPermutations(intArray);
		Assert.assertTrue("size should be grater than 0", 1>0);
		
		
		System.out.println("End getPermutations. size = "+gen.getNumberOfGeneratedObjects());
		date = new Date();
		System.out.println("End:    "+ date.toString());
	}
	
	@Test
	public void getFirstAndSecondHalf(){
		List<String> strings = new ArrayList<>();
		for (int i=0;i<10;i++){
			strings.add("item_"+i);
		}
		Integer[]first = Tools.getFirstHalfIds(strings.size());
		Integer[]second= Tools.getSecondHalfIds(strings.size());
		Assert.assertTrue(first.length==5);
		Assert.assertTrue(second.length==5);
	}
}
