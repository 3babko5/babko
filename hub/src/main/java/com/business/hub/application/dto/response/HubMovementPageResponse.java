package com.business.hub.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class HubMovementPageResponse<T> {
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
    private final int size;
    private final List<T> data;

    public HubMovementPageResponse(Page<T> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber() + 1; // 0부터 시작하므로 +1
        this.size = page.getSize();
        this.data = page.getContent();
    }

    public static <T> HubMovementPageResponse<T> of(Page<T> page) {
        return new HubMovementPageResponse<>(page);
    }

}
