package wordCount;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * <p>This class counts the occurrences of words in a file</p>
 * 
 * Version 1.0 features:
 * <li>load entire text file into memory</li>
 * <li>use whitespace as separator</li>
 * <li>write unsorted output to screen</li>
 * 
 * Limitations:
 * <li>does not handle end of line</li>
 * <li>does not handle punctuation</li>
 * <li>does not handle case</li>
 * <li>read entire text file into memory</li>
 * <li>output goes to screen and is not sorted</li>
 */
public class WordCounter {

	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		countWords(fileName);
	}
	/**
	 * Print frequency counts for words in given fileName
	 * @param fileName name of file to parse
	 * @throws IOException if error reading fileName
	 */
	protected static void countWords(String fileName) throws IOException {
		//read file
		String input = new String(Files.readAllBytes(Paths.get(fileName)));
		//tokenize
		String[] tokens = tokenize(input);
		//count
		HashMap<String, Integer> countsMap = buildCounts(tokens);
		
		//print
		formatOutput(countsMap);
	}

	/**
	 * Build a map of words to counts
	 * @param tokens array of words
	 * @return hashmap with words as keys, and counts as values
	 */
	protected static HashMap<String, Integer> buildCounts(String[] tokens) {
		HashMap<String, Integer> countsMap = new HashMap<>();
		for(String token: tokens){
			Integer count = countsMap.putIfAbsent(token, 1);
			if(count != null){
				count++;
				countsMap.put(token, count);
			}
		}
		return countsMap;
	}

	/**
	 * Tokenize a string
	 * @param input the string to tokenize
	 * @return array of tokens (words)
	 */
	protected static String[] tokenize(String input) {
		String[] tokens = input.split(" ");
		return tokens;
	}
	
	/**
	 * Print out the counts
	 * @param countsMap
	 */
	protected static void formatOutput(HashMap<String, Integer> countsMap){
		for(String token: countsMap.keySet()){
			System.out.println(token + ", " + countsMap.get(token));
		}
	}
}
