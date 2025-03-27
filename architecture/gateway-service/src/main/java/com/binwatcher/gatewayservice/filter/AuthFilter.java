package com.binwatcher.gatewayservice.filter;

import com.binwatcher.gatewayservice.helper.JWTUtil;
import com.binwatcher.gatewayservice.helper.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final RouteValidator routeValidator;
    private final JWTUtil jwtUtil;
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    public AuthFilter(RouteValidator routeValidator, JWTUtil jwtUtil) {
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            LOG.info("Incoming request to path: {}", exchange.getRequest().getPath());
            if(routeValidator.isSecured.test(exchange.getRequest())){
                LOG.info("Verifying Authorization for this path...");
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    LOG.error("Authorization header not found.");
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header not found");
                }

                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (token.startsWith("Bearer ")) token = token.substring(7);

                if (!jwtUtil.validateToken(token)) {
                    LOG.error("Invalid token.");
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token");
                }

                if (!routeValidator.validateRoute(exchange.getRequest(), token)) {
                    LOG.error("User unauthorized for this route.");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized for this route");
                }
            }
            return chain.filter(exchange)
                .doOnTerminate(() -> {
                    LOG.info("Response for path: {} completed", exchange.getRequest().getPath());
                });
        });
    }

    public static class Config {
    }
}
