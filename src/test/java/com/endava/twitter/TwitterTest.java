package com.endava.twitter;

import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.math.BigInteger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class TwitterTest {

    private static final String KEY ="xDpvjjIfui8Su72Ti1AR1CKzi";
    private static final String KEYSECRET = "HobgaN3k3jn0vk11ItpV1CSWhyzpIm0Nf2FXbF01wZmDNxmyxl";
    private static final String TOKEN = "278136668-zJNcE6VzUubCamfnS29EEw1S0HyRUDufFl42AbUk";
    private static final String TOKENSECRET = "2Q5WhderaUy2Tk8tL7n400L4ddoXm6STWtasc6yKpM3Wf";


    private RequestSpecification createRequestSpecification(){
        return given().baseUri("https://api.twitter.com")
                .basePath("1.1/statuses ")
                .auth()
                .oauth(KEY,KEYSECRET, TOKEN, TOKENSECRET);
    }

    @Test
    public void postTweetTest(){
        createRequestSpecification()
                .queryParam("status", "Hello again!")
                .post("/update.json")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void getTwitterTest(){
        String message = "House of " + new Faker().gameOfThrones().house() + " Dragon " + new Faker().gameOfThrones().dragon();

        ValidatableResponse postResponse = createRequestSpecification()
                .queryParam("status", message).log().all()
                .post("/update.json").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);

        BigInteger id = BigInteger.valueOf(postResponse.extract().jsonPath().getInt("id"));
        String id_str = postResponse.extract().jsonPath().getString("id_str");


       createRequestSpecification()
                .get("user_timeline.json").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id_str", hasItem(id_str))
                .body("text", hasItem(message));



    }
}
