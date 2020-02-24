package com.automation.tests.day8;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import  static org.hamcrest.Matchers.*;

public class CalendarificAPIKey {
    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("calendarific.uri");
    }
    /**
     * API key is a secret that the API generates and gives to the developer.
     * API key looks like long string: a54400a8fc7b470ea4850c19ae7997a417996f0b
     * API key can go as query parameter or inside a header,
     * it depends on web service how you must pass API key (as header parameter or query parameter)
     * How it get's created? You go to web site, register, and service gives you API key
     * Then you have to pass API key alongside with every request
     * API key is easy to implement for developer and client
     * But, non-technical people have no idea about this
     * So it's mostly used by developers only
     */

    /**
     * Given accept content type as JSON
     * When user sends GET request to "/countries"
     * Then user verifies that status code is 401
     * And user verifies that status line contains "Unauthorized" message
     * And user verifies that meta.error_detail contains "Missing or invalid api credentials." message
     */

    @Test
    @DisplayName("Verify that user cannot access WebService without valid API key")
    public void test1(){
        given().
                accept(ContentType.JSON).
        when().
                get("/countries").prettyPeek().
        then().
                assertThat().
                    statusCode(401).
                    statusLine(containsString("Unauthorized")).
                    body("meta.error_detail", containsString("Missing or invalid api credentials."));
    }

    /**
     * Given accept content type as JSON
     * And query parameter api_key with valid API key
     * When user sends GET request to "/countries"
     * Then user verifies that status code is 200
     * And user verifies that status line contains "OK" message
     * And user verifies that countries array is not empty
     */
    @Test
    @DisplayName("With API key")
    public void test2(){
        given().
                accept(ContentType.JSON).
                queryParam("api_key", "a54400a8fc7b470ea4850c19ae7997a417996f0b").
        when().
                get("/countries").prettyPeek().
        then().
                assertThat().
                    statusCode(200).
                    statusLine(containsString("OK")).
                    body("response.countries", not(empty()));
    }

    /**
     *
     * Given accept content type as JSON
     * And query parameter api_key with valid API key
     * And query parameter country is equals to US
     * And query parameter type is equals to national
     * And query parameter year is equals to 2019
     * When user sends GET request to "/holidays"
     * Then user verifies that number of national holidays in US is equals to 11
     */
    @Test
    @DisplayName("Verify that there are 11 national holidays in the US")
    public void test3(){
        Response response = given().
                accept(ContentType.JSON).
                queryParam("api_key","ebe88078e981c84bedeb9e8a34ad927560e545f2").
                queryParam("country", "US").
                queryParam("type", "national").
                queryParam("year", 2019).
                when().
                get("/holidays").prettyPeek();
        //shorter way with syntax sugar (hamcrest matcher)
        response.then().assertThat().statusCode(200).body("response.holidays", hasSize(11));
        //List<Map<String, ?>> - list of objects, since there are nested objects, we cannot specify some value type
        List<Map<String, ?>> holidays = response.jsonPath().get("response.holidays");

        //regular way
        assertEquals(11, holidays.size(), "Wrong number of holidays!");
        assertEquals(200, response.getStatusCode());
    }
}
