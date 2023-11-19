package org.example.lab4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

public class TestMockServer {
    public static final String baseUrl = "https://60076a33-8730-4fea-997c-b418158aa100.mock.pstmn.io";


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void testGetSuccess() {
        given().get("/ownerName")
                .then()
                .body("name", equalTo("Mykola Hrechkin"))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testGetUnSuccess() {
        given().get("/ownerName/unsuccess")
                .then()
                .body("name", equalTo("I won't say my name!"))
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testPostSuccess() {
        given().post("/createSomething?permission=yes")
                .then()
                .body("result", equalTo("Nothing was created"))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testPostUnSuccess() {
        given().post("/createSomething")
                .then()
                .body("result", equalTo("You don't have permissions to create something."))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testPut() {
        given().put("/updateMe")
                .then()
                .body("name", equalTo(""))
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDelete() {
        given().delete("/deleteWorld")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
