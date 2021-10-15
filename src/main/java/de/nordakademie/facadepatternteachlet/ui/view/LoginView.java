package de.nordakademie.facadepatternteachlet.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import de.nordakademie.facadepatternteachlet.backend.exception.CardExpiredException;
import de.nordakademie.facadepatternteachlet.backend.exception.CustomerAlreadyLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCardSelectedException;
import de.nordakademie.facadepatternteachlet.backend.exception.WrongPinException;
import de.nordakademie.facadepatternteachlet.backend.facade.ATMFacade;
import de.nordakademie.facadepatternteachlet.ui.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;

/**
 * The class <b>LoginView</b> represents the UI entry point of the application. It provides a login screen where
 * the customer must select a card and enter the correct PIN to authenticate himself to the application.
 *
 * @author Niklas Witzel
 */
@PageTitle("Login")
@Route(value = "/", layout = MainLayout.class)
public class LoginView extends AbstractView {

    private final Label cardErrorLabel;
    private final ComboBox<Card> cardComboBox;
    private final Label pinErrorLabel;
    private final TextField pinTextField;

    public LoginView(@Autowired ATMFacade facade) {
        super(facade);

        H1 header = new H1("Login to your bank account");
        this.cardErrorLabel = initErrorLabel();
        this.cardComboBox = initCardComboBox();
        this.pinErrorLabel = initErrorLabel();
        this.pinTextField = initPinTextField();
        Button loginButton = initLoginButton();

        initUI("30%", header, cardComboBox, pinTextField, loginButton);
    }

    private ComboBox<Card> initCardComboBox() {
        ComboBox<Card> comboBox = new ComboBox<>("Card");
        comboBox.setWidth("300px");
        comboBox.setPlaceholder("Please select a card");
        comboBox.setItems(super.facade.getAllCards());

        comboBox.setItemLabelGenerator(card ->
                card.getAccount().getCustomerName()
                        + ", Nr. "
                        + card.getCardNumber()
                        + ", valid until: "
                        + card.getValidUntil().toString()
        );

        comboBox.addValueChangeListener(event -> comboBox.setHelperComponent(null));

        return comboBox;
    }

    private Label initErrorLabel() {
        Label label = new Label();
        label.getStyle().set("color", "red");

        return label;
    }

    private TextField initPinTextField() {
        TextField textField = new TextField("PIN");
        textField.setWidth("200px");
        textField.setPlaceholder("Enter your PIN");
        textField.setMaxLength(4);
        textField.setPattern("^[0-9]{4}$");
        textField.setErrorMessage("Incorrect PIN format! PIN must consist of exactly four digits.");

        textField.addValueChangeListener(event -> textField.setHelperComponent(null));

        return textField;
    }

    private Button initLoginButton() {
        Button button = new Button("Login");

        button.addClickListener(buttonClickEvent -> {
            Card selectedCard = cardComboBox.getValue();
            String enteredPin = pinTextField.getValue();

            cardComboBox.setHelperComponent(null);
            pinTextField.setHelperComponent(null);

            super.log.info(MessageFormat.format("Attempt login with selected card {0} and entered pin {1}", selectedCard, enteredPin));

            try {
                super.facade.login(selectedCard, enteredPin);
                button.getUI().ifPresent(ui -> ui.navigate("menu"));
            }
            catch (NoCardSelectedException e) {
                cardErrorLabel.setText("No card selected!");
                cardComboBox.setHelperComponent(cardErrorLabel);
            }
            catch (WrongPinException e) {
                pinErrorLabel.setText("Wrong PIN entered!");
                pinTextField.setHelperComponent(pinErrorLabel);
            }
            catch (CustomerAlreadyLoggedInException e) {
                pinErrorLabel.setText("There is already a customer authenticated. Please log out first");
                pinTextField.setHelperComponent(pinErrorLabel);
            }
            catch (CardExpiredException e) {
                cardErrorLabel.setText("This card is expired. Please use another one!");
                cardComboBox.setHelperComponent(cardErrorLabel);
            }
        });

        return button;
    }

}
