package com.study.cucumberRestApi.actions;

import com.study.cucumberRestApi.commons.AbstractActions;
import com.study.cucumberRestApi.dto.PetDto;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ValidatableResponse;

import java.io.File;
import java.util.List;

public class PetStoreActions extends AbstractActions {

    private static final String ADD_PET = "/pet";
    private static final String GET_PETS_BY_STATUS = "/pet/findByStatus";
    private static final String UPLOAD_IMAGE = "/pet/{petId}/uploadImage";
    private static final String DELETE_PET = "/pet/{petId}";

    public ValidatableResponse addNewPetRequest(Object pet) {
        return  abstractCommonRequest(BASE_URL, ADD_PET)
                .body(pet)
                .post()
                .then().log().all();
    }

    public ValidatableResponse addArrayPetRequest(List<PetDto> petList) {
        return  abstractCommonRequest(BASE_URL, ADD_PET)
                .body(petList)
                .post()
                .then();
    }

    public ValidatableResponse uploadImageRequest(File file, Long petId) {
        return abstractCommonRequest(BASE_URL, UPLOAD_IMAGE, file)
                .multiPart(file)
                .pathParam("petId", petId)
                .when()
                .post()
                .then();
    }

    public ValidatableResponse uploadImageRequestWIthJsonContentType(Long petId) {
        return abstractCommonRequest(BASE_URL, UPLOAD_IMAGE)
                .pathParam("petId", petId)
                .post()
                .then();
    }

    public ValidatableResponse uploadImageRequestWithoutFile(Long petId) {
        return abstractCommonRequestWithoutFile(BASE_URL, UPLOAD_IMAGE)
                .pathParam("petId", petId)
                .when()
                .post()
                .then();
    }

    public List<PetDto> getPetsByStatusRequest(List<String> statusList) {
        String commaSep = String.join(",", statusList);
        return abstractCommonRequest(BASE_URL, GET_PETS_BY_STATUS)
                .queryParam("status", commaSep)
                .get()
                .then().statusCode(200)
                .extract().as(new TypeRef<List<PetDto>>() {});
    }

    public ValidatableResponse deletePetRequest(String apiKey, Long petId) {
        return abstractCommonRequest(apiKey, BASE_URL, DELETE_PET)
                .pathParam("petId", petId)
                .when()
                .delete()
                .then().log().all();
    }

    public ValidatableResponse deleteWithoutAuthRequest(Long petId) {
        return abstractCommonRequest(BASE_URL, DELETE_PET)
                .pathParam("petId", petId)
                .when()
                .delete()
                .then();
    }
}
