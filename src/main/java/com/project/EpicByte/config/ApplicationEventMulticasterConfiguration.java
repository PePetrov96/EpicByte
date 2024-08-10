package com.project.EpicByte.config;

import com.project.EpicByte.events.ChangeUsernameEventListener;
import com.project.EpicByte.events.UserRegisterEventListener;
import com.project.EpicByte.service.impl.MailSenderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class ApplicationEventMulticasterConfiguration {
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

    @Bean
    public UserRegisterEventListener myRegisterEventListener() {
        return new UserRegisterEventListener(new MailSenderService());
    }

    @Bean
    public ChangeUsernameEventListener myUpdateEventListener() {
        return new ChangeUsernameEventListener(new MailSenderService());
    }
}
