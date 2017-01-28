package wordCount;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.experimental.runners.Enclosed;

@RunWith(Enclosed.class)
public class MultiThreadedWordCounterTest{
private static ConcurrentHashMap<String, Integer> expectedCounts = new ConcurrentHashMap<String, Integer>();
private static ConcurrentHashMap<String, Integer> expectedCountsIgnoreCase = new ConcurrentHashMap<String, Integer>();
private final static String testInput = "this is is simple Simple test test test test.";	
	
	public static class ParseTaskTest extends ParseTask {
		protected static BlockingQueue<String> queue;
		protected static ConcurrentHashMap<String, Integer> countsMap = new ConcurrentHashMap<>();
		protected static boolean ignoreCase;
		
		@BeforeClass
		public static void setupClass(){
			expectedCounts.put("this", 1);
			expectedCounts.put("is", 2);
			expectedCounts.put("simple", 1);
			expectedCounts.put("Simple", 1);
			expectedCounts.put("test", 4);
			
			expectedCountsIgnoreCase.put("this", 1);
			expectedCountsIgnoreCase.put("is", 2);
			expectedCountsIgnoreCase.put("simple", 2);
			expectedCountsIgnoreCase.put("test", 4);
		}
		
		@Before
		public void setup(){
			countsMap.clear();
		}
		
		public ParseTaskTest() {
			super(queue, countsMap, ignoreCase);
		}

		@Test
		public void testCountWords() {
			ParseTaskTest parseClass = new ParseTaskTest();
			parseClass.countWords(testInput);
			assertTrue(expectedCounts.keySet().containsAll(countsMap.keySet()));
			for(String key: expectedCounts.keySet()){
				assertEquals(expectedCounts.get(key), countsMap.get(key));
			}
		}
		
		@Test
		public void testCountWordsIgnoreCase() {
			ignoreCase = true;
			ParseTaskTest parseClass = new ParseTaskTest();
			parseClass.countWords(testInput);
			System.out.println(countsMap);
			assertTrue(expectedCountsIgnoreCase.keySet().containsAll(countsMap.keySet()));
			for(String key: expectedCountsIgnoreCase.keySet()){
				assertEquals(expectedCountsIgnoreCase.get(key), countsMap.get(key));
			}
		}
	}
}
