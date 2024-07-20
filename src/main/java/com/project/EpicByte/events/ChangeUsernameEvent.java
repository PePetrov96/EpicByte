package com.project.EpicByte.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeUsernameEvent extends ApplicationEvent {
    private final String oldUsername;
    private final String newUsername;
    private final String email;
    private final String names;

    public ChangeUsernameEvent(Object source,
                             String oldUsername,
                             String newUsername,
                             String email,
                             String names) {
        super(source);
        this.oldUsername = oldUsername;
        this.newUsername = newUsername;
        this.email = email;
        this.names = names;
    }
}
