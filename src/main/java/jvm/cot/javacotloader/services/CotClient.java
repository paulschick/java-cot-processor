package jvm.cot.javacotloader.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jvm.cot.javacotloader.models.CotEntityMapProvider;
import jvm.cot.javacotloader.models.response.CftcResponse;
import jvm.cot.javacotloader.repositories.CotJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CotClient {
    private static final Logger logger = LoggerFactory.getLogger(CotClient.class);
    private static final String BASE_URL = "https://publicreporting.cftc.gov/resource/6dca-aqww.json";
    public static final String DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FMT);

    private final String apiKey;
    private final RestClient restClient;
    private final ObjectMapper mapper;
    private final CotJpaRepository cotJpaRepository;
    private final CotEntityMapProvider cotEntityMapProvider;

    public CotClient(@Value("${cftc.appToken}") String apiKey,
                     RestClient restClient,
                     ObjectMapper mapper,
                     CotJpaRepository cotJpaRepository,
                     CotEntityMapProvider cotEntityMapProvider) {
        this.apiKey = apiKey;
        this.restClient = restClient;
        this.mapper = mapper;
        this.cotJpaRepository = cotJpaRepository;
        this.cotEntityMapProvider = cotEntityMapProvider;
    }

    public List<CftcResponse> getCotsAfterDate(int year, int month, int day) throws IllegalArgumentException {
        return get(buildUriAfterDate(year, month, day));
    }

    public List<CftcResponse> getAllCots() {
        var uri = URI.create(BASE_URL);
        return get(uri);
    }

    public URI buildUriAfterDate(int year, int month, int day) {
        var date = LocalDateTime.of(year, month, day, 0, 0);
        var fmtDate = date.format(FORMATTER);
        var whereClause = String.format("report_date_as_yyyy_mm_dd > '%s'", fmtDate);
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("$where", whereClause)
                .build()
                .toUri();
    }

    public String getURLAfterDate(int year, int month, int day) {
        return buildUriAfterDate(year, month, day).toString();
    }

    public int insertCftcResponse(List<CftcResponse> cftcResponses) {
        try {
            var cots = cftcResponses.stream()
                    .map(cotEntityMapProvider::map)
                    .filter(cot -> !cotJpaRepository.existsByMarketDate(cot.getMarketDate()))
                    .toList();

            cotJpaRepository.saveAll(cots);

            return cots.size();
        } catch (Exception e) {
            logger.error("error parsing CFTC response", e);
            logger.error("unable to save response to database");
            return 0;
        }
    }

    private List<CftcResponse> get(URI uri) throws RuntimeException {
        return restClient.get()
                .uri(uri)
                .headers(headers -> {
                    headers.set("Content-Type", "application/json");
                    headers.set("X-App-Token", apiKey);
                })
                .exchange((req, res) -> {
                    if (res.getStatusCode().is2xxSuccessful()) {
                        return mapper.readValue(res.getBody(), new TypeReference<>() {
                        });
                    }
                    logger.error("invalid response code: {}", res.getStatusCode());
                    var respBody = mapper.readValue(res.getBody(), String.class);
                    logger.error(respBody);
                    throw new RuntimeException("unable to parse response");
                });
    }
}
