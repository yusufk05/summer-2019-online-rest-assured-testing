package com.automation.tests.day2;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;


public class ORDSTests {
    //address of ORDS web service, that is running on AWS ec2.
    //data is coming from SQL Oracle data base to this web service
    //during back-end testing with SQL developer and JDBC API
    //we were accessing data base directly
    //now, we gonna access web service
    private String baseURI = "http://ec2-54-85-201-7.compute-1.amazonaws.com:1000/ords/hr";

    //verify that status code is 200
    @Test
    public void test1(){
        Response response = given().get(baseURI+"/employees");

        System.out.println(response.getBody().asString());
        assertEquals(200,response.getStatusCode());
    }

}
