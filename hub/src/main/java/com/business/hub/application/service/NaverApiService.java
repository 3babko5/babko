package com.business.hub.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class NaverApiService {

    private static final String CLIENT_ID = "gsvvhkp9nc"; // 네이버 클라이언트 ID
    private static final String CLIENT_SECRET = "tta5CdMXf29b4hz0Edsnav1gIb64pIwWODSCLpZx"; // 네이버 클라이언트 시크릿

    public double[] getCoordinates(String address) {
        try {
            // 주소 인코딩
            String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;


            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
            headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
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
