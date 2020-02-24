package com.automation.tests.day8;

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

public class OMDBTestAPIKey {

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("omdb.uri");
    }

    @Test
    @DisplayName("Verify that unautohorized user cannot get info about movies from OMDB api")
    public void test1(){
        given().
                accept(ContentType.JSON).
                queryParam("t", "Home Alone").
        when().
                get().prettyPeek().
        then().assertThat().statusCode(401).body("Error", is("No API key provided."));
    }

    @Test
    @DisplayName("Verify that Macaulay Culkin appears in actors list for Home Alone movie")
    public void test2(){
        Response response = given().
                contentType(ContentType.JSON).
                queryParam("t", "Home Alone").
                queryParam("apikey", "fe2c569b").
                when().
                get().prettyPeek();

        response.then().assertThat().statusCode(200).body("Actors", containsString("Macaulay Culkin"));

        Map<String, Object> payload = response.getBody().as(Map.class);

        System.out.println(payload);

        //entry - key=value pair
        //map it's a collection of entries
        //how to iterate a map?
        for(Map.Entry<String, Object> entry: payload.entrySet()){
            System.out.println("Key: "+entry.getKey()+", value: "+entry.getValue());
        }
    }
}
