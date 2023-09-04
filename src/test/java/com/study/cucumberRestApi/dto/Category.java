package com.study.cucumberRestApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Category implements Jsonable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
}
