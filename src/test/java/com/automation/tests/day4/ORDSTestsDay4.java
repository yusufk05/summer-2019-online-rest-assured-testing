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


public class ORDSTestsDay4 {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    /**
     * Warmup!
     * Given accept type is JSON
     * When users sends a GET request to "/employees"
     * Then status code is 200
     * And Content type is application/json
     * And response time is less than 3 seconds
     */

    @Test
    @DisplayName("Verify that response time is less than 3 sec")
    public void test1(){
        given().
                accept("application/json").
        when().
                get("/employees").
        then().
                assertThat().
                        statusCode(200).
                        contentType(ContentType.JSON).
                        time(lessThan(3L), TimeUnit.SECONDS).
                log().body(true);
    }

        /*{{baseUrl}}/countries?q={"country_id":"US"}
    Given accept type is JSON
    And parameters: q = {"country_id":"US"}
    When users sends a GET request to "/countries"
    Then status code is 200
    And Content type is application/json
    And country_name from payload is "United States of America"
 */
    @Test
    @DisplayName("Verify that country_name from payload is \"United States of America\"")
    public void test2(){
        given().
                accept(ContentType.JSON).
                queryParam("q", "{\"country_id\":\"US\"}").
        when()
                .get("/countries").
        then().
                assertThat().
                        statusCode(200).
                        contentType(ContentType.JSON).
                        body("items[0].country_name", is("United States of America")).
                        log().body(true);
    }

    @Test
    @DisplayName("Get all links")
    public void test3(){
        JsonPath jsonPath = given().
                accept(ContentType.JSON).
                queryParam("q", "{\"country_id\":\"US\"}").
                when()
                .get("/countries").
                thenReturn().jsonPath();

        List<?> links = jsonPath.getList("items.links.href");
        //if I don't put index, I will get collection of properties (only if they exists)
        for (Object link : links){
            System.out.println(link);
        }
    }

    @Test
    @DisplayName("Verify that payload contains only 25 countries")
    public void test4(){
        List<?> countries = given().
                                accept(ContentType.JSON).
                            when().
                                get("/countries").
                            thenReturn().jsonPath().getList("items");

        assertEquals(25, countries.size());
    }

    /**
     * given path parameter is "/countries" and region id is 2
     * when user makes get request
     * then assert that status code is 200
     * and user verifies that body returns following country names
     *  |Argentina                |
     *  |Brazil                   |
     *  |Canada                   |
     *  |Mexico                   |
     *  |United States of America |
     */

    @Test
    public void test5(){
        List<String> expected = Arrays.asList("Argentina", "Brazil", "Canada", "Mexico", "United States of America");

        Response response = given().
                accept(ContentType.JSON).
                queryParam("q", "{\"region_id\":2}").
        when().
                get("/countries");

        List<String> actual = response.jsonPath().getList("items.country_name");
        assertEquals(expected, actual);

        // with asserThat
        given().
                accept(ContentType.JSON).
                queryParam("q", "{\"region_id\":2}").
                when().
                get("/countries").
                then().assertThat().
                body("items.country_name", contains("Argentina", "Brazil", "Canada", "Mexico", "United States of America")).
        log().body(true);
    }

    /**
     * given path parameter is "/employees"
     * when user makes get request
     * then assert that status code is 200
     * Then user verifies that every employee has positive salary
     *
     */
    @Test
    @DisplayName("Verify that every employee has positive salary")
    public void test6(){
        given().
                accept(ContentType.JSON).
        when().
                get("/employees").
        then().
                assertThat().
                    statusCode(200).
                    body("items.salary", everyItem(greaterThan(0)));

        //whenever you specify path as items.salary, you will get collection of salaries
        //then to check every single value
        //we can use everyItem(is()), everyItem(greaterThan())
        /**
         * Creates a matcher for {@link Iterable}s that only matches when a single pass over the
         * examined {@link Iterable} yields i the spetems that are all matched bycified
         * <code>itemMatcher</code>.
         * For example:
         * <pre>assertThat(Arrays.asList("bar", "baz"), everyItem(startsWith("ba")))</pre>*/
    }

