package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Jacksonized
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CriterionMetadata {
    private final String name;
    private final String description;
}