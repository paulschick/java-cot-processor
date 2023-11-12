package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.models.SimpleMovingAverage;
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

    public List<Map<String, Object>> getCotsWithSma(int period, String market) {
        String smaKey = "sma" + period;
        Collection<Cot> cotsCollection = cotRepository.retrieveByMarket(market);
        List<Cot> cots = new ArrayList<>(cotsCollection.stream().toList());
        List<Map<String, Object>> response = new ArrayList<>();
        logger.info("Retrieved " + cots.size() + " records for market " + market);
        for (Cot cot : cots) {
            Map<String, Object> cotMap = new HashMap<>();
            SimpleMovingAverage sma = smaRepository.findByCotIdAndPeriod(cot.getId(), period);
            cotMap.put("cot", cot);
            if (sma != null) {
                cotMap.put(smaKey, sma.getValue());
            } else {
                cotMap.put(smaKey, 0);
            }
            response.add(cotMap);
        }
        return response;
    }

    public void insertSmaForPeriodMarket(int period, String market) {
        try {
            Collection<Cot> cotsCollection = cotRepository.retrieveByMarket(market);
            List<Cot> cots = new ArrayList<>(cotsCollection.stream().toList());
            logger.info("Retrieved " + cots.size() + " records for market " + market);
            cots.sort(Comparator.comparing(Cot::getDate));
            List<Map<String, Object>> smaCots = calculateMovingAverage(cots, Cot::getOpenInterest, period);
            List<SimpleMovingAverage> smas = smaCots.stream().map(map -> {
                Object cotMap = map.get("cot");
                SimpleMovingAverage sma = new SimpleMovingAverage();
                sma.setCotId(((Cot) cotMap).getId());
                for (String key : map.keySet()) {
                    if (key.startsWith("sma")) {
                        sma.setValue(String.valueOf(map.get(key)));
                        sma.setPeriod(Integer.parseInt(key.substring(3)));
                    }
                }
                return sma;
            }).toList();
            logger.info("Writing " + smas.size() + " records for market " + market);
            smaRepository.saveAll(smas);
        } catch (Exception e) {
            logger.error("Error while processing market " + market, e);
            throw e;
        }
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
