package services;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.reset;

public class BookingTest {

    String stringToken;
    String stringBookingId;

    @BeforeClass
    public void postCreateToken(){
        String postData= "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        Response res= given()
                .body(postData)
                .contentType(ContentType.JSON)
                .log().all().
                when()
                .post("https://restful-booker.herokuapp.com/auth").
                then()
                .statusCode(200)
                .log().all().extract().response();

        stringToken= res.jsonPath().getString("token");
    }

    @Test(priority = 1)
    public void postCreateBooking(){
        String postBooking="{\n" +
                "    \"firstname\" : \"Elif\",\n" +
                "    \"lastname\" : \"Can\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-02-01\",\n" +
                "        \"checkout\" : \"2022-02-02\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Response resBooking= given()
                .body(postBooking)
                .contentType(ContentType.JSON)
                .log().all().
                when()
                .post("https://restful-booker.herokuapp.com/booking").
                then()
                .statusCode(200)
                .log().all().extract().response();

        stringBookingId= resBooking.jsonPath().getString("bookingid");
    }

    @Test (priority = 2)
    public void getBooking(){
        given()
                .log().all().
                when()
                .get("https://restful-booker.herokuapp.com/booking/" + stringBookingId).
                then()
                .statusCode(200)
                .log().all();
    }


    @DataProvider(name="dataProvider")
    public Object[][] dataProvider(){
        return new Object[][]{
                {2},
                {3}
        };
    }

    @Test (dataProvider = "dataProvider", priority = 3)
    public void getBookingIds(int bookingId){
        given()
                .log().all().
                when()
                .get("https://restful-booker.herokuapp.com/booking/" + bookingId).
                then()
                .statusCode(200)
                .log().all();
    }

    @Test (priority = 4)
    public void updateBooking(){
        String updateBooking="{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brownnn\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-02-01\",\n" +
                "        \"checkout\" : \"2022-02-02\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"SingleRoom\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(updateBooking)
                .header("Cookie","token=" + stringToken)
                .log().all().
                when()
                .put("https://restful-booker.herokuapp.com/booking/" + stringBookingId).
                then()
                .statusCode(200)
                .log().all();
    }

    @AfterClass
    public void deleteBooking(){

        given()
                .log().all()
                .header("Cookie","token=" + stringToken).
                when()
                .delete("https://restful-booker.herokuapp.com/booking/" + stringBookingId).
                then()
                .statusCode(201)
                .log().all();
    }


}
