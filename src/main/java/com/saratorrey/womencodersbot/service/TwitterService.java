package com.saratorrey.womencodersbot.service;

import javax.annotation.PostConstruct;

public interface TwitterService {

    @PostConstruct // Runs the bot when the server starts up
    void runBot();
}
