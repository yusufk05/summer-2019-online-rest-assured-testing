package com.automation.tests.day3;

import com.automation.utilities.ConfigurationReader;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import  static org.hamcrest.Matchers.*;

public class ORDSTestDay3 {

    @BeforeAll
    public static void Setup(){
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    //accept("application/json") shortcut for header("Accept", "Application/json")
    @Test
    public void test1(){
        given().
                accept("application/json").
                get("/employees").
        then().
                assertThat().statusCode(200).
        and().
                assertThat().contentType("application/json").
                log().all(true);
    }

    //path parameter = to point on specific resource /employee/:id/ - id it's a path parameter
    //query parameter = to filter results or describe new resource:
    // POST /users?name=James&age=60&job-title=SDET
    //or to filter GET /employee?name=Jamal get all employees with name Jamal
    @Test
    public void test2(){
        given().
                accept("application/json").
                pathParam("id",100).
        when().get("/employees/{id}").
        then().assertThat().statusCode(200).
        and().assertThat().body("employee_id", is(100),
                "department_id", is(90),
                                        "job_id", is("AD_PRES"))
        .log().all(true);
    }

    /**
     * given path parameter is "/regions/{id}"
     * when user makes get request
     * and region id is equals to 1
     * then assert that status code is 200
     * and assert that region name is Europe
     */

    @Test
    public void test3(){
        given().
                accept("application/json").
                pathParam("id",1).
        when().get("/regions/{id}").
                then().assertThat().statusCode(200).
                assertThat().body("region_name", is("Europe")).
                time(lessThan(5L), TimeUnit.SECONDS).
        log().body(true);
    }

    @Test
    public void Test4() {
        JsonPath json = given().
                accept("application/json").
                when().
                get("/employees").
                thenReturn().jsonPath();

        //items[employee1, employee2, employee3] | items[0] = employee1.first_name = Steven

        String nameOfFirstEmployee = json.getString("items[0].first_name");
        String nameOfLastEmployee = json.getString("items[-1].first_name"); //-1 - last index

        System.out.println("First employee name: " + nameOfFirstEmployee);
        System.out.println("Last employee name: " + nameOfLastEmployee);
        //in JSON, employee looks like object that consists of params and their values
        //we can parse that json object and store in the map.
        Map<String, ?> firstEmployee = json.get("items[0]"); // we put ? because it can be also not String
        System.out.println(firstEmployee);

        //since firstEmployee it's a map (key-value pair, we can iterate through it by using Entry. entry represent one key=value pair)
        // put ? as a value (Map<String, ?>), because there are values of different data type: string, integer, etc..
        //if you put String as value, you might get some casting exception that cannot convert from integer(or something else) to string
        for (Map.Entry<String, ?> entry : firstEmployee.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }

        List<String> lastNames = json.get("items.last_name");
        for (String str : lastNames) {
            System.out.println("last name: " + str);
        }
    }

    //write a code to
    //get info from /countries as List<Map<String, ?>>
    //prettyPrint() - print json/xml/html in nice format and returns string, thus we cannot retrieve jsonpath without extraction...
    //prettyPeek() does same job, but return Response object, and from that object we can get json path.
    @Test
    public void test5(){
        JsonPath json = given().
                            accept("application/json").
                            when().get("/countries").prettyPeek().jsonPath();

        List<HashMap<String, ?>> allCountries = json.get("items");
        System.out.println(allCountries);
        // when we read data from json response, values are not only strings
        //so if we are not sure that all values will have same data type
        //we can put ?
        for(HashMap<String, ?> map: allCountries){
            System.out.println(map);
        }
    }

    @Test
    public void test6(){
        JsonPath json = given().
                accept("application/json").
                when().get("/employees").thenReturn().jsonPath();

       List<Integer> salaries = json.get("items.salary");
        Collections.sort(salaries);
        System.out.println(salaries);
        /**
         *         List<Integer> salaries = given().
         *                                         accept("application/json").
         *                                  when().
         *                                         get("/employees").
         *                                  thenReturn().jsonPath().get("items.salary");
         *         Collections.sort(salaries, Collections.reverseOrder());//sort from a to z, 0-9
         *         System.out.println(salaries);
         */
    }

    //get collection of phone numbers, from employees
    //and replace all dots "." in every phone number with dash "-"
    @Test
    public void test7(){
        List<String> phoneNumbers = given().
                accept("application/json").
                when().get("/employees").
                thenReturn().jsonPath().get("items.phone_number");

        phoneNumbers.replaceAll(phone -> phone.replace(".", "-"));
        System.out.println(phoneNumbers);
    }

    /** ####TASK#####
     *  Given accept type as JSON
     *  And path parameter is id with value 1700
     *  When user sends get request to /locations
     *  Then user verifies that status code is 200
     *  And user verifies following json path information:
     *      |location_id|postal_code|city   |state_province|
     *      |1700       |98199      |Seattle|Washington    |
     *
     */
    @Test
    public void test8(){
        given().
                accept("application/json").
                pathParam("id",1700).
                when().get("/locations/{id}").
                then().assertThat().statusCode(200).
                and().assertThat().body("location_id", is(1700),
                                        "postal_code", is("98199"),
                                        "city", is("Seattle"),
                                        "state_province", is("Washington")).
        log().body(true);
    }

}