    /**
     * given path parameter is "/employees/{id}"
     * and path parameter is 101
     * when user makes get request
     * then assert that status code is 200
     * and verifies that phone number is 515-123-4568
     *
     */
    @Test
    @DisplayName("Verify that employee 101 has following phone number: 515-123-4568")
    public void test7(){
        String expectedNumber = "515-123-4568";

        Response response = given().
                accept(ContentType.JSON).
                pathParam("id", 101).
                when().
                get("/employees/{id}");
        assertEquals(200, response.getStatusCode());

        expectedNumber = expectedNumber.replace("-", ".");
        String actualNumber = response.jsonPath().getString("phone_number");

        assertEquals(expectedNumber, actualNumber);
    }

    /**
     * given path parameter is "/employees"
     * when user makes get request
     * then assert that status code is 200
     * and verify that body returns following salary information after sorting from higher to lower
     *  24000, 17000, 17000, 12008, 11000,
     *  9000, 9000, 8200, 8200, 8000,
     *  7900, 7800, 7700, 6900, 6500,
     *  6000, 5800, 4800, 4800, 4200,
     *  3100, 2900, 2800, 2600, 2500
     *
     */

    @Test
    @Description("Verify that body returns following salary information after sorting from higher to lower(after sorting it in descending order)")
    public void test8(){

        List<Integer> expectedSalaries = List.of(24000, 17000, 17000, 12008, 11000,
                                                9000, 9000, 8200, 8200, 8000,
                                                7900, 7800, 7700, 6900, 6500,
                                                6000, 5800, 4800, 4800, 4200,
                                                3100, 2900, 2800, 2600, 2500);
        Response response = given().
                                accept(ContentType.JSON).
                            when().
                                get("/employees");

        assertEquals(200, response.statusCode());

        List<Integer> actualSalaries = response.jsonPath().getList("items.salary");
        Collections.sort(actualSalaries, Collections.reverseOrder());
        System.out.println(actualSalaries);
        assertEquals(expectedSalaries, actualSalaries, "Salaries are not matching");
    }

    /** ####TASK#####
     *  Given accept type as JSON
     *  And path parameter is id with value 2900
     *  When user sends get request to /locations/{id}
     *  Then user verifies that status code is 200
     *  And user verifies following json path contains following entries:
     *      |street_address         |city  |postal_code|country_id|state_province|
     *      |20 Rue des Corps-Saints|Geneva|1730       |CH        |Geneve        |
     */

    /**
     *     "location_id": 2900,
     *     "street_address": "20 Rue des Corps-Saints",
     *     "postal_code": "1730",
     *     "city": "Geneva",
     *     "state_province": "Geneve",
     *     "country_id": "CH",
     */

    @Test
    @Description("Verify that JSON body has following entries")
    public void test9(){
        given().
                accept(ContentType.JSON).
        when().
                get("/locations/{id}", 2900).
        then().
                assertThat().
                    statusCode(200).
                    body("", hasEntry("street_address", "20 Rue des Corps-Saints")).
                    body("", hasEntry("postal_code", "1730")).
                    body("", hasEntry("city", "Geneva")).
                    body("", hasEntry("state_province", "Geneve")).
                    body("", hasEntry("country_id", "CH")).
        log().all(true);
    }

    @Test
    @DisplayName("Verify that JSON body has following entries")
    public void test9_2(){
        given().
                accept(ContentType.JSON).
                pathParam("id", 2900).
        when().
                get("/locations/{id}").
        then().
                assertThat().
                    statusCode(200).
                    body("street_address", is("20 Rue des Corps-Saints")).
                    body("city", is("Geneva")).
                    body("postal_code", is("1730")).
                    body("country_id", is("CH")).
                    body("state_province", is("Geneve")).
                    log().all(true);
    }















}
