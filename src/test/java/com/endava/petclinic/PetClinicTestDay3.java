package com.endava.petclinic;

import com.endava.petclinic.controllers.OwnerController;
import com.endava.petclinic.controllers.PetController;
import com.endava.petclinic.controllers.TypeController;
import com.endava.petclinic.model.Owner;
import com.endava.petclinic.model.Pet;
import com.endava.petclinic.model.Type;
import com.endava.petclinic.model.User;
import com.endava.petclinic.utils.EnvReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PetClinicTestDay3 {

    @Test
    public void postPet() {
        Pet pet = PetController.generateNewRandomPet();

        ValidatableResponse createOwnerResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet.getOwner())
                .post("/api/owners").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        pet.getOwner().setId(createOwnerResponse.extract().jsonPath().getInt("id"));

        ValidatableResponse createPetTypeResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet.getType())
                .post("/api/pettypes").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        pet.getType().setId(createPetTypeResponse.extract().jsonPath().getInt("id"));

        ValidatableResponse createPetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet)
                .post("/api/pets").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        pet.setId(createPetResponse.extract().jsonPath().getInt("id"));

        ValidatableResponse getPetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("petId", pet.getId())
                .get("/api/pets/{petId}")
                .then()
                .statusCode(HttpStatus.SC_OK);

        Pet actual = getPetResponse.extract().as(Pet.class);
        assertThat(actual, is(pet));
    }

    @Test
    public void creatPetWithOwnerandPetTypeTest(){
        Owner owner = OwnerController.generateNewRandomOwner();

        ValidatableResponse createOwnerResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .post("/api/owners").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        owner.setId(createOwnerResponse.extract().jsonPath().getInt("id"));

        //Type type = new Type(new Faker().animal().name());
        Type type2 = TypeController.generateRandomType();

        ValidatableResponse createPetTypeResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(type2)
                .post("/api/pettypes").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);
        type2.setId(createPetTypeResponse.extract().jsonPath().getInt("id"));

        Pet pet = PetController.generateNewRandomPet();
        pet.setOwner(owner);
        pet.setType(type2);


        ValidatableResponse createPetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet)
                .post("/api/pets").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        pet.setId(createPetResponse.extract().jsonPath().getInt("id"));

        ValidatableResponse getPetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("petId", pet.getId())
                .get("/api/pets/{petId}")
                .then()
                .statusCode(HttpStatus.SC_OK);

        Pet actual = getPetResponse.extract().as(Pet.class);
        assertThat(actual, is(pet));
    }


    @Test
    public void postOwnerTestWithObject(){
        Owner owner = OwnerController.generateNewRandomOwner();
        ValidatableResponse response = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive()
                .basic("admin" , "admin")
                .contentType(ContentType.JSON)
                .body(owner)
                .when()
                .post("/api/owners").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        owner.setId(response.extract().jsonPath().getInt("id"));
        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive()
                .basic("admin" , "admin")
                .pathParam("ownerId", owner.getId())
                .when()
                .get("/api/owners/{ownerId}").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);

        Owner ownerFromGetResponse = getResponse.extract().as(Owner.class);

        assertThat(ownerFromGetResponse, is(owner));

    }

    @Test
    public void createOwnerSecured(){
        Faker faker = new Faker();
        User user = new User(faker.name().username(), faker.internet().password(), "OWNER_ADMIN", "VET_ADMIN");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(user).log().all()
                .post("/api/users").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        Owner owner = OwnerController.generateNewRandomOwner();

        ValidatableResponse response = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive()
                .basic(user.getUsername(), user.getPassword())
                .contentType(ContentType.JSON)
                .body(owner).log().all()
                .when()
                .post("/api/owners").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        owner.setId(response.extract().jsonPath().getInt("id"));
        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive()
                .basic(user.getUsername() , user.getPassword())
                .pathParam("ownerId", owner.getId())
                .when()
                .get("/api/owners/{ownerId}").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);

        Owner ownerFromGetResponse = getResponse.extract().as(Owner.class);

        assertThat(ownerFromGetResponse, is(owner));

    }



}
