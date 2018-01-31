package org.vanbart.hystrixasyncreactive.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vanbart.hystrixasyncreactive.service.CapitalizeService;
import org.vanbart.hystrixasyncreactive.service.GreetTextService;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@Controller
public class HelloController {

    private final CapitalizeService capitalizeService;

    private final GreetTextService greetTextService;

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    public HelloController(CapitalizeService capitalizeService, GreetTextService greetTextService) {
        this.capitalizeService = capitalizeService;
        this.greetTextService = greetTextService;
    }

    @GetMapping(path = "hello")
    @ResponseBody
    public String greeting(@RequestParam(name = "name", defaultValue = "world") String name) {
        log.info("greeting({})", name);
        long start = System.currentTimeMillis();
        final String[] results = new String[2];

        // these do not work anymore since the services operate on a different thread and did not complete yet
//        greetTextService.getGreetText().subscribe(greeting -> results[0] = greeting);
//        capitalizeService.capitalize(name).subscribe(capitalized -> results[1] = capitalized);

        results[0] = greetTextService.getGreetText().toBlocking().value();
        results[1] = capitalizeService.capitalize(name).toBlocking().value();

        String result = results[0] + ", " + results[1];
        log.info("return '{}' in {} ms", result, System.currentTimeMillis() - start);
        return result;
    }

    @GetMapping(path = "hello-para")
    @ResponseBody
    public String helloParallel(@RequestParam(name = "name", defaultValue = "world") String name) {
        log.info("helloParallel({})", name);
        long start = System.currentTimeMillis();
        Single<String> greetText = greetTextService.getGreetText();
        Single<String> capitalize = capitalizeService.capitalize(name);

        String[] answer = new String[1];

        String value = Single.zip(greetText, capitalize, (text, cap) -> text + ", " + cap)
                .toBlocking()
                .value();

        log.info("return '{}' in {} ms", value, System.currentTimeMillis() - start);
        return value;
    }
}
