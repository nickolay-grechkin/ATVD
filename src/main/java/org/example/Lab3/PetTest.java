package org.example.lab3;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class PetTest {
    public static final String baseUrl = "https://petstore.swagger.io/v2";

    public static final String PET = "/pet";
    public static final String FIND_BY_STATUS = PET + "/findByStatus";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void testGetPetAction() {
        given().get(FIND_BY_STATUS + "?status=available")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testPostNewPet() {
        Map<String, Object> category = Map.of(
                "id", 0,
                "name", Faker.instance().dog().breed()
        );

        Map<String, Object> tag = Map.of(
                "id", 0,
                "name", "fine"
        );

        List<String> photoUrls = Collections.singletonList("photo-url");

        Map<String, Object> body = Map.of(
                "id", 0,
                "category", category,
                "name", Faker.instance().dog().name(),
                "photoUrls", photoUrls,
                "tags", Collections.singletonList(tag),
                "status", "available"
        );

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testUpdateExistingPet() {

        Map<String, Object> body = Map.of(
                "id", 0,
                "name", Faker.instance().dog().name()
        );

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
