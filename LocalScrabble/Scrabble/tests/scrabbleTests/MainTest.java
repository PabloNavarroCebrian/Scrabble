package scrabbleTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import scrabble.Main;

public class MainTest {
	
	private static final String inputDirectory = "resources/tests/inputs/";
	private static final String outputDirectory = "resources/tests/outputs/";
	private static final String expOutDirectory = "resources/tests/expectedOutputs/";

	private static String outFileTxt;
	private static String outFileJSON;
	private static String expOutFileTxt;
	private static String expOutFileJSON;

	static private boolean run(String[] args) {

		try {
			Main.main(args);
			return compareOutputs();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public static boolean compareOutputs() throws JSONException, IOException {
		
		return compareJSON() && compareTxt();
	}

	@Test
	void test_1() {
		assertTrue(run(getArgs("4easy", "1389871984")));
	}

	@Test
	void test_2() {
		assertTrue(run(getArgs("2easy", "951881097")));
	}
	
	@Test
	void test_3() {
		assertTrue(run(getArgs("1easy1medium1hard", "1547")));
	}
	
	@Test
	void test_4() {
		assertTrue(run(getArgs("2easy2medium", "1234567")));
	}
	
	@Test
	void test_5() {
		assertTrue(run(getArgs("3medium", "956192780")));
	}
	
	@Test
	void test_6() {
		assertTrue(run(getArgs("2easy1hard", "9876543")));
	}
	
	
	private String[] getArgs(String type, String seed) {
		outFileJSON =  outputDirectory + type + "PlayersOut.json";
		outFileTxt = outputDirectory + type + "PlayersOut.txt";
		expOutFileTxt = expOutDirectory + type + "PlayersExpOut.txt";
		expOutFileJSON = expOutDirectory + type + "PlayersExpOut.json";
		
		String[] args = {"-i", inputDirectory + type + "PlayersIn.txt", "-ot", outFileTxt, 
				"-oj", outFileJSON, "-m", "console", "-s", seed };
		return args;
	}
	
	private static boolean compareJSON() throws JSONException, FileNotFoundException {
		
		JSONObject jo1 = new JSONObject(new JSONTokener(new FileInputStream(outFileJSON)));
		JSONObject jo2 = new JSONObject(new JSONTokener(new FileInputStream(expOutFileJSON)));
		
		return jo1.similar(jo2);

	}
	
	private static boolean compareTxt() throws FileNotFoundException, IOException {
		
		boolean equal = true;
		
		try(BufferedReader expOut = new BufferedReader(new FileReader(expOutFileTxt))) {
			try(BufferedReader out = new BufferedReader(new FileReader(outFileTxt))) {
				String expOutLinea = expOut.readLine();
				String outLinea = out.readLine();
				while(equal && expOutLinea != null && outLinea != null) {
					if(!expOutLinea.equals(outLinea))
						equal = false;
					
					expOutLinea = expOut.readLine();
					outLinea = out.readLine();
				}
				
				if(equal && expOutLinea == null && outLinea == null) {
					return true;
				}
				
				return false;
			}
		}
	}
}
