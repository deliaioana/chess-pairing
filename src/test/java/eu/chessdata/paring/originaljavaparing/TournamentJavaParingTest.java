package eu.chessdata.paring.originaljavaparing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import junit.framework.TestCase;

public class TournamentJavaParingTest extends TestCase {
	
	public void test1LoadFromFile() throws FileNotFoundException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("tournamentJavaParingTest/TestTorneoNatalizio2010-01-NoRounds.txt").getFile());
		if (!file.exists()) {
			throw new IllegalStateException("File does not exist: " + file.getAbsolutePath());
		}

		TournamentJavaParing tournament = new TournamentJavaParing();
		BufferedReader br;

		br = new BufferedReader(new FileReader(file.getAbsolutePath()));
		tournament.loadFromFile(br);
	}
}
