package com.platform.device.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

@RestController
public class LoadBalancerController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    // Weighted distribution: weights for up to 3 replicas (50%, 30%, 20%)
    private static final int[] WEIGHTS = { 50, 30, 20 };

    @RequestMapping("/**")
    public ResponseEntity<?> proxyRequest(HttpServletRequest request, HttpEntity<byte[]> body) {
        try {
            // 1. Discover replicas
            List<String> replicas = discoverReplicas();

            if (replicas.isEmpty()) {
                return ResponseEntity.status(503).body("No replicas available for monitoring-microservice");
            }

            // 2. Select replica using WEIGHTED DISTRIBUTION
            String selectedReplicaIp = selectReplicaWeighted(replicas);

            // 3. Construct Target URL
            String requestUri = request.getRequestURI();
            String queryString = request.getQueryString();
            String targetUrl = "http://" + selectedReplicaIp + ":8080" + requestUri
                    + (queryString != null ? "?" + queryString : "");

            System.out.println(
                    "LoadBalancer forwarding " + requestUri + " to " + selectedReplicaIp + " (weighted selection)");

            // 4. Copy headers from original request
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    // Skip host header as it will be set to target
                    if (!"host".equalsIgnoreCase(headerName)) {
                        headers.addAll(headerName, Collections.list(request.getHeaders(headerName)));
                    }
                }
            }

            // Create new HttpEntity with both headers and body
            HttpEntity<byte[]> forwardEntity = new HttpEntity<>(body.getBody(), headers);

            // 5. Forward Request with headers
            return restTemplate.exchange(
                    new URI(targetUrl),
                    HttpMethod.valueOf(request.getMethod()),
                    forwardEntity,
                    byte[].class);

        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            // Pass through errors from the downstream service
            return ResponseEntity.status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Load Balancer Error: " + e.getMessage());
        }
    }

    /**
     * Selects a replica using weighted distribution.
     * Replica 1 gets 50% of traffic, Replica 2 gets 30%, Replica 3 gets 20%
     */
    private String selectReplicaWeighted(List<String> replicas) {
        if (replicas.size() == 1) {
            return replicas.get(0);
        }

        // Calculate total weight based on number of available replicas
        int totalWeight = 0;
        for (int i = 0; i < Math.min(replicas.size(), WEIGHTS.length); i++) {
            totalWeight += WEIGHTS[i];
        }

        // Generate random number between 0 and totalWeight
        int randomValue = random.nextInt(totalWeight);

        // Select replica based on weighted ranges
        int cumulativeWeight = 0;
        for (int i = 0; i < Math.min(replicas.size(), WEIGHTS.length); i++) {
            cumulativeWeight += WEIGHTS[i];
            if (randomValue < cumulativeWeight) {
                return replicas.get(i);
            }
        }

        // Fallback (should not reach here)
        return replicas.get(0);
    }

    private List<String> discoverReplicas() {
        List<String> ips = new ArrayList<>();
        try {
            // Swarm DNS for the service
            InetAddress[] addresses = InetAddress.getAllByName("tasks.monitoring-microservice");
            for (InetAddress address : addresses) {
                ips.add(address.getHostAddress());
            }
        } catch (Exception e) {
            System.err.println("Error discovering replicas: " + e.getMessage());
        }
        return ips;
    }
}
