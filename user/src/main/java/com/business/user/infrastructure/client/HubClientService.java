package com.business.user.infrastructure.client;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.application.exception.DeliveryDriverErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubClientService {

  private final HubClient hubClient;

  public void validateHubId(UUID hubId) {

    if (hubId == null || !hubClient.existsByHubId(hubId.toString())) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.HUB_NOT_FOUND);
    }
  }
}

