package com.automation.tests.day8;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;


public class BadSSLTest {

    @BeforeAll
    public static void setup(){
        baseURI = "https://untrusted-root.badssl.com/";
    }

    @Test
    @DisplayName("Access web site with bad SSL (MUST FAIL)")
    public void test1(){
        //unable to find valid certification path to requested target
        //no valid SSL - no handshake
        //if web site cannot provide valid certificate
        //secured connection is not possible
        //client will reject to exchange information by default
        Response response = get().prettyPeek();
        System.out.println(response.statusCode());
    }

    @Test
    @DisplayName("Access Website with bad SSL (Solution)")
    public void test2(){
        //.relaxedHTTPSValidation() - ignores SSL issues
        // * Use relaxed HTTP validation with SSLContext protocol SSL.
        // This means that you'll trust all hosts regardless if the SSL certificate is invalid.
        Response response = given().relaxedHTTPSValidation().get().prettyPeek();
        System.out.println(response.statusCode());
        assertEquals(200, response.getStatusCode());
    }
}
