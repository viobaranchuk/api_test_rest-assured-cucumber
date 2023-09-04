package com.study.cucumberRestApi.commons;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseInformation {

    private Map<String, String> headers = new HashMap<>();
    private String message;
    private String type;
    private Integer code;
}
