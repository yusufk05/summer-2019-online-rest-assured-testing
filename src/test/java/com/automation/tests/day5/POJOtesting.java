package com.automation.tests.day5;

import com.automation.pojos.Job;
import com.automation.pojos.Location;
import com.automation.utilities.ConfigurationReader;
import com.google.gson.Gson;
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

public class POJOtesting {
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    @Test
    @DisplayName("Get job info from json and convert it into POJO")
    public void test1(){
        Response response = given().
                                accept(ContentType.JSON).
                            when().
                                get("/jobs");

        JsonPath jsonPath = response.jsonPath();
        //this is deserialization
        // from JSON to POJO (java object)
        Job job = jsonPath.getObject("items[0]", Job.class);

        System.out.println(job);
    }

    @Test
    @DisplayName("Convert from POJO to JSON")
    public void test2(){
        Job sdet = new Job("SDET", " Software Development Engineer in Test", 5000, 20000);

        Gson gson = new Gson();
        String json = gson.toJson(sdet); // convert POJO to JSON => serialization

        System.out.println("JSON file: " + json);
        System.out.println("From toString(): " + sdet);
    }

    @Test
    @DisplayName("Convert JSON into collection POJO's")
    public void test3(){
        Response response = given().
                                accept(ContentType.JSON).
                            when().
                                get("/jobs");

        JsonPath jsonPath = response.jsonPath();

        List<Job> jobs = jsonPath.getList("items", Job.class);
        System.out.println(jobs);
        //for demo
        for (Job job : jobs){
            System.out.println(job);
        }
    }
/*
#Task
Create POJO for Location:
public class Location{
}
Write 2 tests:
#1 get single POJO of Location class
#2 get collection of POJO’s.
Same thing like we did with Job class
* follow java naming conventions!
*/
    @Test
    @DisplayName("Convert from JSON to Location POJO")
    public void test4(){
        Response response = given().
                                accept(ContentType.JSON).
                                pathParam("location_id", 2000).
                            when().
                                get("/locations/{location_id}");
        JsonPath jsonPath = response.jsonPath();
        Location location = jsonPath.getObject("", Location.class);
        System.out.println(location);

        Response response2 = given().
                accept(ContentType.JSON).
                when().
                get("/locations");


        List<Location> locations = response2.jsonPath().getList("items", Location.class);
        for (Location locs : locations){
            System.out.println(locs);
        }
    }



}
