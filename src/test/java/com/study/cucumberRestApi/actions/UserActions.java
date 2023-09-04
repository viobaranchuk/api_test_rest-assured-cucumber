package com.study.cucumberRestApi.actions;

import com.study.cucumberRestApi.commons.AbstractActions;
import com.study.cucumberRestApi.commons.ResponseInformation;
import com.study.cucumberRestApi.dto.UserDto;

public class UserActions  extends AbstractActions {

    private static final String CREATE_USER = "/user";
    private static final String LOGIN_USER = "/user/login";

    public ResponseInformation addNewUser(UserDto user) {
        return abstractCommonRequest(BASE_URL, CREATE_USER)
                .body(user)
                .post()
                .then().statusCode(200)
                .extract().as(ResponseInformation.class);
    }

    public ResponseInformation loginUser(UserDto user) {
        return abstractCommonRequest(BASE_URL, LOGIN_USER)
                .queryParam("username", user.getUsername())
                .queryParam("password", user.getPassword())
                .get()
                .then()
                .extract().as(ResponseInformation.class);
    }

}
