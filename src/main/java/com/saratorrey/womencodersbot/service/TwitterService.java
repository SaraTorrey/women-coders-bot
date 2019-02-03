package com.saratorrey.womencodersbot.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
public interface TwitterService {

    Integer numberCount(String string);

    @PostConstruct // Runs the bot when the server starts up
    void runBot();
}
