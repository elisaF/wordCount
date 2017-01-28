package wordCount;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class to support multiple threads processing a file
 * 
 * @author elisa
 *
 */
public class MultiThreadedWordCounter {

	public static void main(String[] args)
			throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException {

		final int threadCount = 10;

		String fileName = null;
		boolean ignoreCase = false;

		for (String argument : args) {
			if (argument.equals("-ignoreCase")) {
				ignoreCase = true;
			} else {
				fileName = argument;
			}
		}
		// BlockingQueue with a capacity of 200
		BlockingQueue<String> queue = new ArrayBlockingQueue<>(200);
		ConcurrentHashMap<String, Integer> countsMap = new ConcurrentHashMap<>();

		// create thread pool with given size
		ExecutorService service = Executors.newFixedThreadPool(threadCount);

		for (int i = 0; i < (threadCount - 1); i++) {
			service.submit(new ParseTask(queue, countsMap, ignoreCase));
		}

		// Wait for reading to complete
		service.submit(new ReadFileTask(queue, fileName)).get();

		service.shutdownNow(); // interrupt ParseTasks

		// Now wait for parsing to complete
		service.awaitTermination(1, TimeUnit.DAYS);

		// Write counts to file
		WordCounter.formatOutput(countsMap, fileName);
	}
}

/**
 * Class to read a file line by line
 * 
 * @author elisa
 *
 */
class ReadFileTask implements Runnable {

	private final BlockingQueue<String> queue;
	private final String fileName;

	public ReadFileTask(BlockingQueue<String> queue, String fileName) {
		this.queue = queue;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = br.readLine()) != null) {
				// block if the queue is full
				queue.put(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * Class to process a line read from the file
 * 
 * @author elisa
 *
 */
class ParseTask implements Runnable {

	protected final BlockingQueue<String> queue;
	protected ConcurrentHashMap<String, Integer> countsMap;
	protected boolean ignoreCase;

	protected ParseTask(BlockingQueue<String> queue, ConcurrentHashMap<String, Integer> countsMap, boolean ignoreCase) {
		this.queue = queue;
		this.countsMap = countsMap;
		this.ignoreCase = ignoreCase;
	}

	@Override
	public void run() {
		String line;
		while (true) {
			try {
				// block if the queue is empty
				line = queue.take();
				// do things with line
				countWords(line);
			} catch (InterruptedException ex) {
				break; // FileTask has completed
			}
		}
		// poll() returns null if the queue is empty
		while ((line = queue.poll()) != null) {
			// do things with line;
			countWords(line);
		}
	}

	/**
	 * Build a thread-safe hashmap of tokens to counts
	 * 
	 * @param line
	 *            text to process
	 */
	protected void countWords(String line) {
		List<String> tokens = WordCounter.tokenize(line, ignoreCase);
		for (String token : tokens) {
			countsMap.compute(token, (k, v) -> (v == null) ? 1 : v + 1); // must be atomic!
		}
	}
}
