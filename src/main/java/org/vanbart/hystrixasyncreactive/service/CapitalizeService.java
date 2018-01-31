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
public class CapitalizeService {

    private final RestTemplate restTemplate;

    private final URI uri = URI.create("http://localhost:8080/cap?arg=");

    private static final Logger log = LoggerFactory.getLogger(CapitalizeService.class);

    @Autowired
    public CapitalizeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "fallback", commandKey = "capitalize")
    public Single<String> capitalize(String arg) {
        log.info("capitalize({})", arg);
        URI requri = URI.create(uri + arg);
//        return Single.fromCallable(() -> restTemplate.getForObject(requri, String.class)).observeOn(Schedulers.io());
        return Single.fromCallable(() -> restTemplate.getForObject(requri, String.class)).subscribeOn(Schedulers.io());
    }

    public String fallback(String arg) {
        log.info("fallback({})", arg);
        return arg.toUpperCase();
    }
}
