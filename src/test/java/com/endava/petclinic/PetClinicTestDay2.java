package com.endava.petclinic;

import com.endava.petclinic.controllers.OwnerController;
import com.endava.petclinic.model.Owner;
import com.endava.petclinic.utils.EnvReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PetClinicTestDay2 {

    @Test
    public void postOwnerTest(){
        HashMap <String, String> owner = new HashMap<>();
        owner.put("id", null);
        owner.put("firstName", "Ionut");
        owner.put("lastName", "Dinescu");
        owner.put("address" , "7 August");
        owner.put("city" , "Bucharest");
        owner.put("telephone" , "0785258456");

        ValidatableResponse responsePostOwner = given().baseUri("http://api.petclinic.mywire.org")
                .basePath("/petclinic")
                .port(80)
                .contentType(ContentType.JSON)
                .body(owner)
                .when()
                .post("/api/owners").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        Integer ownerId = responsePostOwner.extract().jsonPath().getInt("id");

        given().baseUri("http://api.petclinic.mywire.org")
                .basePath("/petclinic")
                .port(80)
                .pathParam("ownerId" , ownerId)
                .when()
                .get("/api/owners/{ownerId}").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is(ownerId));

    }

    @Test
    public void postOwnerTestWithObject(){
        Owner owner = OwnerController.generateNewRandomOwner();
        ValidatableResponse response = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .when()
                .post("/api/owners").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        owner.setId(response.extract().jsonPath().getInt("id"));
        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("ownerId", owner.getId())
                .when()
                .get("/api/owners/{ownerId}").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);

        Owner ownerFromGetResponse = getResponse.extract().as(Owner.class);

        assertThat(ownerFromGetResponse, is(owner));

    }

    @Test
    public void putOwnerTest(){
        Faker faker=new Faker();
        Owner owner = OwnerController.generateNewRandomOwner();

        ValidatableResponse postResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .when()
                .post("/api/owners").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        owner.setId(postResponse.extract().jsonPath().getInt("id"));

        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setTelephone(faker.number().digits(10));

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .pathParam("ownerId" , owner.getId())
                .body(owner)
                .put("/api/owners/{ownerId}").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("ownerId", owner.getId())
                .when()
                .get("/api/owners/{ownerId}").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);

        Owner ownerFromGetResponse = getResponse.extract().as(Owner.class);
        assertThat(ownerFromGetResponse, is(owner));

    }
}
