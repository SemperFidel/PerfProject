package org.example.perfproject.mock.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record RequestDTO(
        @NotNull UUID rqUid,
        @NotNull  UUID clientId,
        @NotNull String account,
        @NotNull LocalDate openDate,
        @NotNull LocalDate closeDate
) {}