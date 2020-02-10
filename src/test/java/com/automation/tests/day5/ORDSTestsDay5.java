package com.automation.tests.day5;
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
public class ORDSTestsDay5 {
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    /**
     * WARMUP
     * given path parameter is "/employees"
     * when user makes get request
     * then user verifies that status code is 200
     * and user verifies that average salary is grater than $5000
     */

    @Test
    public void test1(){
        Response response = given().
                                accept(ContentType.JSON).
                            when().
                                get("/employees");

        response.then().assertThat().statusCode(200);

        List<Integer> salaries = response.jsonPath().getList("items.salary");

        int totalsalaries = 0;
        System.out.println(salaries);
        for (int salary : salaries ){
            totalsalaries += salary;
        }
        int avgSalaries = totalsalaries/salaries.size();
        System.out.println(totalsalaries);
        System.out.println(avgSalaries);
        assertTrue(avgSalaries>5000);

    }




















}
