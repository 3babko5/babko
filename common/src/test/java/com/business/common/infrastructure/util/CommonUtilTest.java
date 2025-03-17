package com.business.common.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommonUtilTest {

    @Test
    void testLDTToString() {
        LocalDateTime now = LocalDateTime.of(2024, 3, 15, 14, 30, 0);

        String formatted = CommonUtil.LDTToString(now);
        System.out.println("변환된 값: " + formatted);

        assertEquals("2024-03-15 14:30:00", formatted);
    }

    @Test
    void testLDTToString_NullValue() {
        LocalDateTime now = null;

        String formatted = CommonUtil.LDTToString(now);

        assertEquals("", formatted);
    }

    @Test
    void testStringToLDT() {
        String dateStr = "2024-03-15 14:30:00";
        System.out.println("입력값: " + dateStr);

        LocalDateTime parsed = CommonUtil.stringToLDT(dateStr);
        System.out.println("변환된 값: " + parsed);

        assertNotNull(parsed);
        assertEquals(2024, parsed.getYear());
        assertEquals(3, parsed.getMonthValue());
        assertEquals(15, parsed.getDayOfMonth());
        assertEquals(14, parsed.getHour());
        assertEquals(30, parsed.getMinute());
        assertEquals(0, parsed.getSecond());
    }

    @Test
    void testStringToLDT_InvalidFormat() {
        String invalidDateStr = "2024-03-15T14:30:00";
        System.out.println("테스트 시작: 잘못된 형식의 String → LocalDateTime 변환");
        System.out.println("입력값: " + invalidDateStr);

        assertThrows(Exception.class, () -> CommonUtil.stringToLDT(invalidDateStr));
    }

    @Test
    void testStringToLDT_NullOrEmpty() {
        String emptyString = "";
        String nullString = null;
        System.out.println("테스트 시작: Null 또는 빈 문자열 변환 확인");

        LocalDateTime result1 = CommonUtil.stringToLDT(emptyString);
        LocalDateTime result2 = CommonUtil.stringToLDT(nullString);

        System.out.println("빈 문자열 변환 결과: " + result1);
        System.out.println("Null 값 변환 결과: " + result2);

        assertNull(result1);
        assertNull(result2);
    }

    @Test
    void testCheckStringIsEmpty() {
        String emptyString = "";
        String nullString = null;
        String nonEmptyString = "Hello";
        System.out.println("테스트 시작: 문자열이 비어있는지 확인");

        assertTrue(CommonUtil.checkStringIsEmpty(emptyString));
        assertTrue(CommonUtil.checkStringIsEmpty(nullString));
        assertFalse(CommonUtil.checkStringIsEmpty(nonEmptyString));
    }

    @Test
    void testConvertJsonToDto_ValidJson() throws JsonProcessingException {
        String jsonString = "{\"name\":\"John\",\"age\":30}";
        System.out.println("테스트 시작: JSON → DTO 변환");
        System.out.println("입력 JSON: " + jsonString);

        TestDto result = CommonUtil.convertJsonToDto(jsonString, TestDto.class);

        System.out.println("변환된 DTO: name=" + result.getName() + ", age=" + result.getAge());

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    void testConvertJsonToDto_InvalidJson() {
        String invalidJsonString = "{invalid json}";
        System.out.println("테스트 시작: 잘못된 JSON 변환 테스트");
        System.out.println("입력 JSON: " + invalidJsonString);

        assertThrows(RuntimeException.class, () -> CommonUtil.convertJsonToDto(invalidJsonString, TestDto.class));
    }

    private static class TestDto {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

}
