package com.example.talkivo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Minimal in-memory rate limiter: N requests per IP per rolling minute on
 * /api/** routes. Good enough for a student/portfolio project running on a
 * single instance — swap for a shared store (Redis/bucket4j) if you ever
 * run more than one instance behind a load balancer.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 30;

    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (!request.getRequestURI().startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        String clientIp = request.getRemoteAddr();
        Window window = windows.computeIfAbsent(clientIp, ip -> new Window());

        if (window.isExpired()) {
            window.reset();
        }

        if (window.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"success\":false,\"status\":429,\"message\":\"Too many requests. Please slow down.\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private static class Window {
        volatile long windowStart = Instant.now().getEpochSecond();
        AtomicInteger count = new AtomicInteger(0);

        boolean isExpired() {
            return Instant.now().getEpochSecond() - windowStart >= 60;
        }

        void reset() {
            windowStart = Instant.now().getEpochSecond();
            count.set(0);
        }
    }
}
