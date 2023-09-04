package com.study.cucumberRestApi.stepDefs;

import com.study.cucumberRestApi.commons.HeadersBuilder;
import com.study.cucumberRestApi.actions.PetStoreActions;
import com.study.cucumberRestApi.commons.ResponseInformation;
import com.study.cucumberRestApi.actions.UserActions;

import com.study.cucumberRestApi.dto.PetDetailsFactory;
import com.study.cucumberRestApi.dto.PetDto;
import com.study.cucumberRestApi.dto.UserDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.study.cucumberRestApi.utils.ResourceFileLoader.getFileFromResources;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PetStoreSteps {

    private PetStoreActions petActions = new PetStoreActions();
    private UserActions userActions = new UserActions();

    private PetDto newPet;
    private PetDto petResponse;
    private ValidatableResponse validatableResponse;
    private ResponseInformation responseInfo;
    String filename = "photo-1472457897821-70d3819a0e24.jpeg";
    List<PetDto> petListResponse = new ArrayList<>();
    String apikey;

    private List<PetDto> petsList = new ArrayList<>();
    private Object invalidPetDetails;

    @Given("A new pet is generated")
    public void newPetIsGenerated() {
        newPet = PetDetailsFactory.getDefaultPet();
    }

    @Given("A new pet with invalid id type is generated")
    public void newPetWithStringIdISGenerated() {
        invalidPetDetails = PetDetailsFactory
                .getFromResources("pet", "pet-details-string-id.json", Object.class);
    }

    @When("PetStore service | POST | valid request to addPet endpoint")
    public void clientSendPOSTValidRequestToAddPet() {
        validatableResponse = petActions.addNewPetRequest(newPet);
    }
    @When("PetStore service | GET | request to addPet endpoint")
    public void clientSendGETRequestToAddPet() {
        validatableResponse = given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .headers(new HeadersBuilder().build())
                .accept("application/json")
                .contentType(ContentType.JSON)
                .baseUri("https://petstore.swagger.io/v2")
                .basePath("/pet")
                .body(newPet)
                .get()
                .then();
    }

    @When("PetStore service | POST | valid request to addPet endpoint with invalidId")
    public void clientSendPOSTValidRequestToAddPetWithInvalidRequest() {
        validatableResponse = petActions.addNewPetRequest(invalidPetDetails);
    }

    @Then("A new pet is added")
    public void verifyNewPetIsAdded() {
        petResponse = validatableResponse
                .statusCode(200)
                .extract()
                .as(PetDto.class);
        SoftAssertions.assertSoftly(a -> {
            a.assertThat(petResponse.getId()).isEqualTo(newPet.getId());
            a.assertThat(petResponse.getName()).isEqualToIgnoringCase(newPet.getName());
            a.assertThat(petResponse.getCategory()).isEqualTo(newPet.getCategory());
            a.assertThat(petResponse.getPhotoUrls()).isEqualTo(newPet.getPhotoUrls());
            a.assertThat(petResponse.getTags()).isEqualTo(newPet.getTags());
            a.assertThat(petResponse.getStatusName()).isEqualTo(newPet.getStatusName());
        });
    }

    @Then("Required fields are in json response")
    public void jsonSchemaContact() {
        validatableResponse.body(matchesJsonSchemaInClasspath("JsonSchemas/AddPetJsonSchema.json"));
    }

    @Then("A new empty pet is added")
    public void newEmptyPetIsAdded() {
        petResponse = validatableResponse.statusCode(200)
                .extract()
                .as(PetDto.class);
        SoftAssertions.assertSoftly(a -> {
            a.assertThat(petResponse.getId()).isNotNull();
            a.assertThat(petResponse.getName()).isNull();
            a.assertThat(petResponse.getTags()).isEmpty();
            a.assertThat(petResponse.getPhotoUrls()).isEmpty();
        });
    }

    @Given("A new empty pet is generated")
    public void newEmptyPetIsGenerated() {
        newPet = new PetDto();
    }

    @Given("A list of new pets are generated")
    public void listOfNewPetsAreGenerated() {
        petsList.add(PetDetailsFactory.getDefaultPet());
        petsList.add(PetDetailsFactory.getDefaultPet().setName("Second Default"));
    }

    @When("PetStore service | POST | invalid request to addPet endpoint")
    public void clientSendPOSTRequestArrayToPet() {
        validatableResponse = petActions.addArrayPetRequest(petsList);
    }

    @Then("Error {int} code is in response")
    public void errorCodeIsInResponse(int status) {
        validatableResponse.statusCode(status);
    }

    @Given("A new pet with non unique Id is generated")
    public void newPetWithNonUniqueIdIsGenerated() {
        newPet = new PetDto().setId(2l).setName("Mike");
        clientSendPOSTValidRequestToAddPet();
        validatableResponse.statusCode(200);
        newPet.setName("New name");
    }

    @Given("Known pet exists in the system")
    public void knownPetExistsInTheSystem() {
        newPet = new PetDto().setId(2L).setName("Mike");
        clientSendPOSTValidRequestToAddPet();
    }

    @When("PetStore service | POST | valid request to uploadImage endpoint")
    public void petstoreServicePOSTValidRequestToUploadImageEndpoint() throws IOException {
        File file = getFileFromResources(filename);
        validatableResponse = petActions.uploadImageRequest(file, newPet.getId());
        file.deleteOnExit();
    }

    @Then("Image is uploaded")
    public void imageIsUploaded() {
        ResponseInformation responseInfo = validatableResponse
                .statusCode(200)
                .extract().as(ResponseInformation.class);
        assertThat(responseInfo.getMessage()).as("Filename should be in message").contains(filename);
    }

    @When("PetStore service | POST | request with unsupported file to uploadImage endpoint")
    public void petstoreServicePOSTRequestWithUnsupportedFileToUploadImageEndpoint() throws IOException {
        String invalidFilename = "EmptyExcel.csv";
        File file = getFileFromResources(invalidFilename);
        validatableResponse = petActions.uploadImageRequest(file, newPet.getId());
        file.deleteOnExit();
    }

    @When("PetStore service | POST | valid request to uploadImage endpoint with invalid petId")
    public void petstoreServicePOSTValidRequestToUploadImageEndpointWithInvalidPetId() throws IOException {
        File file = getFileFromResources(filename);
        validatableResponse = petActions.uploadImageRequest(file,999999999999999998L);
        file.deleteOnExit();
    }

    @When("PetStore service | GET | request to findByStatus endpoint by the following statuses:")
    public void petstoreServiceGETRequestToFindByStatusEndpointByTheFollowingStatuses(List<String> statuses) {
        petListResponse = petActions.getPetsByStatusRequest(statuses);
    }

    @Then("All pets in response corresponded to the applied the following statuses:")
    public void allPetsInResponseCorrespondedToTheAppliedStatus(List<String> statuses) {
        List<String> uniqueStatus = petListResponse.stream().map(PetDto::getStatusName).distinct().collect(Collectors.toList());
       Assertions.assertThat(uniqueStatus).isSubsetOf(statuses);
    }

    @Then("Verify that empty array is in response")
    public void verifyThatEmptyArrayIsInResponse() {
        assertThat(petListResponse).as("Empty array should be return for 0 results")
                .isEqualTo(new ArrayList<String>());
    }

    @Given("An existing user is logged in to the system")
    public void existingUserIsLoggedInToTheSystem() {
        UserDto user = new UserDto().validUser();
        responseInfo = userActions.addNewUser(user);
        responseInfo = userActions.loginUser(user);
        assertThat(responseInfo.getType()).isEqualTo("unknown");
        assertThat(responseInfo.getMessage()).contains("logged in user session:");
        apikey = responseInfo.getMessage().split(":")[1];
    }

    @When("PetStore service | DELETE | request to deletePet endpoint with {word} petId")
    public void petstoreServiceDELETERequestToDeletePetEndpoint(String validity) {
        Long petId = "valid".equalsIgnoreCase(validity) ? newPet.getId() : 1111111116L;
        validatableResponse = petActions.deletePetRequest(apikey, petId);
    }

    @When("PetStore service | DELETE | request to deletePet endpoint without api_key")
    public void petstoreServiceDELETERequestToDeletePetEndpointWithoutAuth() {
        validatableResponse = petActions.deleteWithoutAuthRequest(newPet.getId());
    }

    @Then("The pet is deleted")
    public void petIsDeleted() {
        validatableResponse.statusCode(200);
    }

    @When("PetStore service | POST | request with JSON content-type to uploadImage endpoint")
    public void petstoreServicePOSTRequestJSONToUploadImageEndpoint() {
        validatableResponse = petActions.uploadImageRequestWIthJsonContentType(newPet.getId());
    }


    @When("PetStore service | POST | request without file to uploadImage endpoint")
    public void petstoreServicePOSTRequestWithoutFileToUploadImageEndpoint() {
        validatableResponse = petActions.uploadImageRequestWithoutFile(newPet.getId());
    }

    @Then("{} error message is in response")
    public void messageIsInResponse(String errorMessage) {
        responseInfo = validatableResponse.statusCode(400).extract().as(ResponseInformation.class);
        assertThat(responseInfo.getMessage()).contains(errorMessage);
    }

}
