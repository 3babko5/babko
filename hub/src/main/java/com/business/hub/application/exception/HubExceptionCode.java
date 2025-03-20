package com.business.hub.application.exception;

import com.business.common.application.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum HubExceptionCode implements ExceptionCode {
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "HUB-001", "요청한 허브를 찾을 수 없습니다."),
    DUPLICATE_HUB_NAME(HttpStatus.CONFLICT, "HUB-002", "중복된 허브 이름이 존재합니다."),
    HUB_MANAGER_NOT_ASSIGNED(HttpStatus.BAD_REQUEST, "HUB-003", "허브 관리자가 지정되지 않았습니다."),
    HUB_DELETE_RESTRICTED(HttpStatus.FORBIDDEN, "HUB-004", "해당 허브는 삭제할 수 없습니다."),
    INVALID_HUB_ADDRESS(HttpStatus.BAD_REQUEST, "HUB-005", "유효하지 않은 허브 주소입니다."),
    INVALID_HUB_NAME(HttpStatus.BAD_REQUEST, "HUB-006", "허브 이름을 입력해주세요."),
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "HUB-007", "존재하지 않는 허브 관리자 ID입니다."),
    DUPLICATE_HUB(HttpStatus.CONFLICT, "HUB-008", "이미 등록된 허브입니다.");

    private final HttpStatusCode status;
    private final String code;
    private final String message;
}
