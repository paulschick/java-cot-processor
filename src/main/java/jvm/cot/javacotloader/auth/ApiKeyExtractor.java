package jvm.cot.javacotloader.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApiKeyExtractor {
    private static final String API_KEY_HEADER = "X-App-Token";
    private static final String API_KEY_QUERY_PARAM = "api-key";
    private final String apiToken;

    public ApiKeyExtractor(@Value("${security.apiToken}") String apiToken) {
        this.apiToken = apiToken;
    }

    public Optional<Authentication> extract(HttpServletRequest request) {
        String providedKey = request.getHeader(API_KEY_HEADER);
        String queryKey = request.getParameter(API_KEY_QUERY_PARAM);
        if (apiToken == null ||
            (!apiToken.equals(providedKey) && !apiToken.equals(queryKey))) {
            return Optional.empty();

        }

        return Optional.of(new ApiKeyAuthentication(providedKey, AuthorityUtils.NO_AUTHORITIES));
    }
}
