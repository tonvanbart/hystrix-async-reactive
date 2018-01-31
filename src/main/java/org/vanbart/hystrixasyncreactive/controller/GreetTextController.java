package org.vanbart.hystrixasyncreactive.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalTime;

/**
 * This controller is the "remote service" returning a greeting text.
 */
@Controller
public class GreetTextController {

    private static final Logger log = LoggerFactory.getLogger(GreetTextController.class);

    @RequestMapping(path = "/greet-text")
    @ResponseBody
    public String getGreetText() {
        log.info("getGreetText()");
        try {
            log.info("waiting...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        String result;
        int hour = LocalTime.now().getHour();
        if (hour < 6) {
            result = "Good Night";
        } else if (hour < 12) {
            result = "Good Morning";
        } else if (hour < 18) {
            result = "Good Afternoon";
        } else {
            result = "Good Evening";
        }
        log.info("returning '{}'", result);
        return result;
    }
}
