package jvm.cot.javacotloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MethodsTestController {
    private static final Logger logger = LoggerFactory.getLogger(MethodsTestController.class);
    @GetMapping("/test")
    public String testMethod() {
        logger.info("Test Method called");
        return "test";
    }
}
