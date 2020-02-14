package com.gmail.phellipe;

import com.vaadin.flow.component.dependency.StyleSheet;

import java.time.LocalDateTime;

public class ChatMessage {

    private String form;
    private LocalDateTime time;
    private String message;

    public ChatMessage(String form, String message) {
        this.form = form;
        this.message = message;
        this.time = LocalDateTime.now();
    }

    public String getForm() {
        return form;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
