package com.gie.fleet.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Limitation du débit des tentatives de connexion par IP, en complément du blocage
 * de compte par téléphone géré dans AuthService. Fenêtre glissante simple en mémoire
 * (suffisant pour un déploiement mono-instance sur un seul VPS).
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final int maxRequests;
    private final long windowSeconds;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimitingFilter(@Value("${app.security.rate-limit.max-requests}") int maxRequests,
                               @Value("${app.security.rate-limit.window-seconds}") long windowSeconds) {
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        if (isLoginRequest(request)) {
            String key = clientIp(request);
            Bucket bucket = buckets.computeIfAbsent(key, k -> new Bucket());
            if (!bucket.tryConsume(maxRequests, windowSeconds)) {
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Trop de tentatives, réessayez plus tard.\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
                && request.getRequestURI().endsWith("/api/v1/auth/login");
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
    }

    private static class Bucket {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile Instant windowStart = Instant.now();

        synchronized boolean tryConsume(int max, long windowSeconds) {
            Instant now = Instant.now();
            if (now.isAfter(windowStart.plusSeconds(windowSeconds))) {
                windowStart = now;
                count.set(0);
            }
            return count.incrementAndGet() <= max;
        }
    }
}
