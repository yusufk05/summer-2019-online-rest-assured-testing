package com.automation.tests.day5;
import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import  static org.hamcrest.Matchers.*;

public class SchoolTest {

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("school.uri");
    }

    @Test
    @DisplayName("Create and Delete Student")
    public void test1(){

        String json = "{\n" +
                "  \"admissionNo\": \"1234\",\n" +
                "  \"batch\": 12,\n" +
                "  \"birthDate\": \"01/01/1890\",\n" +
                "  \"company\": {\n" +
                "    \"address\": {\n" +
                "      \"city\": \"McLean\",\n" +
                "      \"state\": \"Virginia\",\n" +
                "      \"street\": \"7925 Jones Branch Dr\",\n" +
                "      \"zipCode\": 22102\n" +
                "    },\n" +
                "    \"companyName\": \"Cybertek\",\n" +
                "    \"startDate\": \"02/02/2020\",\n" +
                "    \"title\": \"SDET\"\n" +
                "  },\n" +
                "  \"contact\": {\n" +
                "    \"emailAddress\": \"james_bond@gmail.com\",\n" +
                "    \"phone\": \"240-123-1231\",\n" +
                "    \"premanentAddress\": \"7925 Jones Branch Dr\"\n" +
                "  },\n" +
                "  \"firstName\": \"James\",\n" +
                "  \"gender\": \"Males\",\n" +
                "  \"joinDate\": \"01/01/3321\",\n" +
                "  \"lastName\": \"Bond\",\n" +
                "  \"major\": \"CS\",\n" +
                "  \"password\": \"1234\",\n" +
                "  \"section\": \"101010\",\n" +
                "  \"subject\": \"Math\"\n" +
                "}";

        //create student
        Response response = given().
                                contentType(ContentType.JSON).
                            body(json).
                                post("student/create").prettyPeek();

        int studentId = response.jsonPath().getInt("studentId");
        System.out.println("=========================");
        //delete student
        Response response2 = given().
                                accept(ContentType.JSON).
                            when().
                                delete("/student/delete/{id}", studentId).
                            prettyPeek();
    }


    @Test
    @DisplayName("Delete Student")
    public void test2(){
        Response response2 = given().
                accept(ContentType.JSON).
                when().
                delete("/student/delete/{id}", 3406);
    }

    @Test
    @DisplayName("Create new Student and read data from external json file")
    public void test3(){

    }
}
