package com.endava.Homework;

import com.endava.petclinic.controllers.OwnerController;
import com.endava.petclinic.controllers.PetController;
import com.endava.petclinic.model.Owner;
import com.endava.petclinic.model.Pet;
import com.endava.petclinic.utils.EnvReader;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;

public class PetClinicTestHomework {
    @Test

    public void postAPest(){

       Owner owner = OwnerController.generateNewRandomOwner();

        ValidatableResponse ownerPostResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .when().log().all()
                .post("/api/owners").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        Owner owner1 = new Owner();
        owner1.setId(ownerPostResponse.extract().jsonPath().getInt("id"));
        owner1.setFirstName(ownerPostResponse.extract().jsonPath().getString("firstName"));
        owner1.setLastName(ownerPostResponse.extract().jsonPath().getString("lastName"));
        owner1.setAddress(ownerPostResponse.extract().jsonPath().getString("address"));
        owner1.setCity(ownerPostResponse.extract().jsonPath().getString("city"));
        owner1.setTelephone(ownerPostResponse.extract().jsonPath().getString("telephone"));

        Pet pet =PetController.generateNewRandomPet() ;

        ValidatableResponse postPetResponse  = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet)
                .when().log().all()
                .post("/api/pets").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

    }
}
