package de.nordakademie.facadepatternteachlet.ui.layout;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class <b>MainLayout</b> provides a basic layout for all views used in the application. It provides a header with
 * a title and a view container to render the views correspondent to the current route.
 *
 * @author Niklas Witzel
 */
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0")
public class MainLayout extends FlexLayout implements RouterLayout {

    private static final Logger log = LoggerFactory.getLogger(MainLayout.class);

    private final FlexLayout headerContainer;
    private final FlexLayout viewContainer;

    public MainLayout() {
        initErrorHandler();

        this.headerContainer = initHeaderContainer();
        this.viewContainer = initViewContainer();

        initUI();
    }

    private void initErrorHandler() {
        VaadinSession.getCurrent()
                .setErrorHandler((ErrorHandler) errorEvent -> {
                    log.error("Uncaught UI exception", errorEvent.getThrowable());
                    Notification.show("We are sorry, but an internal error occurred");
                });
    }

    private FlexLayout initHeaderContainer() {
        H2 pageTitle = new H2("ATM Simulation");
        pageTitle.getStyle().set("color", "white");
        pageTitle.getStyle().set("margin", "0");
        pageTitle.getStyle().set("padding-left", "2rem");

        FlexLayout flexLayout = new FlexLayout(pageTitle);
        flexLayout.setWidthFull();
        flexLayout.setAlignItems(Alignment.CENTER);
        flexLayout.getStyle().set("background-color", "#355172");

        return flexLayout;
    }

    private FlexLayout initViewContainer() {
        FlexLayout flexLayout = new FlexLayout();
        flexLayout.getStyle().set("overflow", "hidden");
        flexLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        flexLayout.setAlignItems(Alignment.CENTER);

        return flexLayout;
    }

    private void initUI() {
        setFlexDirection(FlexDirection.COLUMN);
        setSizeFull();

        add(headerContainer);
        add(viewContainer);

        setFlexGrow(.05, headerContainer);
        setFlexGrow(.95, viewContainer);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        this.viewContainer.getElement().appendChild(content.getElement());
    }
}
