package com.business.common.application.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverApiService {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    @Value("${naver.api.base-url}")
    private String baseUrl;

    @Value("${naver.api.geocode-api}")
    private String geocodeUrl;

    public double[] getCoordinates(String address) {
        try {
            // 주소 인코딩
            String url = baseUrl+ geocodeUrl + "?query=" + address;


            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
            headers.set("X-NCP-APIGW-API-KEY", clientSecret);
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "*/*");

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 로그 추가
            System.out.println("API 요청 URL: " + url);
            System.out.println("요청 헤더: " + headers.toString());
            System.out.println("응답 상태 코드: " + response.getStatusCode());
            System.out.println("응답 바디: " + response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            JsonNode addresses = root.path("addresses");
            if (addresses.isArray() && addresses.size() > 0) {
                JsonNode firstResult = addresses.get(0);
                double latitude = firstResult.path("y").asDouble();
                double longitude = firstResult.path("x").asDouble();
                return new double[]{latitude, longitude};
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("네이버 API 요청 중 오류 발생: " + e.getMessage());
        }
        throw new RuntimeException("네이버 API 요청 중 오류 발생: 유효한 좌표를 찾을 수 없습니다.");
    }
}