package origrad.roundforest.csv.analyzer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import origrad.roundforest.csv.analyzer.configuration.ApplicationConfiguration;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

		ctx.scan("origrad.roundforest.csv.analyzer");
		ctx.register(ApplicationConfiguration.class);
		ctx.refresh();

	}

}
