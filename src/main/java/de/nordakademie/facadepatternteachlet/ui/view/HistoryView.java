package de.nordakademie.facadepatternteachlet.ui.view;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.nordakademie.facadepatternteachlet.backend.entity.Transaction;
import de.nordakademie.facadepatternteachlet.backend.entity.Withdrawal;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.facade.ATMFacade;
import de.nordakademie.facadepatternteachlet.ui.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The class <b>HistoryView</b> represents the UI of the bank account history.
 * It contains a label with  the current account balance and a table with all bank account transactions.
 *
 * @author Til Zöller
 */
@PageTitle("History")
@Route(value = "history", layout = MainLayout.class)
public class HistoryView extends AbstractView{

    public HistoryView(@Autowired ATMFacade facade) {
        super(facade);

        H1 viewTitle = this.initViewTitle();

        Label currentBalance = this.initCurrentBalanceLabel();
        Grid<Transaction> transactionGrid = this.initTransactionsGrid();

        this.initUI("60%", viewTitle, currentBalance, transactionGrid, cancelBtn);
    }

    private H1 initViewTitle() {
        H1 h1 = new H1("Your account history!");
        h1.getStyle().set("text-align", "center");

        return h1;
    }

    private Label initCurrentBalanceLabel() {
        Label label = new Label();

        try {
            label.setText("Current account balance: " + this.facade.getCurrentBalance() + "€");
        } catch (NoCustomerLoggedInException e) {
            label.setText("Current account balance: -");

            log.error("No user logged in. Account balance not available!");
            Notification.show("No user logged in. Account balance not available!");
        }
        return label;
    }

    private Grid<Transaction> initTransactionsGrid() {
        Grid<Transaction> grid = new Grid<>(Transaction.class);

        grid.removeAllColumns();

        grid.addColumn(transaction -> {
            if (transaction instanceof Withdrawal) {
                return "-" + transaction.getAmount() + "€";
            } else {
                return transaction.getAmount() + "€";
            }
        }).setHeader("Amount").setWidth("20%").setSortable(true);
        grid.addColumn(Transaction::getDateTime).setHeader("Date and Time").setWidth("30%");
        grid.addColumn(Transaction::getReasonForPayment).setHeader("Reason").setWidth("50%").setSortable(false);

        grid.sort(List.of(
                new GridSortOrder<>(grid.getColumns().get(1), SortDirection.DESCENDING)
        ));

        try {
            grid.setItems(this.facade.getTransactions());
        } catch (NoCustomerLoggedInException e) {
            log.error("No user logged in. Transactions not available!");
            Notification.show("No user logged in. Transactions not available!");
        }

        return grid;
    }
}

