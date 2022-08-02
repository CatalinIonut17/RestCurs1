package com.endava.petclinic;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetClinicTest {

    @Test
    public void getOwnerById(){
        given().baseUri("http://api.petclinic.mywire.org/")
                .basePath("/petclinic")
                .port(80)
                .pathParam("ownerId", 1)
                .when().log().all()
                .get("/api/owners/{ownerId}")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is(1))
                .body("firstName", is("George"))
                .body("firstName", containsString("org"))
                .body("lastName", startsWith("Fr"))
                .body("city", equalToIgnoringCase("madiSon"))
                .body("telephone" ,hasLength(10) );
    }

    @Test
    public void postOwnersTest(){
        ValidatableResponse response = given().baseUri("http://api.petclinic.mywire.org/")
                .basePath("/petclinic")
                .port(80)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"address\": \"Strada Iarna\",\n" +
                        "  \"city\": \"Baia Mare\",\n" +
                        "  \"firstName\": \"Cornel\",\n" +
                        "  \"id\": null,\n" +
                        "  \"lastName\": \"Istrate\",\n" +
                        "  \"telephone\": \"0795863254\"\n" +
                        "}")
                .when()
                .post("/api/owners")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        Integer owenerId = response.extract().jsonPath().getInt("id");
        given().baseUri("http://api.petclinic.mywire.org/")
                .port(80)
                .basePath("/petclinic")
                .pathParam("ownerId", owenerId)
                .when()
                .get("/api/owners/{ownerId}")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is(owenerId));

    }



}
