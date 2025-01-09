package com.binwatcher.gatewayservice.helper;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private final JWTUtil jwtUtil;

    public RouteValidator(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    private static final List<String> openUri = List.of(
            "/auth/register",
            "/auth/login"
    );

    private final Map<String, List<String>> routeRoleMap = new HashMap<>() {{
        put("/admin", List.of("ADMIN"));
        put("/driver", List.of("ADMIN", "DRIVER"));
        put("/test/admin", List.of("ADMIN"));
    }};

    public boolean validateRoute(ServerHttpRequest request, String token) {
        List<String> roles = (List<String>) jwtUtil.getRoles(token);
        List<String> allowedRoutes =  getRolesForPath(request.getURI().getPath());
        return allowedRoutes == null || !Collections.disjoint(roles, allowedRoutes);
    }

    private List<String> getRolesForPath(String path) {
        for (Map.Entry<String, List<String>> entry : routeRoleMap.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Predicate<ServerHttpRequest> isSecured = request ->
            openUri.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
