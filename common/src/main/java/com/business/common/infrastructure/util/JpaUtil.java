package com.business.common.infrastructure.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class JpaUtil {

    /**
     * 페이징 객체를 반환
     * @param page : 요청 페이지 (1부터 시작)
     * @param size : 한페이지에 노출될 엘리먼트 수 (10, 30, 50 이외는 모두 10으로 처리)
     * @param orderby : 정렬 기준 엔티티의 필드 이름 (예: createdAt, updatedAt)
     * @param sort : 정렬 순서(DESC, ASC)
     * @return PageRequest
     */
    public static PageRequest getNormalPageable(Integer page, Integer size, String orderby, String sort) {

        Sort sortAndOrderBy = Sort.by(OrderBy.from(orderby).fieldName);
        sort = sort.toUpperCase();
        sortAndOrderBy = "ASC".equals(sort) ? sortAndOrderBy.ascending() : sortAndOrderBy.descending();

        return PageRequest.of(page - 1, getUsableSize(size), sortAndOrderBy);
    }

    private static int getUsableSize(int size) {
        return switch (size) {
            case 30 -> 30;
            case 50 -> 50;
            default -> 10;
        };
    }

    @Getter
    @AllArgsConstructor
    public enum OrderBy {
        CREATED("createdAt"),
        UPDATED("updatedAt");

        private final String fieldName;

        public static OrderBy from(String value) {
            for (OrderBy type : OrderBy.values()) {
                if (type.name().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("orderBy는 CREATED 또는 UPDATED 여야 합니다.");
        }
    }
}
