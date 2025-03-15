package com.business.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CommonUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String LDTToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.format(formatter);
    }

    public static LocalDateTime stringToLDT (String ldtString) {
        if(Objects.isNull(ldtString) || ldtString.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(ldtString, formatter);
    }

    public static boolean checkStringIsEmpty (String target) {
        return target == null || target.isEmpty();
    }

    public static <T> T convertJsonToDto(String jsonString, Class<T> classType) {
        try {
            return objectMapper.readValue(jsonString, classType);
        } catch (Exception e) {
            throw new RuntimeException("Json 변환 실패");
        }
    }
}
