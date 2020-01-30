package com.automation.tests.day2;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRatesAPITests {
    private String baseURI = "http://api.openrates.io";

    //GET https://api.exchangeratesapi.io/latest?base=USD HTTP/1.1
    //base it's a query parameter that will ask web service to change currency from eu to something else

    @Test
    public void test1(){
        Response response = given().
                get(baseURI+"/latest");
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    @Test
    public void test2(){
        Response response = given().get(baseURI+"/latest");
        //verify that content type is json
        assertEquals(200,response.getStatusCode());
        //verify that data is coming from as json
        assertEquals("application/json", response.getHeader("Content-Type"));
        //or like this
        assertEquals("application/json",response.getContentType());
    }

    //get currency exchange rate for DOLLAR
    @Test
    public void test3(){
        Response response = given().get(baseURI+"/latest?base=USD");
        // or like this
        Response response1 = given().
                baseUri(baseURI).basePath("/latest").
                queryParam("base","USD").get();
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());

        assertEquals(200, response1.getStatusCode());
        System.out.println(response1.prettyPrint());
    }

    //#Verify that respond body have curren date
    @Test
    public void test4(){
        Response response = given().
                baseUri(baseURI).
                basePath("/latest").
                queryParam("base", "GBP").get();

        String todaysDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("Today's date is: "+todaysDate);
        System.out.println(response.getBody().asString());

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains(todaysDate));
    }

    //Currency exchange rate for 2000
    @Test
    public void test5(){
        Response response = given().
                baseUri(baseURI).
                basePath("/history").
                queryParam("start_at","2000-01-01").
                queryParam("end_at", "2000-12-31").
                queryParam("base","USD").
                queryParam("symbols","EUR", "GBP", "JPY").get();
        System.out.println(response.prettyPrint());
    }

    /**
     * Given request parameter "base" is "USD"
     * When user sends request to "api.openrates.io"
     * Then response code should be 200
     * And response body must contain ""base": "USD""
     */

    @Test
    public void test6(){
        Response response = given().
                queryParam("base").
                baseUri(baseURI).
                basePath("/latest").
                queryParam("base","USD").
                get();

        String body = response.getBody().asString();
        assertEquals(200,response.getStatusCode());
        assertTrue(body.contains("\"base\":\"USD\""));
    }
























}
