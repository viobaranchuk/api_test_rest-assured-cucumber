Feature: PetStore API tests

  @AddPet @Smoke
  Scenario: A valid pet can be successfully added
    Given A new pet is generated
    # status parameter should be enum, any string can be status value
    When  PetStore service | POST | valid request to addPet endpoint
    Then  A new pet is added
    Then  Required fields are in json response

  @AddPet
  Scenario: An empty pet can be successfully added
    Given A new empty pet is generated
    When  PetStore service | POST | valid request to addPet endpoint
    Then  A new empty pet is added

  @AddPet @Negative
  Scenario: A pet with invalid id type cannot be added
    Given A new pet with invalid id type is generated
    When  PetStore service | POST | valid request to addPet endpoint with invalidId
    Then  Error 500 code is in response

  @AddPet @Negative
  Scenario: An array of pets cannot be added
    Given A list of new pets are generated
    When  PetStore service | POST | invalid request to addPet endpoint
    Then  Error 500 code is in response

  @AddPet @Negative
  Scenario: A pet with non-unique id cannot be added
    Given A new pet with non unique Id is generated
    When  PetStore service | POST | valid request to addPet endpoint
   # An existing pet is updated -issue
    Then  Error 500 code is in response

    @AddPet @Negative
  Scenario: Invalid method request cannot be sent to addPet endpoint
    Given A new empty pet is generated
    When  PetStore service | GET | request to addPet endpoint
    Then  Error 405 code is in response
#
  @UploadImage @Smoke
  Scenario: Valid file can be uploaded
    Given Known pet exists in the system
    When  PetStore service | POST | valid request to uploadImage endpoint
    Then  Image is uploaded

#  @UploadImage
#  Valid Request with Additional Metadata -> Verify metadata is added in message field in response

  @UploadImage @UnsupportedFileType
  Scenario: Video file cannot be uploaded
    Given Known pet exists in the system
    When  PetStore service | POST | request with unsupported file to uploadImage endpoint
#    not image file can be uploaded - issue
    Then  Error 500 code is in response

  @UploadImage
  Scenario: Upload file request with JSON content-type
    Given Known pet exists in the system
    When  PetStore service | POST | request with JSON content-type to uploadImage endpoint
    Then  Error 415 code is in response

  @UploadImage @Negative
  Scenario: File cannot be uploaded to non existing pet
    #    there is no id filed validation-issue
    When  PetStore service | POST | valid request to uploadImage endpoint with invalid petId
    Then  Error 404 code is in response

  @UploadImage @Negative
  Scenario: Invalid Upload File request without file shold return an error
    Given Known pet exists in the system
    When  PetStore service | POST | request without file to uploadImage endpoint
    Then  Error 400 code is in response
    Then  MIMEParsingException: Missing start boundary error message is in response

  @FindByStatus @Smoke
  Scenario: Corresponded pets are returned for findByStatus request with list of statuses
    When  PetStore service | GET | request to findByStatus endpoint by the following statuses:
      | available |
      | sold      |
    Then  All pets in response corresponded to the applied the following statuses:
      | available |
      | sold      |

  @FindByStatus @Smoke
  Scenario: Corresponded pets are returned for findByStatus request with single status
    When  PetStore service | GET | request to findByStatus endpoint by the following statuses:
      | sold |
    Then  All pets in response corresponded to the applied the following statuses:
      | sold |

  @FindByStatus
#    status parameter should be enum
  Scenario: Empty array is returned for findByStatus request with non-existing status
    When  PetStore service | GET | request to findByStatus endpoint by the following statuses:
      | ddd |
    Then  Verify that empty array is in response

  @DeletePet @Negative
  Scenario: Pet cannot be deleted by unauthorized user
    Given A new pet is generated
    Given PetStore service | POST | valid request to addPet endpoint
    When  PetStore service | DELETE | request to deletePet endpoint without api_key
#    Error response should be returned without api-key deletion
    Then  The pet is deleted

    @DeletePet @Smoke
  Scenario: Pet can be deleted by an authorized user
    Given An existing user is logged in to the system
    Given A new pet is generated
    Given PetStore service | POST | valid request to addPet endpoint
    When  PetStore service | DELETE | request to deletePet endpoint with valid petId
    Then  The pet is deleted

    @DeletePet @Negative
  Scenario: Deletion un-existed pet by an authorized user
    Given An existing user is logged in to the system
    When  PetStore service | DELETE | request to deletePet endpoint with invalid petId
    Then  Error 404 code is in response
