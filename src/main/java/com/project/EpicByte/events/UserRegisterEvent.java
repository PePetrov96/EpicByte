package com.project.EpicByte.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private final String username;
    private final String email;
    private final String names;

    public UserRegisterEvent(Object source,
                             String username,
                             String email,
                             String names) {
        super(source);
        this.username = username;
        this.email = email;
        this.names = names;
    }
}
