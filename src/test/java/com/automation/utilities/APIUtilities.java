package com.automation.utilities;

import com.automation.pojos.Spartan;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtilities {
    private String URI = ConfigurationReader.getProperty("spartan.uri");
    /**
     *This method can POST new Spartan
     * @param spartan POJO
     * @return response object
     */
    public Response postSpartan(Spartan spartan){
        Response response = given().
                                contentType(ContentType.JSON).
                                basePath(URI).
                                body(spartan).
                            when().
                            post("/spartans");
        return response;
    }

    /**
     * This method can POST new Spartan
     * @param spartan as Map
     * @return
     */
    public Response postSpartan(Map<String, ?> spartan){
        RestAssured.baseURI = ConfigurationReader.getProperty("spartan.uri");
        Response response = given().
                contentType(ContentType.JSON).
                body(spartan).
                when().
                post("/spartans");
        return response;
    }

    /**
     * This method can POST new Spartan
     * @param filePath to the Spartan JSON File
     * @return
     */
    public Response postSpartan(String filePath){
        RestAssured.baseURI = ConfigurationReader.getProperty("spartan.uri");
        File file = new File(filePath);
        Response response = given().
                contentType(ContentType.JSON).
                body(file).
                when().
                post("/spartans");
        return response;
    }

    /**
     * Method to delete spartan
     * @param id
     * @return
     */
    public Response deleteSpartanById(int id){
        RestAssured.baseURI = ConfigurationReader.getProperty("spartan.uri");
        Response response = RestAssured.when().delete("/spartans/{id}", id);
        return response;
    }

    /**
     * Method that generates access token
     * @return bearer token
     */
    public static String getTokenForBookit(){
        Response response = given().
                queryParam("email", ConfigurationReader.getProperty("team.leader.email")).
                queryParam("password", ConfigurationReader.getProperty("team.leader.password")).
                when().
                get("/sign").prettyPeek();
        return response.jsonPath().getString("accessToken");
    }

    /**
     * Method that generates access token
     * @param role - type of user. Allowed types: student team leader, studen team member and teacher
     * @return bearer token
     */
    public static String getTokenForBookit(String role){
        String userName = "";
        String password = "";
        if (role.toLowerCase().contains("lead")) {
            userName = ConfigurationReader.getProperty("team.leader.email");
            password = ConfigurationReader.getProperty("team.leader.password");
        } else if (role.toLowerCase().contains("teacher")) {
            userName = ConfigurationReader.getProperty("teacher.email");
            password = ConfigurationReader.getProperty("teacher.password");
        } else if (role.toLowerCase().contains("member")) {
            userName = ConfigurationReader.getProperty("team.member.email");
            password = ConfigurationReader.getProperty("team.member.password");
        } else {
            throw new RuntimeException("Invalid user type!");
        }
        Response response = given().
                queryParam("email", userName).
                queryParam("password", password).
                when().
                get("/sign").prettyPeek();
        return response.jsonPath().getString("accessToken");
    }

}
