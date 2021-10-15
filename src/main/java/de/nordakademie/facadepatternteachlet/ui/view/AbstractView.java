package de.nordakademie.facadepatternteachlet.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.nordakademie.facadepatternteachlet.backend.facade.ATMFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * The class <b>AbstractView</b></b> serves as abstraction for all Views by providing implementation
 * for various UI Elements as well as initialising fields for the logger and facade.
 *
 * @author Anna Engelmann
 * @author Niklas Witzel
 */

public abstract class AbstractView extends VerticalLayout implements Serializable{

    private static final long serialVersionUID = 1456279676406658631L;

    protected final Logger log;
    protected final ATMFacade facade;

    protected final Button cancelBtn;

    protected AbstractView (@Autowired ATMFacade facade){
        this.log = LoggerFactory.getLogger(this.getClass());

        this.facade = facade;

        this.cancelBtn = initCancelButton();
    }

    protected Button initCancelButton() {
        Button button = new Button("Cancel");
        button.setWidth("150px");
        button.getStyle().set("background-color", "red");
        button.getStyle().set("color", "white");

        button.addClickListener(buttonClickEvent -> button.getUI().ifPresent(this::backToMenu));

        return button;
    }

    protected void backToMenu(UI ui) {
        ui.navigate("menu");
    }

    protected void initUI(String minWidth, Component... components) {
        setMinWidth(minWidth);
        setSizeUndefined();
        setMargin(true);
        setPadding(true);

        this.add(components);
    }
}
