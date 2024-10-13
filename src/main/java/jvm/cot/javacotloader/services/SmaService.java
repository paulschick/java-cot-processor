//package jvm.cot.javacotloader.services;
//
//import jvm.cot.javacotloader.mappers.PaginationMapper;
//import jvm.cot.javacotloader.models.*;
//import jvm.cot.javacotloader.models.entities.Cot;
//import jvm.cot.javacotloader.models.entities.SimpleMovingAverage;
//import jvm.cot.javacotloader.repositories.CotRepository;
//import jvm.cot.javacotloader.repositories.SmaRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.function.Function;
//
//@Service
//public class SmaService {
//    private static final Logger logger = LoggerFactory.getLogger(SmaService.class);
//    private final SmaRepository smaRepository;
//    private final CotRepository cotRepository;
//
//    @Autowired
//    public SmaService(SmaRepository smaRepository, CotRepository cotRepository) {
//        this.smaRepository = smaRepository;
//        this.cotRepository = cotRepository;
//    }
//
//    public CotPaginatedResponse getCotResponseWithAllSmas(String market, int page, int size, String sort) {
//        String baseSmaKey = "sma";
//        Pageable pagingSort = PaginationMapper.getPageRequest(page, size, sort);
//        Page<Cot> cotPage = cotRepository.retrieveByMarketPageable(market, pagingSort);
//        CotBuilder cotBuilder = new CotBuilder(cotPage).withNetValues(true);
//        CotPaginatedResponse cotResponse = cotBuilder.build();
//        for (CotResponse cot : cotResponse.getCots()) {
//            smaRepository.findByCotId(cot.getId())
//                    .forEach(sma -> {
//                        cot.getCalculatedFields().put(baseSmaKey + sma.getPeriod(), sma.getValue());
//                    });
//        }
//        return cotResponse;
//    }
//
//    public CotPaginatedResponse getCotResponseWithSma(int period, String market, int page, int size, String sort) {
//        String smaKey = "sma" + period;
//        Pageable pagingSort = PaginationMapper.getPageRequest(page, size, sort);
//        Page<Cot> cotPage = cotRepository.retrieveByMarketPageable(market, pagingSort);
//        CotBuilder cotBuilder = new CotBuilder(cotPage).withNetValues(true);
//        CotPaginatedResponse cotResponse = cotBuilder.build();
//        for (CotResponse cot : cotResponse.getCots()) {
//            SimpleMovingAverage sma = smaRepository.findByCotIdAndPeriod(cot.getId(), period);
//            if (sma != null) {
//                cot.getCalculatedFields().put(smaKey, sma.getValue());
//            } else {
//                cot.getCalculatedFields().put(smaKey, 0);
//            }
//        }
//        return cotResponse;
//    }
//
//    public void insertSmaForPeriodMarket(int period, String market) {
//        try {
//            Collection<Cot> cotsCollection = cotRepository.retrieveByMarket(market);
//            List<Cot> cots = new ArrayList<>(cotsCollection.stream().toList());
//            logger.info("Retrieved " + cots.size() + " records for market " + market);
//            cots.sort(Comparator.comparing(Cot::getDate));
//            List<Map<String, Object>> smaCots = calculateMovingAverage(cots, Cot::getOpenInterest, period);
//            List<SimpleMovingAverage> smas = smaCots.stream().map(map -> {
//                Object cotMap = map.get("cot");
//                SimpleMovingAverage sma = new SimpleMovingAverage();
//                sma.setCotId(((Cot) cotMap).getId());
//                for (String key : map.keySet()) {
//                    if (key.startsWith("sma")) {
//                        sma.setValue(String.valueOf(map.get(key)));
//                        sma.setPeriod(Integer.parseInt(key.substring(3)));
//                    }
//                }
//                return sma;
//            }).toList();
//            logger.info("Writing " + smas.size() + " records for market " + market);
//            smaRepository.saveAll(smas);
//        } catch (Exception e) {
//            logger.error("Error while processing market " + market, e);
//            throw e;
//        }
//    }
//
//    public List<Map<String, Object>> calculateMovingAverage(Collection<Cot> cots, Function<Cot, String> valueGetter, int period) {
//        List<Cot> sortedCots = new ArrayList<>(cots);
//        sortedCots.sort(Comparator.comparing(Cot::getDate));
//        List<Map<String, Object>> result = new ArrayList<>();
//        double sum = 0;
//        Queue<Integer> window = new LinkedList<>();
//
//        for (int i = 0; i < sortedCots.size(); i++) {
//            Cot currentCot = sortedCots.get(i);
//            int value = getIntValueFromCot(currentCot, valueGetter);
//            window.add(value);
//            sum += value;
//            Map<String, Object> cotWithSma = new HashMap<>();
//            cotWithSma.put("cot", currentCot);
//            if (i >= period) {
//                sum -= window.remove();
//                cotWithSma.put("sma" + period, sum / period);
//            } else {
//                cotWithSma.put("sma" + period, 0);
//            }
//            result.add(cotWithSma);
//        }
//        return result;
//    }
//
//    public int getIntValueFromCot(Cot cot, Function<Cot, String> valueGetter) {
//        String value = valueGetter.apply(cot);
//        if (value == null || value.isEmpty()) {
//            return 0;
//        }
//        return (int) Double.parseDouble(value);
//    }
//}
