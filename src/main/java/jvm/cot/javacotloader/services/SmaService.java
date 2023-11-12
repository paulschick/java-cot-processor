package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.repositories.CotRepository;
import jvm.cot.javacotloader.repositories.SmaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class SmaService {
    private static final Logger logger = LoggerFactory.getLogger(SmaService.class);
    private final SmaRepository smaRepository;
    private final CotRepository cotRepository;

    @Autowired
    public SmaService(SmaRepository smaRepository, CotRepository cotRepository) {
        this.smaRepository = smaRepository;
        this.cotRepository = cotRepository;
    }

    public void testInserts() {
        String market = "E-MINI S&P 500 - CHICAGO MERCANTILE EXCHANGE";
        Collection<Cot> cotsCollection = cotRepository.retrieveByMarket(market);
        List<Cot> cots = new java.util.ArrayList<>(cotsCollection.stream().toList());
        logger.info("Retrieved " + cots.size() + " records for market " + market);
        cots.sort(Comparator.comparing(Cot::getId));
        logger.info("First ID: " + cots.get(0).getId());
        logger.info("Last ID: " + cots.get(cots.size() - 1).getId());
    }

    public List<Map<String, Object>> calculateMovingAverage(Collection<Cot> cots, Function<Cot, String> valueGetter, int period) {
        List<Cot> sortedCots = new ArrayList<>(cots);
        sortedCots.sort(Comparator.comparing(Cot::getDate));
        List<Map<String, Object>> result = new ArrayList<>();
        double sum = 0;
        Queue<Integer> window = new LinkedList<>();

        for (int i = 0; i < sortedCots.size(); i++) {
            Cot currentCot = sortedCots.get(i);
            int value = getIntValueFromCot(currentCot, valueGetter);
            window.add(value);
            sum += value;
            Map<String, Object> cotWithSma = new HashMap<>();
            cotWithSma.put("cot", currentCot);
            if (i >= period) {
                sum -= window.remove();
                cotWithSma.put("sma" + period, sum / period);
            } else {
                cotWithSma.put("sma" + period, 0);
            }
            result.add(cotWithSma);
        }
        return result;
    }

    public int getIntValueFromCot(Cot cot, Function<Cot, String> valueGetter) {
        String value = valueGetter.apply(cot);
        if (value == null || value.isEmpty()) {
            return 0;
        }
        return (int) Double.parseDouble(value);
    }
}
