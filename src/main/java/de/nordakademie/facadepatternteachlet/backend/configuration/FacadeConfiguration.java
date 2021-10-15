package de.nordakademie.facadepatternteachlet.backend.configuration;

import de.nordakademie.facadepatternteachlet.backend.facade.ATMFacade;
import de.nordakademie.facadepatternteachlet.backend.facade.ATMFacadeImpl;
import de.nordakademie.facadepatternteachlet.backend.service.AuthenticationService;
import de.nordakademie.facadepatternteachlet.backend.service.BankAccountService;
import de.nordakademie.facadepatternteachlet.backend.service.CardService;
import de.nordakademie.facadepatternteachlet.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The class <b>FacadeConfiguration</b> represents a configuration class to populate an instance of the ATMFacade to the
 * Spring Boot application context. It therefore defines a Bean of type ATMFacadeImpl.
 *
 * @author Niklas Witzel
 * @author Til Zöller
 */
@Configuration
public class FacadeConfiguration {

    private final AuthenticationService authenticationService;
    private final CardService cardService;
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;

    public FacadeConfiguration(@Autowired AuthenticationService authenticationService,
                               @Autowired CardService cardService,
                               @Autowired TransactionService transactionService,
                               @Autowired BankAccountService bankAccountService) {
        this.authenticationService = authenticationService;
        this.cardService = cardService;
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
    }

    @Bean
    public ATMFacade atmFacade() {
        return new ATMFacadeImpl(authenticationService, cardService, transactionService, bankAccountService);
    }
}
