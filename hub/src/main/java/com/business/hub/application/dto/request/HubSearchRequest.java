package com.business.hub.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class HubSearchRequest {

    private String hubName;
    private String hubAddress;

}