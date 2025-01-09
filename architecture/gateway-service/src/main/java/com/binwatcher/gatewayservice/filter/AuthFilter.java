package com.binwatcher.gatewayservice.filter;

import com.binwatcher.gatewayservice.helper.JWTUtil;
import com.binwatcher.gatewayservice.helper.RouteValidator;
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

    public AuthFilter(RouteValidator routeValidator, JWTUtil jwtUtil) {
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header not found");
                }

                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (token.startsWith("Bearer ")) token = token.substring(7);

                if (!jwtUtil.validateToken(token)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token");
                }

                if (!routeValidator.validateRoute(exchange.getRequest(), token)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized for this route");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
