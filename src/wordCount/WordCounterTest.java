package wordCount;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WordCounterTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final static HashMap<String, Integer> expectedCounts = new HashMap<String, Integer>();
	
	@BeforeClass
	public static void setup(){
		expectedCounts.put("this", 1);
		expectedCounts.put("is", 2);
		expectedCounts.put("simple", 1);
		expectedCounts.put("Simple", 1);
		expectedCounts.put("test", 3);
		expectedCounts.put("test.", 1);
	}
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testBuildCounts() {
		String[] testInput = {"this", "is", "is", "simple", "Simple", "test", "test", "test", "test."};
		
		HashMap<String, Integer> actualCounts = WordCounter.buildCounts(testInput);
		assertTrue(expectedCounts.keySet().equals(actualCounts.keySet()));
		for(String token: testInput){
			assertEquals(expectedCounts.get(token), actualCounts.get(token));
		}
	}

	@Test
	public void testTokenize() {
		String testInput = "a b c";
		String[] expectedOutput = {"a", "b", "c"};
		
		assertArrayEquals(expectedOutput, WordCounter.tokenize(testInput));
	}
	
	@Test
	public void testFormatOutput(){
		WordCounter.formatOutput(expectedCounts);
		String expectedOutput = ("test, 3\nthis, 1\nis, 2\nsimple, 1\ntest., 1\nSimple, 1\n");
		assertEquals(expectedOutput, outContent.toString());
	}

}
