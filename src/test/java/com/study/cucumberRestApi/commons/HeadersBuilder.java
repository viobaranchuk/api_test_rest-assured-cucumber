package com.study.cucumberRestApi.commons;

import com.google.common.base.Strings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HeadersBuilder {

    static final String AUTHORIZATION = "Authorization";
    static final String CONTENT_TYPE = "Content-Type";
    static final String API_KEY = "api_key";

    private Map<String, String> headers = new HashMap<>();

    public HeadersBuilder add(String key, String value) {
        if (!Strings.isNullOrEmpty(key)) {
            headers.put(key, value);
        }
        return this;
    }

    public HeadersBuilder addApiKey(String apiKey) {
        add(API_KEY, apiKey);
        return this;
    }

    public Map<String, String> build() {
        return Collections.unmodifiableMap(headers);
    }
}
