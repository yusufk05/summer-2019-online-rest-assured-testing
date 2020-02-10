package com.automation.tests.Homeworks.HW1;

import com.automation.utilities.ConfigurationReader;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jdk.jfr.Description;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.*;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import  static org.hamcrest.Matchers.*;

public class UInameAPItest {

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("UInames.uri");
    }

    @Test
    @DisplayName("No params test")
    public void test1(){
        given().
                accept("application/json").
        when().
                get().
        then().assertThat().
                statusCode(200).
                contentType("application/json;charset=utf-8").
                body("name", is(notNullValue())).
                body("surname", is(notNullValue())).
                body("gender", is(notNullValue())).
                body("region", is(notNullValue())).
                log().body(true);
    }

    @Test
    @DisplayName("Gender Test")
    public void test2(){
        given().
                accept("application/json").
                queryParam("gender", "male").
        when().
                get().
        then().
                assertThat().
                    statusCode(200).
                    contentType("application/json;charset=utf-8").
                    body("gender", is("male"));
    }

    @Test
    @DisplayName("2 params test")
    public void test3(){
        given().
                accept("application/json").
                queryParam("region", "austria").
                queryParam("gender", "male").
        when().
                get().
        then().
                assertThat().
                    statusCode(200).
                    contentType("application/json;charset=utf-8").
                    body("region", is("Austria")).
                    body("gender", is("male")).
                    log().body(true);
    }

    @Test
    @DisplayName("Invalid gender test")
    public void test4(){
        given().
                accept("application/json").
                queryParam("gender", "qwert").
        when().
                get().
        then().
                assertThat().
                statusCode(400).

                body("error", equalTo("Invalid gender"));
    }

    @Test
    @DisplayName("Invalid region test")
    public void test5(){
        given().
                accept("application/json").
                queryParam("region", "qwert").
        when().
                get().
        then().
                assertThat().
                statusCode(400).
                statusLine(containsString("Bad Request")).
                body("error", equalTo("Region or language not found"));
    }

    @Test
    @DisplayName("Amount and regions test")
    public void test6(){
        Response response =given().
                accept("application/json").
                queryParam("region", "Turkey").
                queryParam("amount", 5).
        when().
                get();

        assertEquals(200, response.statusCode());
        assertEquals("application/json; charset=utf-8", response.contentType());

        List<?> combination = response.jsonPath().getList("");
        System.out.println(combination);
    }

    @Test
    @DisplayName("3 params test")
    public void test7() {
        given().
                accept(ContentType.JSON).
                queryParam("region", "turkey").
                queryParam("gender", "female").
                queryParam("amount", 3).
        when().
                get().
        then().
                assertThat().
                    statusCode(200).
                    contentType(ContentType.JSON).
                    body("region", everyItem(is("Turkey"))).
                    body("gender", everyItem(is("female")));
    }

    @Test
    @DisplayName("Amount count test")
    public void test8(){
        given().
                accept(ContentType.JSON).
                queryParam("amount", 3).
        when().
                get().
         then().
                assertThat().
                statusCode(200).
                contentType(ContentType.JSON).
                body("size()", is(3));
    }

}
