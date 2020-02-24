package com.automation.tests.day7;

import com.automation.pojos.Student;
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

public class PreschoolTests {

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("school.uri");
    }

    @Test
    @DisplayName("Get student with id 3681 and convert payload into POJO")
    public void test1(){
        Response response = given().
                                accept(ContentType.JSON).
                                pathParam("id", 3681).
                            when().
                                get("/student/{id}").prettyPeek();
        Student student = response.jsonPath().getObject("students[0]", Student.class);
        System.out.println(student);

        assertEquals(3681, student.getStudentId());
        assertEquals("34345645", student.getContact().getPhone());
    }
}
