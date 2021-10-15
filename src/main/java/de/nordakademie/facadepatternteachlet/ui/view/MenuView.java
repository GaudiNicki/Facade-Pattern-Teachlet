package de.nordakademie.facadepatternteachlet.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.facade.ATMFacade;
import de.nordakademie.facadepatternteachlet.ui.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The class <b>MenuView</b></b> represents the UI navigation point of the application. It provides different buttons
 * which navigate the customer to the different views of this application.
 *
 *   @author Anna Engelmann
 */
@PageTitle("Menu")
@Route(value = "menu", layout = MainLayout.class)
public class MenuView extends AbstractView {

    private final Button btnWithdraw;
    private final Button btnDeposit;
    private final Button btnHistory;
    private final Button btnLogout;

    public MenuView(@Autowired ATMFacade facade) {
        super(facade);

        H1 header = new H1("Choose action!");
        header.getStyle().set("text-align", "center");

        this.btnWithdraw = initButton("Withdraw", "withdraw");
        this.btnHistory = initButton("History", "history");
        this.btnDeposit = initButton("Deposit", "deposit");
        this.btnLogout = initLogoutButton();

        FlexLayout topButtonRow = initTopButtonRow();
        FlexLayout bottomButtonRow = initBottomButtonRow();

        initUI("30%", header, topButtonRow, bottomButtonRow);
    }

    private FlexLayout initTopButtonRow(){
        FlexLayout flexLayout = new FlexLayout(btnWithdraw, btnHistory);
        flexLayout.setWidthFull();
        flexLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        return flexLayout;
    }

    private FlexLayout initBottomButtonRow(){
        FlexLayout flexLayout = new FlexLayout(btnDeposit, btnLogout);
        flexLayout.setWidthFull();
        flexLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);


        return flexLayout;
    }

    private Button initButton(String text, String route) {
        Button button = new Button(text);
        button.setWidth("150px");
        button.getStyle().set("color", "black");

        button.addClickListener(buttonClickEvent -> button.getUI().ifPresent(ui -> ui.navigate(route)));

        return button;
    }


    private Button initLogoutButton() {
        Button button = new Button("Logout");
        button.setWidth("150px");
        button.getStyle().set("background-color", "red");
        button.getStyle().set("color", "white");

        button.addClickListener(buttonClickEvent -> {
            try {
                facade.logout();
                button.getUI().ifPresent(ui -> ui.navigate(""));
            } catch (NoCustomerLoggedInException e) {
                log.error("No User found to log out!");
                Notification.show("No User found to log out!");
            }
        });

        return button;
    }
}
