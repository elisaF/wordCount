package wordCount;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WordCounterTest {

	private final String testInput = "A b--c\"d[e]f,g!h(j)k:l;m?nO`p";

	@Test
	public void testTokenize() {
		String testInput = "a b c";
		List<String> expectedOutput = new ArrayList<String>(Arrays.asList("a", "b", "c"));
		
		assertEquals(expectedOutput, WordCounter.tokenize(testInput, false));
	}
	
	@Test
	public void testTokenizeWithPunct() {
		List<String> expectedOutput = new ArrayList<String>(Arrays.asList("A", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "nO", "p"));
		List<String> actualOutput = WordCounter.tokenize(testInput, false);
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void testTokenizeWithIgnoreCaseTrue() {
		List<String> expectedOutput = new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "no", "p"));
		List<String> actualOutput = WordCounter.tokenize(testInput, true);
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void testTokenizeEmptyString() {
		String testInput = "this ' one";
		List<String> expectedOutput = new ArrayList<String>(Arrays.asList("this", "one"));
		List<String> actualOutput = WordCounter.tokenize(testInput, false);
		assertEquals(expectedOutput, actualOutput);
	}
}
