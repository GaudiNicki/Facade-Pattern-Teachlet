package de.nordakademie.facadepatternteachlet.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.nordakademie.facadepatternteachlet.backend.exception.NoAmountGivenException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoValidReturnOfBanknotesPossibleException;
import de.nordakademie.facadepatternteachlet.backend.exception.NotEnoughMoneyInBankAccountException;
import de.nordakademie.facadepatternteachlet.backend.facade.ATMFacade;
import de.nordakademie.facadepatternteachlet.ui.enums.Banknote;
import de.nordakademie.facadepatternteachlet.ui.layout.MainLayout;
import de.nordakademie.facadepatternteachlet.ui.model.TypedBanknoteStack;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The class <b>WithdrawalView</b> represents the UI of the ATM deposit functionality.
 * It contains an input field for the amount of money to be withdrawn.
 * A button to cancel the action and return to the main menu is also provided.
 *
 * @author Merlin Ritsch
 */
@PageTitle("Withdrawal")
@Route(value = "withdraw", layout = MainLayout.class)
public class WithdrawalView extends AbstractView{

    private final IntegerField withdrawalAmountField;

    private final Label fiveHundredEuroLabel;
    private final Label twoHundredEuroLabel;
    private final Label oneHundredEuroLabel;
    private final Label fiftyEuroLabel;
    private final Label twentyEuroLabel;
    private final Label tenEuroLabel;
    private final Label fiveEuroLabel;

    private final Button withdrawBtn;

    private final Map<Banknote, Label> banknoteLabelMap;

    public WithdrawalView(@Autowired ATMFacade facade) {
        super(facade);

        H1 viewTitle = initViewTitle();

        this.withdrawalAmountField = this.initWithdrawalAmountField();

        this.banknoteLabelMap = new EnumMap<>(Banknote.class);

        this.fiveHundredEuroLabel = initCashOutputLabel(Banknote.FIVE_HUNDRED_EURO);
        this.twoHundredEuroLabel = initCashOutputLabel(Banknote.TWO_HUNDRED_EURO);
        this.oneHundredEuroLabel = initCashOutputLabel(Banknote.ONE_HUNDRED_EURO);
        this.fiftyEuroLabel = initCashOutputLabel(Banknote.FIFTY_EURO);
        this.twentyEuroLabel = initCashOutputLabel(Banknote.TWENTY_EURO);
        this.tenEuroLabel = initCashOutputLabel(Banknote.TEN_EURO);
        this.fiveEuroLabel = initCashOutputLabel(Banknote.FIVE_EURO);

        this.withdrawBtn = this.initWithdrawBtn();

        VerticalLayout withdrawFieldLayout = this.initWithdrawFieldLayout();
        HorizontalLayout buttonLayout = this.initButtonLayout();

        this.initUI("30%", viewTitle, withdrawFieldLayout, buttonLayout);
    }

    private H1 initViewTitle() {
        H1 h1 = new H1("Enter amount to withdraw money!");
        h1.getStyle().set("text-align", "center");

        return h1;
    }

    private Label initCashOutputLabel(Banknote banknoteType) {
        Label label = new Label("0x " + banknoteType.getValue() + "€");

        this.banknoteLabelMap.put(banknoteType, label);

        return label;
    }

    private Button initWithdrawBtn() {
        Button button = new Button("Withdraw");
        button.setWidth("150px");

        button.addClickListener(buttonClickEvent -> {
            try {

                Integer valueToWithdraw = this.withdrawalAmountField.getValue();

                List<TypedBanknoteStack> typedBanknoteStacks = facade.withdraw(BigDecimal.valueOf(valueToWithdraw));

                for (TypedBanknoteStack typedBanknoteStack : typedBanknoteStacks) {
                    Label banknoteLabel = this.banknoteLabelMap.get(typedBanknoteStack.getBanknote());
                    Banknote banknoteStackType = typedBanknoteStack.getBanknote();
                    banknoteLabel.setText(typedBanknoteStack.getCount() + "x " + banknoteStackType.getValue() + "€");
                }

                Notification.show("Successfully withdrawn " + valueToWithdraw + " € to customer!");

            } catch (NoCustomerLoggedInException e) {
                log.error("No user logged in! Deposit not possible.");
                Notification.show("No user logged in! Deposit not possible.");
            } catch (NoAmountGivenException e) {
                log.error("The amount to be deposited may not be negative or zero.");
                Notification.show("The amount to be deposited may not be negative or zero.");
            } catch (NotEnoughMoneyInBankAccountException e) {
                log.error("The requested amount to be withdrawn is more than the bank accounts balance.");
                Notification.show("The requested amount to be withdrawn is more than the bank accounts balance.");
            } catch (NoValidReturnOfBanknotesPossibleException e) {
                log.error("The requested amount cannot be withdrawn with banknotes.");
                Notification.show("The requested amount cannot be withdrawn with banknotes.");
            }
        });

        return button;
    }

    private IntegerField initWithdrawalAmountField() {
        IntegerField integerField = new IntegerField("Amount to withdraw");

        integerField.setWidth("200px");
        integerField.setPlaceholder("Enter Amount");
        integerField.setMin(5);
        integerField.setHasControls(true);
        integerField.setStep(5);

        return integerField;
    }

    private VerticalLayout initWithdrawFieldLayout() {
        return new VerticalLayout(
                withdrawalAmountField,
                fiveHundredEuroLabel,
                twoHundredEuroLabel,
                oneHundredEuroLabel,
                fiftyEuroLabel,
                twentyEuroLabel,
                tenEuroLabel,
                fiveEuroLabel);
    }

    private HorizontalLayout initButtonLayout() {
        return new HorizontalLayout(
                withdrawBtn,
                cancelBtn
        );
    }
}
