package jvm.cot.javacotloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {
    private static final Logger logger = LoggerFactory.getLogger(CotDownloadController.class);

    private final CotDataRepository repository;

    @Autowired
    public DataController(CotDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/test-write")
    public String testWrite() {
        CotEntity cotEntity = new CotEntity();
        cotEntity.setDate("2021-01-01");
        cotEntity.setMarket("CORN");
        cotEntity.setOpenInterest("100");
        cotEntity.setNonCommLong("10");
        cotEntity.setNonCommShort("20");
        cotEntity.setCommLong("30");
        cotEntity.setCommShort("40");
        cotEntity.setNonReptLong("50");
        cotEntity.setNonReptShort("60");
        logger.info("Writing " + cotEntity);

        repository.save(cotEntity);

        return "Test write";
    }

    @GetMapping("/test-read")
    public String testRead() {
        return "Test read";
    }
}
