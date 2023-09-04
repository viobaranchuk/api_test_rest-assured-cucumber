package com.study.cucumberRestApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PetStatus {
    AVAILABLE("available"),
    PENDING("pending"),
    INVALID_STATUS("invalid"),
    SOLD("sold");

    private final String name;
}
