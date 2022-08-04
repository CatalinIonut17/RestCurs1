package com.endava.facebook;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class FacebookTest {
    private final String ID = "455136538609782";
    private final String TOKEN = "455136538609782|77cc2b5e28ee35c76bead0348506af38";

    @Test
    public void addUserTest() {
        given().baseUri("https://graph.facebook.com")
                .basePath("/v14.0")
                .auth()
                .oauth2(TOKEN)
                .pathParam("application-id", ID)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"name\": \"Catalin Dinescu\",\n" +
                        "\t\"password\": \"qwertyuiop\"\n" +
                        "}").log().all()
                .post("/{application-id}/accounts/test-users").prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);
    }


}



