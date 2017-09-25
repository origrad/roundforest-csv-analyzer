package origrad.roundforest.csv.analyzer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import service.ConsumerCsvReader;
import service.CsvAnalyzer;
import service.ICsvAnalyzer;
import service.ICsvReader;
import service.IMonitoringService;
import service.ITranslationService;
import service.MonitoringService;
import service.TranslationService;

@Configuration
public class ApplicationConfiguration {

	@Bean(name = "csvReader")
	public ICsvReader csvReader() {
		return new ConsumerCsvReader();
	}

	@Bean(initMethod = "init")
	public ICsvAnalyzer csvAnalyzer() {
		return new CsvAnalyzer();
	}

	@Bean(initMethod = "init", name = "translationService")
	public ITranslationService translationService() {
		return new TranslationService();
	}

	@Bean(name = "monitoringService")
	public IMonitoringService monitoringService() {
		return new MonitoringService();
	}

}
