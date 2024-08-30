package com.herokuapp.restfulbooker;

import com.herokuapp.restfulbooker.BasePage;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class RestfulBooker {

    //5.1. Get a list of all books and verify that the request was completed correctly
    @Test
    @Tag("Tag1")
    public void getListOfAllBooks() {
        Response response = given().log().all()
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .contentType(ContentType.JSON).get()
                .then().log().all().extract().response();

        assertEquals(200, response.statusCode());
    }

    //5.2. From the resulting list, get a book by id (take a random id)
    @Test
    @Tag("Tag1")
    public void getBookIdFromList() {
        Response response = RestAssured.get("https://restful-booker.herokuapp.com/booking");
        //response.print();

        assertEquals(200, response.getStatusCode());

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        assertFalse(bookingIds.isEmpty());

        Random r = new Random();
        int randomId = r.nextInt(50);

        Response responseBookId = RestAssured.get(
                "https://restful-booker.herokuapp.com/booking/" + bookingIds.get(randomId));
        responseBookId.print();
        System.out.println(randomId);
    }

    //5.3. Create booking with valid data
    @Test
    public void createValidBooking() {

        BasePage pojo = new BasePage();
        Response response = pojo.createValidBooking();

        assertEquals(200, response.getStatusCode());

        String expectedFirstName = "Caroline";
        String expectedLastName = "Mendez";
        int expectedTotalPrice = 50;
        boolean expectedDepositPaid = false;
        String expectedCheckInDate = "2024-01-01";
        String expectedCheckOutDate = "2024-06-06";
        String expectedAdditionalNeeds = "Lunch";

        BasePage bp = new BasePage();
        bp.assertAllBookingFields(response,
                                  expectedFirstName,
                                  expectedLastName,
                                  expectedTotalPrice,
                                  expectedDepositPaid,
                                  expectedCheckInDate,
                                  expectedCheckOutDate,
                                  expectedAdditionalNeeds);
    }


    //5.4. Create booking with invalid data
    @Test
    public void createInvalidBooking() {
        JSONObject body = new JSONObject();
        body.put("firstname", 600);

        Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(
                "https://restful-booker.herokuapp.com/booking");

        assertEquals(500, response.getStatusCode());
    }

    //5.5. Create booking with firstname =FirsrNameBook1
    @Test
    public void createBookingFirstName() {
        JSONObject body = new JSONObject();
        body.put("firstname", "FirsrNameBook1");

        Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(
                "https://restful-booker.herokuapp.com/booking");
        response.print();

        assertEquals(500, response.getStatusCode());

        //String actualFirstName = response.jsonPath().getString("booking.firstname");
        //String expectedFirstName = "Caroline";
        //
        //assertAll("book",
        //          () -> assertEquals(actualFirstName,
        //                             expectedFirstName,
        //                             "The expected last name was " + expectedFirstName + " but the actual name was " +
        //                             actualFirstName));
    }

    //5.6. Update booking using valid data
    @Test
    public void updateValidBooking() {
        BasePage pojo = new BasePage();
        Response response = pojo.createValidBooking();
        response.print();

        Response responseUpdate = pojo.updateValidBooking(response.jsonPath()
                                                                  .getInt("bookingid"));
        responseUpdate.print();
    }


}
