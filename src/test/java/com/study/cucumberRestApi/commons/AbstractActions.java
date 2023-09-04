package com.study.cucumberRestApi.commons;

import com.study.cucumberRestApi.commons.HeadersBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.File;

import static io.restassured.RestAssured.given;

public abstract class AbstractActions {

    protected static final String BASE_URL = "https://petstore.swagger.io/v2";
    protected static RequestSpecification abstractCommonRequest(String apiKey, String uri, String path) {
        return given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .headers(new HeadersBuilder().addApiKey(apiKey).build())
                .accept("application/json")
                .contentType(ContentType.JSON)
                .baseUri(uri)
                .basePath(path)
                .request();
    }

    protected RequestSpecification abstractCommonRequest(String uri, String path) {
        return given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .headers(new HeadersBuilder().build())
                .accept("application/json")
                .contentType(ContentType.JSON)
                .baseUri(uri)
                .basePath(path)
                .request();
    }

    protected RequestSpecification abstractCommonRequest(String uri, String path, File file) {
        return given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .headers(new HeadersBuilder().build())
                .contentType(ContentType.MULTIPART)
                .multiPart(file)
                .baseUri(uri)
                .basePath(path)
                .request();
    }

    protected RequestSpecification abstractCommonRequestWithoutFile(String uri, String path) {
        return given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .headers(new HeadersBuilder().build())
                .contentType(ContentType.MULTIPART)
                .baseUri(uri)
                .basePath(path)
                .request();
    }
}
