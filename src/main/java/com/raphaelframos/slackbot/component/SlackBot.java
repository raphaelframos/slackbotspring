package com.raphaelframos.slackbot.component;

import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.raphaelframos.slackbot.model.BadWord;
import com.raphaelframos.slackbot.repository.BadWordRepository;

import me.ramswaroop.jbot.core.common.Controller;
import me.ramswaroop.jbot.core.common.EventType;
import me.ramswaroop.jbot.core.common.JBot;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;

@JBot
@Profile("slack")
public class SlackBot extends Bot{

	@Autowired
    BadWordRepository badwordRepository;

    private static final Logger logger = LoggerFactory.getLogger(SlackBot.class);

    @Value("${slackBotToken}")
    private String slackToken;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }
    
    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveDM(WebSocketSession session, Event event) {
        reply(session, event, new Message("Hi, I am " + slackService.getCurrentUser().getName()));
    }
    
    @Controller(events = EventType.MESSAGE, pattern = "fuck|shit|bitch")
    public void onReceiveMessage(WebSocketSession session, Event event, Matcher matcher) {
        if(!matcher.group(0).isEmpty()) {
            BadWord badword = new BadWord(event.getUserId(),matcher.group(0));
            badwordRepository.save(badword);
            Integer countBadWords = badwordRepository.countByUser(event.getUserId());
            if(countBadWords >= 5) {
                reply(session, event, new Message("Enough! You have too many say bad words. \nThe admin will kick you away from this channel."));
            } else {
                reply(session, event, new Message("Becareful you have say bad words "+countBadWords+" times"));
            }
        }
    }

}
