package com.automation.tests.day4;
import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import  static org.hamcrest.Matchers.*;

public class MetaWeatherJsonPathTests {
    @BeforeAll
    public static void setup(){
            baseURI = ConfigurationReader.getProperty("meta.weather.uri");
    }

    /**
     * TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'New'
     * Then user verifies that payload contains 5 objects
     */
    @Test
    @DisplayName("Verify that are 5 cities that are matching 'New'")
    public void test1(){
        given().
            accept("application/json").
            queryParam("query", "new").
        when().
            get("/search").
        then().
            assertThat().
                statusCode(200).
                body("", hasSize(5)).
        log().body(true);
    }

    /**
     * TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is New
     * Then user verifies that 1st object has following info:
     * |title   |location_type|woeid  |latt_long          |
     * |New York|City         |2459115|40.71455,-74.007118|
     */
    @Test
    @DisplayName("Verify First Object")
    public void test2(){
        given().
                accept(ContentType.JSON).
                queryParam("query", "New").
        when().
                get("/search").
        then().
                assertThat().
                    statusCode(200).
                    body("title[0]", is("New York")).
                    body("location_type[0]", is("City")).
                    body("latt_long[0]", is("40.71455,-74.007118")).
                    body("woeid[0]", is(2459115)).
        log().body(true);
    }

    @Test
    @DisplayName("Verify First Object")
    public void test2_2(){
        Map<String,String> expected = new HashMap<>();
        expected.put("title", "New York");
        expected.put("location_type", "City");
        expected.put("woeid", "2459115");
        expected.put("latt_long", "40.71455,-74.007118");

        Response response = given().
                                accept(ContentType.JSON).
                                queryParam("query", "New").
                            when().
                                get("/search");
    JsonPath jsonPath = response.jsonPath();
    //String.class, String.class will force jsonpath to return map with String as key and value
    assertEquals(expected, jsonPath.getMap("[0]", String.class, String.class));
    //for first title, title[0], but for first object, we can say just [0]
    // if one object is a key=value pair like map, collection of this objects can be represented as list of map

        List<Map<String, ?>> values = jsonPath.get();
        for(Map<String, ?> value: values){
            System.out.println(value);
        }
    }

    /* * TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'Las'
     * Then user verifies that payload  contains following titles:
     * |Glasgow  |
     * |  Dallas |
     * |Las Vegas|*/
    @Test
    @DisplayName("Verify payload contains 'Glasgow', 'Dallas', 'Las Vegas'")
    public void test3(){
        List<String> expectedCities = List.of("Glasgow", "Dallas", "Las Vegas");
        given().
                accept(ContentType.JSON).
                queryParam("query", "Las").
        when().
                get("/search").
        then().
                assertThat().
                    statusCode(200).
                    body("title", contains(expectedCities.toArray()));
    }








}