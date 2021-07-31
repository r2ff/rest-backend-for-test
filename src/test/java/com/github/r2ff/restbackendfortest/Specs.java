package com.github.r2ff.restbackendfortest;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;

public class Specs {

    public static RequestSpecification request = with()
            .baseUri("http://localhost:8080")
            .log().all();
}
