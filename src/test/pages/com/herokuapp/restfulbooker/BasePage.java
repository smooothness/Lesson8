package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

public class BasePage {

    public BasePage() {

    }

    public Response createValidBooking() {
        JSONObject body = new JSONObject();
        body.put("firstname", "Caroline");
        body.put("lastname", "Mendez");
        body.put("totalprice", 50);
        body.put("depositpaid", false);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2024-01-01");
        bookingdates.put("checkout", "2024-06-06");
        body.put("bookingdates", bookingdates);

        body.put("additionalneeds", "Lunch");

        return RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(
                "https://restful-booker.herokuapp.com/booking");
    }

    public Response updateValidBooking(int bookingId) {
        JSONObject body = new JSONObject();
        body.put("firstname", "Tom");
        body.put("lastname", "Smith");
        body.put("totalprice", 150);
        body.put("depositpaid", true);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2024-01-01");
        bookingdates.put("checkout", "2024-06-06");
        body.put("bookingdates", bookingdates);

        body.put("additionalneeds", "Dinner");

        return RestAssured.given().auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).body(
                body.toString()).put(
                "https://restful-booker.herokuapp.com/booking/" + bookingId);
    }

    public void assertAllBookingFields(Response response,
                                       String expectedFirstName,
                                       String expectedLastName,
                                       int expectedTotalPrice,
                                       Boolean expectedDepositPaid,
                                       String expectedCheckInDate,
                                       String expectedCheckOutDate,
                                       String expectedAdditionalNeeds) {

        String actualFirstName = response.jsonPath().getString("booking.firstname");
        String actualLastName = response.jsonPath().getString("booking.lastname");
        int actualTotalPrice = response.jsonPath().getInt("booking.totalprice");
        boolean actualDepositPaid = response.jsonPath().getBoolean("booking.depositpaid");
        String actualCheckInDate = response.jsonPath().getString("booking.bookingdates.checkin");
        String actualCheckOutDate = response.jsonPath().getString("booking.bookingdates.checkout");
        String actualAdditionalNeeds = response.jsonPath().getString("booking.additionalneeds");

        assertAll("book",
                  () -> assertEquals(actualFirstName,
                                     expectedFirstName,
                                     "The expected last name was " + expectedFirstName + " but the actual name was " +
                                     actualFirstName),
                  () -> assertEquals(actualLastName,
                                     expectedLastName,
                                     "The expected first name was " + expectedLastName + " but the actual name was " +
                                     actualLastName),
                  () -> assertEquals(actualTotalPrice,
                                     expectedTotalPrice,
                                     "The expected total price was " + expectedTotalPrice +
                                     " but the actual total price was " +
                                     actualTotalPrice),
                  () -> assertFalse(actualDepositPaid,
                                    "The deposit was expected to be " + expectedDepositPaid +
                                    " but the actual total price was " +
                                    actualDepositPaid),
                  () -> assertEquals(actualCheckInDate,
                                     expectedCheckInDate,
                                     "The expected check in date was " + expectedCheckInDate +
                                     " but the actual check in date was " +
                                     actualCheckInDate),
                  () -> assertEquals(actualCheckOutDate,
                                     expectedCheckOutDate,
                                     "The expected check out date was " + expectedCheckOutDate +
                                     " but the actual check out date was " +
                                     actualCheckOutDate),
                  () -> assertEquals(actualAdditionalNeeds,
                                     expectedAdditionalNeeds,
                                     "The expected additional needs were " + expectedAdditionalNeeds +
                                     " but the actual additional needs were " +
                                     actualAdditionalNeeds));
    }

}
