package com.automation.tests.day7;

import com.automation.pojos.Spartan;
import com.automation.utilities.ConfigurationReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import  static org.hamcrest.Matchers.*;

public class SpartanTestsDay7 {
    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("spartan.uri");
    }

    //add new spartan from external file
    @Test
    @DisplayName("Add new user by using external JSON File")
    public void test1(){
        File file = new File(System.getProperty("user.dir")+"/spartan.json");

        given().
                accept(ContentType.JSON).
                contentType(ContentType.JSON).
                body(file).
        when().
                post("/spartans").prettyPeek().
        then().
                assertThat().
                    statusCode(201).
                    body("success", is("A Spartan is Born!"));
    }

    @Test
    @DisplayName("Add new user by using map")
    public void test2(){
        Map<String, Object> spartan = new HashMap<>();
        spartan.put("phone", 12345678901L);
        spartan.put("gender", "Female");
        spartan.put("name", "Trios");

        given().
                accept(ContentType.JSON).
                contentType(ContentType.JSON).
                body(spartan).
        when().
                post("/spartans").prettyPeek().
        then().
                assertThat().
                    statusCode(201).
                    body("success", is("A Spartan is Born!")).
                    body("data.name", is("Trios"));
    }

    @Test
    @DisplayName("Update Spartan only name PATCH")
    public void test3(){
        Map<String, Object> update = new HashMap<>();

        update.put("name", "Tresa");

        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(update).
                pathParam("id", 25).
        when().
                patch("/spartans/{id}").prettyPeek().
        then().
                assertThat().
                    statusCode(204);

    }






}
