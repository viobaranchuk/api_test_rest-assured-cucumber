package com.study.cucumberRestApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Tag implements Jsonable {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;

}
