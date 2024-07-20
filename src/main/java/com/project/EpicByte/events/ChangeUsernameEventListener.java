package com.project.EpicByte.events;

import com.project.EpicByte.service.impl.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ChangeUsernameEventListener implements ApplicationListener<ChangeUsernameEvent> {
    private final MailSenderService mailSenderService;

    @Autowired
    public ChangeUsernameEventListener(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Override
    public void onApplicationEvent(ChangeUsernameEvent event) {
        String subject = "Warning " + event.getOldUsername();
        String message = "Hi " + event.getNames() +  ",\nYou successfully changed your username to: " + event.getNewUsername();
        this.mailSenderService.sendNewMail(event.getEmail(), subject, message);
    }
}
