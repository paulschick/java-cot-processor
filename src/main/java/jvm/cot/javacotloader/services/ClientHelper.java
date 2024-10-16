package jvm.cot.javacotloader.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientHelper {
    private ClientHelper() {}

    public static String buildQuery(String... parameters) {
        if (parameters.length % 2 != 0) {
            throw new IllegalArgumentException("parameters must be in pairs of two: " + parameters.length);
        }
        return IntStream.range(0, parameters.length)
                .filter(index -> index % 2 == 0)
                .mapToObj(index ->
                        urlEncode(parameters[index]) +
                        "=" +
                        urlEncode(parameters[index + 1]))
                .collect(Collectors.joining("&"));
    }

    public static String urlEncode(String value) {
        return value == null ?
                "" :
                URLEncoder.encode(value.trim(), StandardCharsets.UTF_8);
    }
}
