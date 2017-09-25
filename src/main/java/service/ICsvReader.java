package service;

import java.util.function.Consumer;

public interface ICsvReader {

	String readFile(String filename, Consumer<String> consumer);

}
