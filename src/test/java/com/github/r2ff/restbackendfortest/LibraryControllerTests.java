package com.github.r2ff.restbackendfortest;

import com.github.r2ff.restbackendfortest.domain.BookInfo;
import io.restassured.http.ContentType;
import lombok.Builder;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Builder
public class LibraryControllerTests {

    @Test
    void addBookTest() {

        given()
                .spec(Specs.request)
                .contentType(ContentType.JSON)
                .body("{" +
                        "    \"book_name\": \"Winter\"," +
                        "    \"book_author\": \"King\"" +
                        "}")
                .when()
                .post("book/add")
                .then()
                .log().body()
                .statusCode(200)
                .body("book_name", is("Winter King"))
                .body("message", is("Successful added"));

    }

    @Test
    void addExistingBookTest() {

        given()
                .spec(Specs.request)
                .contentType(ContentType.JSON)
                .body("{" +
                        "    \"book_name\": \"Search\"," +
                        "    \"book_author\": \"Brin\"" +
                        "}")
                .when()
                .post("book/add")
                .then()
                .log().body()
                .statusCode(400)
                .body("error", is("Bad Request"));

    }

    @Test
    void getAllBookTest() {

        BookInfo[] bookInfoBefore = given()
                .spec(Specs.request)
                .contentType(ContentType.JSON)
                .when()
                .get("books/getall")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response()
                .as(BookInfo[].class);

        given()
                .spec(Specs.request)
                .contentType(ContentType.JSON)
                .body("{" +
                        "    \"book_name\": \"Summer\"," +
                        "    \"book_author\": \"King\"" +
                        "}")
                .when()
                .post("book/add")
                .then()
                .log().body()
                .statusCode(200)
                .body("book_name", is("Summer King"))
                .body("message", is("Successful added"));

        BookInfo[] bookInfoAfter = given()
                .spec(Specs.request)
                .contentType(ContentType.JSON)
                .when()
                .get("books/getall")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response()
                .as(BookInfo[].class);

        assertEquals(bookInfoBefore.length + 1, bookInfoAfter.length);
    }

    @Test
    void getBookByAuthorTest() {

        given()
                .spec(Specs.request)
                .contentType(ContentType.JSON)
                .body("{" +
                        "    \"book_author\": \"King\"" +
                        "}")
                .when()
                .post("books/byauthor")
                .then()
                .log().body()
                .statusCode(200)
                .body("book_author", hasItem("King"));
    }

    @Test
    void getBookByAuthorNegativeTest() {

        BookInfo[] bookInfoByAuthor = given()
                .spec(Specs.request)
                .contentType(ContentType.JSON)
                .body("{" +
                        "    \"book_author\": \"Kong\"" +
                        "}")
                .when()
                .post("books/byauthor")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .body().as(BookInfo[].class);

        assertEquals(0, bookInfoByAuthor.length);
    }

}
