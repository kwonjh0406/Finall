package com.kwonjh0406.finall.test;

import com.kwonjh0406.finall.model.Exchange;
import com.kwonjh0406.finall.repository.ExchangeRepository;
import com.kwonjh0406.finall.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    ExchangeService exchangeService;

    @Autowired
    ExchangeRepository exchangeRepository;

    @GetMapping("/test/exchange")
    public Exchange exchange() {
        return exchangeRepository.findByTicker("USD").get();
    }
}
