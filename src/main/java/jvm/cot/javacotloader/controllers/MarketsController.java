package jvm.cot.javacotloader.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("markets")
public class MarketsController {
    private static final Logger logger = LoggerFactory.getLogger(MarketsController.class);
}
