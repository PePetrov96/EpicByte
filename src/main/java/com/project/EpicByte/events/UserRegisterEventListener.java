package com.project.EpicByte.events;

import com.project.EpicByte.service.impl.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterEventListener implements ApplicationListener<UserRegisterEvent> {
    private final MailSenderService mailSenderService;

    @Autowired
    public UserRegisterEventListener(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Override
    public void onApplicationEvent(UserRegisterEvent event) {
        String subject = "Welcome " + event.getUsername();
        String message = "Hi " + event.getNames() +  ",\nEpicByte would like to welcome you!";
        this.mailSenderService.sendNewMail(event.getEmail(), subject, message);
    }
}
