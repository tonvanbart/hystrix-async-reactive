package org.vanbart.hystrixasyncreactive.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This controller is the "remote service" called by {@link org.vanbart.hystrixasyncreactive.service.CapitalizeService}.
 */
@Controller
public class CapitalizeController {

    private static final Logger log = LoggerFactory.getLogger(CapitalizeController.class);

    @GetMapping(path = "cap")
    @ResponseBody
    public String capitalize(@RequestParam(name = "arg") String arg) {
        log.info("capitalize({})", arg);
        try {
            log.info("waiting...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }

        String result =  arg.substring(0,1).toUpperCase() + arg.substring(1).toLowerCase();
        log.info("returning '{}'", result);
        return result;
    }
}
