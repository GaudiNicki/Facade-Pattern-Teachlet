package de.nordakademie.facadepatternteachlet;

import de.nordakademie.facadepatternteachlet.backend.data.SampleDataProvider;
import de.nordakademie.facadepatternteachlet.backend.repository.CardRepository;
import de.nordakademie.facadepatternteachlet.backend.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * This class <b>Application</b> is the entry point of this application. It provides the main method, which runs the
 * Spring Boot application and initializes the application's DB with sample data.
 *
 * @author Niklas Witzel
 */
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner createSampleData(
			final SampleDataProvider sampleDataProvider,
			final CardRepository cardRepository,
			final TransactionRepository transactionRepository
	) {
		return args -> {
			cardRepository.saveAll(sampleDataProvider.getSampleCards());
			transactionRepository.saveAll(sampleDataProvider.getSampleTransactions());
		};
	}

}
