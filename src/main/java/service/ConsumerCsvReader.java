package service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ConsumerCsvReader implements ICsvReader {

	@Override
	public String readFile(String filename, Consumer<String> consumer) {
		// read file into stream, try-with-resources
		Path path = null;
		try {
			path = Paths.get(new URI("file:" + filename));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try (Stream<String> stream = Files.lines(path)) {
			stream.forEach(consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
