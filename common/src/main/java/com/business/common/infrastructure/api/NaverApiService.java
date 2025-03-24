package com.business.common.infrastructure.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Service
@ConditionalOnProperty(name = "naver.api.client-id", matchIfMissing = false)
public class NaverApiService {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    @Value("${naver.api.base-url}")
    private String baseUrl;

    @Value("${naver.api.geocode-api}")
    private String geocodeUrl;

    @Value("${naver.api.direction-api}")
    private String directionApi;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String sendRequest(String url) {
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public double[] getCoordinates(String address) {
        String url = baseUrl + geocodeUrl + "?query=" + address;
        String jsonResponse = sendRequest(url);
        JsonNode root = parseJsonResponse(jsonResponse);

        JsonNode addresses = root.path("addresses");
        if (addresses.isArray() && !addresses.isEmpty()) {
            JsonNode firstResult = addresses.get(0);
            return new double[]{
                    firstResult.path("y").asDouble(),
                    firstResult.path("x").asDouble()
            };
        }
        throw new RuntimeException("유효한 좌표를 찾을 수 없습니다.");
    }

    /** 최적 경로 조회 */

    public String getOptimalRoute(double startLat, double startLon, double goalLat, double goalLon) {
        log.info("start: {}, {}, goal: {}, {}", startLat, startLon, goalLat, goalLon);
        String url = baseUrl + directionApi + "?start=" + startLon + "," + startLat + "&goal=" + goalLon + "," + goalLat;
        return sendRequest(url);
    }

    private JsonNode parseJsonResponse(String jsonResponse) {
        try {
            return objectMapper.readTree(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 오류: " + e.getMessage(), e);
        }
    }

    public BigDecimal extractDistanceFromJson(String jsonResponse) {
        JsonNode root = parseJsonResponse(jsonResponse);
        JsonNode traoptimalArray = root.path("route").path("traoptimal");

        if (!traoptimalArray.isArray() || traoptimalArray.isEmpty()) {
            throw new RuntimeException("경로 데이터를 찾을 수 없습니다.");
        }

        JsonNode summaryNode = parseJsonResponse(jsonResponse)
                .path("route").path("traoptimal").get(0).path("summary");

        return BigDecimal.valueOf(summaryNode.path("distance").asDouble());
    }

    public int extractDurationFromJson(String jsonResponse) {
        JsonNode root = parseJsonResponse(jsonResponse);
        JsonNode traoptimalArray = root.path("route").path("traoptimal");

        if (!traoptimalArray.isArray() || traoptimalArray.isEmpty()) {
            throw new RuntimeException("경로 데이터를 찾을 수 없습니다.");
        }

        JsonNode summaryNode = parseJsonResponse(jsonResponse)
                .path("route").path("traoptimal").get(0).path("summary");

        return summaryNode.path("duration").asInt() / 1000;
    }
}