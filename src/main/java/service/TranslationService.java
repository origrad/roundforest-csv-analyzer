package service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TranslationService implements ITranslationService {

	private static final char SPACE_CHAR = ' ';

	private static final int MAX_TRANSLATE_CHARS = 1000;

	private static final int PARALLEL_TRANSLATION_REQUESTS = 100;

	private ExecutorService translationThreadPool;

	@Override
	public void init() {
		translationThreadPool = Executors.newFixedThreadPool(PARALLEL_TRANSLATION_REQUESTS);
	}

	@Override
	public String translate(String sentence) {
		int i = 0;
		int size = sentence.length();
		StringBuilder translated = new StringBuilder();
		while (i < size) {
			int end = (i + MAX_TRANSLATE_CHARS) > size ? size : (i + MAX_TRANSLATE_CHARS);
			while (sentence.charAt(end - 1) != SPACE_CHAR && end < size) {
				end--;
			}
			final String substr = sentence.substring(i, end);
			i = end;
			Future<String> submit = translationThreadPool.submit(() -> {
				// here we'll have translation api call
				return substr;
			});

			try {
				translated.append(submit.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return translated.toString();
	}

	@Override
	public String translationCall(String toTranslate) {

		return toTranslate;
	}

	@Override
	public void destroy() {
		translationThreadPool.shutdown();
	}

}
