package com.endava.Wheater;

import com.github.javafaker.Faker;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class WheaterTest {

    private final String KEY = "14fdae5097e006752d94eed9ea503201";

    /*@Test

    private RequestSpecification createRequestSpecification(){
        return given().baseUri("https://api.openweathermap.org")
                .basePath("data/2.5")
                .auth()
                .preemptive()

    }*/

    @Test
    public void getWheaterTest(){

        given().baseUri("https://api.openweathermap.org")
                .basePath("data/2.5")
                .auth()
                .none()
                .queryParam("appid" , KEY)
                .queryParam("units" , "metric")
                .queryParam("q", "Bucharest").log().all()
                .get("/weather").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bucharest", "Craiova" , "Constanta" , "Iasi", "Targoviste"})
    public void getWheaterTestParam(String city){

        given().baseUri("https://api.openweathermap.org")
                .basePath("data/2.5")
                .auth()
                .none()
                .queryParam("appid" , KEY)
                .queryParam("units" , "metric")
                .queryParam("q", city).log().all()
                .get("/weather").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", is(city));
    }

}
