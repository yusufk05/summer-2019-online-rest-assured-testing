package com.automation.tests.day8;

import com.automation.utilities.ConfigurationReader;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class BearerTokenTestWithBookit {
    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("bookit.qa1");
    }

    //  /api/room
    @Test
    @Description("Get List of all rooms")
    public void test1(){
        Response response = given().
                                header("Authorization", getToken()).
                            when().
                                get("/api/rooms").prettyPeek();
    }



    /**
     * Method that generates access token
     * @return bearer token
     */
    public String getToken(){
        Response response = given().
                                queryParam("email", ConfigurationReader.getProperty("team.leader.email")).
                                queryParam("password", ConfigurationReader.getProperty("team.leader.password")).
                            when().
                                get("/sign").prettyPeek();
         return response.jsonPath().getString("accessToken");
    }

}
