package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import origrad.roundforest.csv.analyzer.model.DataCount;

public class CsvAnalyzer implements ICsvAnalyzer {

	private static final int ITEMS_TO_PRINT = 1000;
	private int numThreads = 10;
	private AtomicInteger itemsProcessed = new AtomicInteger(0);
	private AtomicInteger itemsAdded = new AtomicInteger(0);
	private BlockingQueue<String> queue;
	private ExecutorService mainThreadPool;
	private Set<String> unique = new HashSet<>();
	private ConcurrentHashMap<String, Long> authors;
	private ConcurrentHashMap<String, Long> products;
	private ConcurrentHashMap<String, Long> words;
	private boolean loadingLines = true;
	private CountDownLatch latch = new CountDownLatch(numThreads);

	@Autowired
	@Qualifier("csvReader")
	ICsvReader reader;

	@Autowired
	@Qualifier("translationService")
	ITranslationService translationService;

	@Autowired
	@Qualifier("monitoringService")
	IMonitoringService monitoringService;

	public void init() {
		queue = new ArrayBlockingQueue<>(50000);

		authors = new ConcurrentHashMap<>();
		products = new ConcurrentHashMap<>();
		words = new ConcurrentHashMap<>();

		mainThreadPool = Executors.newFixedThreadPool(numThreads);
		for (int i = 0; i < numThreads; i++) {
			mainThreadPool.submit(() -> {
				while (loadingLines || queue.size() > 0) {
					String line = null;
					try {
						line = queue.poll(2000, TimeUnit.MILLISECONDS);
						if (line == null) {
							continue;
						}
						itemsProcessed.incrementAndGet();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					final String[] split = line.split(",");

					boolean added = unique.add(split[0]);
					if (added == false) {
						continue;
					}

					Long authorsAdd = authors.compute(split[3], (key, value) -> value == null ? 1 : value + 1);
					if (authorsAdd == 1l) {
						monitoringService.addString(split[3]);
					}
					Long productsAdd = products.compute(split[1], (key, value) -> value == null ? 1 : value + 1);
					if (productsAdd == 1l) {
						monitoringService.addString(split[1]);
					}
					String translated = translationService.translate(split[9]);

					// counting words
					String[] wordsplit = translated.split(" ");
					for (int j = 0; j < wordsplit.length; j++) {
						if (wordsplit[j].trim().equals("") == false) {
							Long wordsAdd = words.compute(wordsplit[j], (key, value) -> value == null ? 1 : value + 1);
							if (wordsAdd == 1l) {
								monitoringService.addString(wordsplit[j]);
							}
						}
					}

				}
				// System.out.println(Thread.currentThread().getName() + " - " +
				// queue.size());
				latch.countDown();
			});
		}

		String filename = System.getProperty("csvFile");
		if (filename != null) {
			analyze(filename);
		} else {
			System.err.println(
					"Missing property csvFile.\nUsage: -DcsvFile=/home/orig/git/roundforest-csv-analyzer/src/main/resources/Reviews.csv");
		}
	}

	@Override
	public void analyze(String filename) {
		long start = System.currentTimeMillis();
		reader.readFile(filename, (line) -> {
			try {
				queue.put(line);
				itemsAdded.incrementAndGet();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		loadingLines = false;

		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		printTopItems(authors, "Top authors");
		printTopItems(products, "Top Products");
		printTopItems(words, "Popular words");

		System.out.println("Lines read: " + itemsAdded + " Lines processed: " + itemsProcessed + " Execution time: "
				+ (System.currentTimeMillis() - start));

		translationService.destroy();
		mainThreadPool.shutdown();
	}

	private void printTopItems(Map<String, Long> map, String title) {
		List<DataCount> list = new ArrayList<>();
		for (String user : map.keySet()) {
			list.add(new DataCount(user, map.get(user)));
		}

		list.sort((o1, o2) -> o2.getCount().compareTo(o1.getCount()));
		int toPrint = ITEMS_TO_PRINT < list.size() ? ITEMS_TO_PRINT : list.size();
		List<DataCount> subList = list.subList(0, toPrint);
		subList.sort((o1, o2) -> o1.getData().compareTo(o2.getData()));
		System.out.println(title);
		for (int i = 0; i < toPrint; i++) {
			System.out.println(subList.get(i).getData() + " " + subList.get(i).getCount());
		}
		System.out.println("");

	}

	public void destroy() {

	}

}
