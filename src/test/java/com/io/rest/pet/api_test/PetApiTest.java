package com.io.rest.pet.api_test;

import com.io.rest.pet.dto.Category;
import com.io.rest.pet.dto.Pet;
import com.io.rest.pet.dto.Tag;
import com.io.rest.pet.util.TestUtil;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class PetApiTest {

    private long id;
    private long invalid_id;
    private String photoURL;

    public PetApiTest() {
        id = new TestUtil().getId();
        invalid_id = new TestUtil().getInvalidID();
        photoURL = new TestUtil().getPhotoURL();
    }

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI =  "https://petstore.swagger.io/v2";
        //RestAssured.baseURI =  new TestUtil().getBaseUri();
    }


    @Test(priority = 1)
    public void givenValidDetails_Create_PetData_Check_StatusCode200() {

        Category category = new Category();
        category.setId(34L);
        category.setName("Sporting Group");

        Tag tag = new Tag();
        tag.setId(34L);
        tag.setName("Sporting");

        Pet pet = new Pet();
        pet.setId(id);
        pet.setName("Jojo");
        pet.setStatus("available");
        pet.setCategory(category);
        pet.setTags(Arrays.asList(tag));
        pet.setPhotoUrls(Arrays.asList("https://images.dog.ceo//breeds//labrador//n02099712_5008.jpg"));

        JsonPath jsonPath = RestAssured.given()
                .when()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .with()
                .body(pet)//add pet object
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(200)
                .log()
                .body()
                .assertThat()
                .extract().body().jsonPath();

        get(baseURI + "/pet/" + pet.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .log()
                .body()
                .body("id", equalTo(pet.getId().intValue()))
                .body("name", equalToCompressingWhiteSpace(pet.getName()))
                .body("status", equalToCompressingWhiteSpace(pet.getStatus()))
                .body("category.id",equalTo(pet.getCategory().getId().intValue()))
                .body("category.name",equalTo(pet.getCategory().getName()))
                .body("tags[0].id",equalTo(pet.getTags().get(0).getId().intValue()))
                .body("tags[0].name",equalTo(pet.getTags().get(0).getName()))
                .body("photoUrls[0]", containsStringIgnoringCase("labrador"));

    }


    @Test(priority = 2)
    public void givenPetID_Update_Check_StatusCode200() {

        RestAssured.given()
                .when()
                .contentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                .formParam("name", "TommyLabrador")
                .formParam("status", "pending")
                .post("/pet/" + id)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200);

    }


    @Test(priority = 3)
    public void givenPetIdReturn_Pet_Data_Check_StatusCode200() {

        JsonPath jsonPath = RestAssured.given()
                .when()
                .get("/pet/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .extract().body().jsonPath();

        assertEquals("TommyLabrador", jsonPath.get("name"));
        assertEquals("pending", jsonPath.get("status"));

    }


    @Test(priority = 4)
    public void givenPetId_Delete_Pet_Check_StatusCode200() {

        RestAssured.given()
                .when()
                .header("api_key", "token")
                .delete("/pet/" + id)
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void givenPetID_Delete_InvalidPet_Check_StatusCode404() {

        RestAssured.given()
                .when()
                .header("api_key", "token")
                .delete("/pet/" + id)
                .then()
                .assertThat()
                .statusCode(404);

    }

    @Test(priority = 6)
    public void givenInvalid_PetID_checkGetPetId_Check_StatusCode404() {
        Response response =
                get("/pet/" + invalid_id);
        assertTrue(response.getStatusCode()==404);

    }

}