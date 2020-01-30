package com.automation.tests.day2;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class MetaWeatherTest {
    private String baseURI = "https://www.metaweather.com/api/";

    /**
     * /api/location/search/?query=san
     * /api/location/search/?query=london
     * /api/location/search/?lattlong=36.96,-122.02
     * /api/location/search/?lattlong=50.068,-5.316
     *
     *  "title": "Istanbul",
     *         "location_type": "City",
     *         "woeid": 2344116,
     *         "latt_long": "41.040852,28.986179"
     */

    @Test
    public void test1(){
        Response response = given().
            baseUri(baseURI).
            basePath("location/search/").
            queryParam("query", "ist").
            get();

        assertEquals(200,response.statusCode());
        System.out.println(response.prettyPrint());

    }

    @Test
    public void test2(){
        Response response = given().
                pathParam("woeid", "2344116").
                baseUri(baseURI).
                basePath("location/{woeid}").
                get();

        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }



}
