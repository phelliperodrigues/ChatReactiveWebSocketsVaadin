package com.gmail.phellipe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = true)
@StyleSheet("frontend://styles/styles.css")
@Push
public class MainView extends VerticalLayout {

    private String username;
    private final UnicastProcessor<ChatMessage> puclicher;
    private final Flux<ChatMessage> messages;

    public MainView(UnicastProcessor<ChatMessage> puclicher, Flux<ChatMessage> messages){
        this.puclicher = puclicher;
        this.messages = messages;

        setClassName("main-view");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 header = new H1("Chat Flux Vaadin");
        header.getElement().getThemeList().add(Material.DARK);

        add(header);

        askUsername();

    }

    private void askUsername() {

        HorizontalLayout layout = new HorizontalLayout();

        TextField userNameField = new TextField();
        Button startChatButton = new Button("Iniciar Chat");

        layout.add(userNameField,startChatButton );

        startChatButton.addClickListener(click -> {
            username = userNameField.getValue();
            remove(layout);
            showChat();
        });

        add(layout);
    }

    private void showChat() {
        MessageList messageList = new MessageList();
        
        add(messageList, creatInputLayout());
        expand(messageList);


        messages.subscribe(message -> {
            getUI().ifPresent(ui -> ui.access(() ->
                messageList.add(
                    new Paragraph(message.getForm()+ ": " +
                        message.getMessage())
                )
            ));
        });
    }

    private Component creatInputLayout() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");

        TextField messageField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        layout.add(messageField,sendButton);

        expand(messageField);
        sendButton.addClickListener(click -> {
            puclicher.onNext(new ChatMessage(username, messageField.getValue()));
            messageField.clear();
            messageField.focus();
        });
        messageField.focus();

        return layout;
    }

}
