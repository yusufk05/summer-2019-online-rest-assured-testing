package com.automation.tests.day9;
import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import  static org.hamcrest.Matchers.*;
public class WarmUp {
    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("calendarific.uri");
    }
    /**
     *  ####### WARM-UP TASK ########
     * Given accept content type as JSON
     * And query parameter api_key with valid API key
     * When user sends GET request to "/countries"
     * Then user verifies that total number of holidays in United Kingdom is equals to 95
     *
     * website: https://calendarific.com/
     */

    @Test
    @DisplayName("user verifies that total number of holidays in United Kingdom is equals to 95")
    public void test1(){
        Response response = given().
                accept(ContentType.JSON).
                queryParam("api_key", "a54400a8fc7b470ea4850c19ae7997a417996f0b").
        when().get("/countries").prettyPeek();

        //Gpath - it's like Xpath, stands for searching values in JSON file.
        //GPath: response.countries.find {it.country_name == 'United Kingdom'}.total_holidays
        //find - method to find some parameter
        //{it.parameter_name == value} find JSON object that is matching criteria
        //.parameter_that_you_want = return this parameter after filtering
        int holidays = response.jsonPath().get("response.countries.find {it.country_name == 'United Kingdom'}.total_holidays");
        assertEquals(95, holidays);

        //get all countries where number supported_languages is = 4
        List<String> countries = response.jsonPath().get("response.countries.findAll {it.supported_languages == 4}.country_name");
        System.out.println(countries);


    }

}
