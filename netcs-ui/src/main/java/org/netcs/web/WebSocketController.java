package org.netcs.web;

import org.netcs.util.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    @MessageMapping("/hello")
    @SendTo("/topic/experiments")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(3000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

    @Autowired
    private MessageSendingOperations<String> messagingTemplate;

//    @Scheduled(fixedDelay = 1000)
//    public void sendQuotes() {
//        this.messagingTemplate.convertAndSend("/topic/experiments", new Greeting("Hello, there!"));
//    }


    class Greeting {

        private String content;

        public Greeting(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

    }

}