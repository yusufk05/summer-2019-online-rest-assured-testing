package com.automation.tests.day8;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class BasicAuthenticationTests {

    @BeforeAll
    public static void setup(){
        baseURI = "http://practice.cybertekschool.com";
    }

    @Test
    @DisplayName("Basic Authentication test")
    public void test1(){
        given().
                auth().basic("admin", "admin").
        when().
                get("/basic_auth").prettyPeek().
        then().assertThat().statusCode(200);
    }
}
