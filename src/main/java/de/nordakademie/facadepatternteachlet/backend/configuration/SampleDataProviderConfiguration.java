package de.nordakademie.facadepatternteachlet.backend.configuration;

import de.nordakademie.facadepatternteachlet.backend.data.SampleDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The class <b>SampleDataProviderConfiguration</b> represents a configuration class to populate an instance of
 * SampleDataProvider to the Spring Boot application context. It therefore defines a Bean of type SampleDataProvider.
 *
 * @author Niklas Witzel
 */
@Configuration
public class SampleDataProviderConfiguration {
    @Bean
    public SampleDataProvider sampleDataProvider() {
        return new SampleDataProvider();
    }
}
