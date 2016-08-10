package eu.chessdata.chesspairing;

import java.util.List;

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

}
