package org.vanbart.hystrixasyncreactive.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Single;
import rx.schedulers.Schedulers;

import java.net.URI;

@Service
public class GreetTextService {

    private final RestTemplate restTemplate;

    private final URI uri = URI.create("http://localhost:8080/greet-text");

    private static final Logger log = LoggerFactory.getLogger(GreetTextService.class);

    @Autowired
    public GreetTextService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "fallback", commandKey = "greetText")
    public Single<String> getGreetText() {
        log.info("getGreetText()");
        return Single.fromCallable(() -> restTemplate.getForObject(uri, String.class)).subscribeOn(Schedulers.io());
//        return Single.fromCallable(() -> restTemplate.getForObject(uri, String.class)).observeOn(Schedulers.io());
    }

    public String fallback() {
        log.info("fallback()");
        return "Hello there";
    }
}
