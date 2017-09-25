package service;

public interface ITranslationService {

	void init();

	String translate(String sentence);

	void destroy();

	String translationCall(String toTranslate);

}