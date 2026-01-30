package org.example.perfproject.mock.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseDTO(
        UUID rqUid,
        UUID clientId,
        String account,
        Currency currency,
        BigDecimal balance,
        BigDecimal maxLimit
) {}
