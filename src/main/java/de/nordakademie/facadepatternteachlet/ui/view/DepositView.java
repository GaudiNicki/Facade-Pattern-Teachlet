package de.nordakademie.facadepatternteachlet.ui.view;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.nordakademie.facadepatternteachlet.backend.exception.NoAmountGivenException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.facade.ATMFacade;
import de.nordakademie.facadepatternteachlet.ui.enums.Banknote;
import de.nordakademie.facadepatternteachlet.ui.layout.MainLayout;
import de.nordakademie.facadepatternteachlet.ui.model.TypedBanknoteStack;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class <b>DepositView</b> represents the UI of the ATM deposit functionality.
 * It contains input fields for the respective types of banknotes and a button to start the deposit.
 * A button to cancel the action and return to the main menu is also provided.
 *
 * @author Til Zöller
 */
@PageTitle("Deposit")
@Route(value = "deposit", layout = MainLayout.class)
public class DepositView extends AbstractView {

    private final IntegerField fiveHundredNoteField;
    private final IntegerField twoHundredNoteField;
    private final IntegerField oneHundredNoteField;
    private final IntegerField fiftyNoteField;
    private final IntegerField twentyNoteField;
    private final IntegerField tenNoteField;
    private final IntegerField fiveNoteField;

    private final VerticalLayout currencyFieldLayout;

    private final Button depositBtn;

    private final Map<IntegerField, Banknote> integerFieldToBanknoteMapping;


    public DepositView(@Autowired ATMFacade facade) {
        super(facade);

        H1 viewTitle = initViewTitle();

        this.integerFieldToBanknoteMapping = new HashMap<>();

        this.fiveHundredNoteField = this.initCurrencyField("500€", Banknote.FIVE_HUNDRED_EURO);
        this.twoHundredNoteField = this.initCurrencyField("200€", Banknote.TWO_HUNDRED_EURO);
        this.oneHundredNoteField = this.initCurrencyField("100€", Banknote.ONE_HUNDRED_EURO);
        this.fiftyNoteField = this.initCurrencyField("50€", Banknote.FIFTY_EURO);
        this.twentyNoteField = this.initCurrencyField("20€", Banknote.TWENTY_EURO);
        this.tenNoteField = this.initCurrencyField("10€", Banknote.TEN_EURO);
        this.fiveNoteField = this.initCurrencyField("5€", Banknote.FIVE_EURO);

        this.currencyFieldLayout = this.initCurrencyFieldLayout();

        this.depositBtn = this.initDepositButton();

        HorizontalLayout buttonLayout = this.initButtonLayout();

        this.initUI("30%", viewTitle, currencyFieldLayout, buttonLayout);
    }

    private H1 initViewTitle() {
        H1 h1 = new H1("Insert your bank notes!");
        h1.getStyle().set("text-align", "center");

        return h1;
    }

    private Button initDepositButton() {
        Button button = new Button("Deposit");
        button.setWidth("150px");

        button.addClickListener(buttonClickEvent -> {
            try {
                List<TypedBanknoteStack> typedBanknoteStacks = new ArrayList<>();
                this.currencyFieldLayout.getChildren().forEach(component -> {
                    IntegerField integerField = (IntegerField) component;

                    typedBanknoteStacks.add(
                            new TypedBanknoteStack(
                                    this.integerFieldToBanknoteMapping.get(integerField),
                                    integerField.getValue()
                            )
                    );
                });

                BigDecimal depositValue = facade.deposit(typedBanknoteStacks);

                Notification.show("Successfully deposited " + depositValue + " € to your bank account!");
                button.getUI().ifPresent(this::backToMenu);
            } catch (NoCustomerLoggedInException e) {
                log.error("No user logged in! Deposit not possible.");
                Notification.show("No user logged in! Deposit not possible.");
            } catch (NoAmountGivenException e) {
                log.error("The amount to be deposited may not be negative or zero.");
                Notification.show("The amount to be deposited may not be negative or zero.");
            }
        });

        return button;
    }

    private IntegerField initCurrencyField(String text, Banknote bankNote) {
        IntegerField integerField = new IntegerField(text);
        integerField.setValue(0);
        integerField.setHasControls(true);
        integerField.setMin(0);
        integerField.setMax(42);

        this.integerFieldToBanknoteMapping.put(integerField, bankNote);

        return integerField;
    }

    private VerticalLayout initCurrencyFieldLayout() {
        return new VerticalLayout(
                fiveHundredNoteField,
                twoHundredNoteField,
                oneHundredNoteField,
                fiftyNoteField,
                twentyNoteField,
                tenNoteField,
                fiveNoteField
        );
    }

    private HorizontalLayout initButtonLayout() {
        return new HorizontalLayout(
                depositBtn,
                cancelBtn
        );
    }
}
