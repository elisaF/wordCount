package wordCount;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>This class counts the occurrences of words in a file</p>
 * 
 * Version 2.0 features:
 * <li>handles large files</li>
 * <li>uses multiple threads to process file</li>
 * <li>uses whitespace and punctuations as separator</li>
 * <li>writes sorted output to file</li>
 * 
 * Limitations:
 * <li>strips off period from tokens, which is sometimes not desirable (i.e. becomes i.e)</li>
 * <li>doesn't tokenize on apostrophe, whereas other tokenizers do (don't becomes don 't)</li>
 * <li>uses only one thread for reading, which can become a bottleneck</li>
 * <li>toLowerCase() won't work with some UTF-8 characters, and should use case folding instead</li>
 * <li>no logging</li>
 * 
 * @author Elisa
 */
public class WordCounter {

	protected static String[] punctuation = {"\\-\\-", "\\[", "\\]", "\"", ",", "\\!", "\\(", "\\)", ":", ";", "\\?", "`"};

	/**
	 * Tokenize a string
	 * @param line text to tokenize
	 * @param ignoreCase lowercase characters when <boolean>true</boolean>
	 * @return list of tokens
	 */
	protected static List<String> tokenize(String line, boolean ignoreCase) {
		List<String> cleanedTokens = new ArrayList<>();
		
		String regex = " |" + String.join("|", punctuation);
		String[] tokens = line.split(regex);
		cleanedTokens = clean(tokens, ignoreCase);
		
		return cleanedTokens;
	}
	
	/**
	 * Clean array of tokens
	 * @param tokens array of tokens
	 * @param ignoreCase lowercase characters when <boolean>true</boolean>
	 * @return cleaned tokens
	 */
	protected static List<String> clean(String[] tokens, boolean ignoreCase){
		List<String> cleanedTokens = new ArrayList<>();
		for(String token: tokens){
			String cleanedToken = token.trim().replaceAll("\\.+$|'$", ""); //delete trailing periods and apostrophes
			if(ignoreCase){
				cleanedToken = cleanedToken.toLowerCase();
			}
			if(!cleanedToken.isEmpty()){
				cleanedTokens.add(cleanedToken);
			}
		}
		return cleanedTokens;
	}
	
	/**
	 * Sort and print out the counts to a file
	 * @param countsMap map of tokens to counts
	 * @param fileName name of input filename to create output filename 
	 */
	protected static void formatOutput(ConcurrentHashMap<String, Integer> countsMap, String fileName) throws FileNotFoundException, UnsupportedEncodingException{
		String outputFileName = fileName + "_counts";
		Map<String, Integer> sortedCountsMap = sortByValue(countsMap);
		PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");
		for(String token: sortedCountsMap.keySet()){
			writer.println(token + ", " + countsMap.get(token));
		}
		writer.close();
	}
	
	/**
	 * Sort by Map value
	 * @param map map of keys and values to sort
	 * @return sorted map
	 */
	protected static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}
}
