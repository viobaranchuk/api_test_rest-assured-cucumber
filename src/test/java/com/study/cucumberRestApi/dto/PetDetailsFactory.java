package com.study.cucumberRestApi.dto;

import com.study.cucumberRestApi.utils.ResourceFileLoader;

public class PetDetailsFactory {
    public enum PetDetails {
        VALID_PET("pet", "pet-default.json"),
        INVALID_PET_WITH_STRING_ID("pet", "pet-details-string-id.json");
        final String folder;
        final String filename;

        PetDetails(String folder, String filename) {
            this.folder = folder;
            this.filename = filename;
        }
    }

    private PetDetailsFactory() {
    }

    public static PetDto getDefaultPet(){
        return getFromResources(
                PetDetails.VALID_PET.folder,
                PetDetails.VALID_PET.filename,
                PetDto.class
        );
    }

    public static <T> T getFromResources(String folder, String filename, Class<T> objectType) {
        return new ResourceFileLoader(PetDetailsFactory.class)
                .withPath(folder)
                .withName(filename)
                .jsonToObject(objectType);
    }
}
