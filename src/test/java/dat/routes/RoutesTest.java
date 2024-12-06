package dat.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;

import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.is;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoutesTest {
/*
    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final HotelDAO hotelDAO = new HotelDAO(emf);
    private static final RoomDAO roomDAO = new RoomDAO(emf);
    private static final String BASE_URL = "http://localhost:7070/api";

    private static HotelDTO h1, h2, h3;
    private static RoomDTO r1, r2, r3,r4;

    @BeforeAll
    void setUpAll(){
        app = ApplicationConfig.startServer(7070);
        HibernateConfig.setTest(true);
    }
    @BeforeEach
    void setUp() {
        BigDecimal b1 = new BigDecimal(100.00);
        BigDecimal b2 = new BigDecimal(200.00);
        BigDecimal b3 = new BigDecimal(300.00);
        BigDecimal b4 = new BigDecimal(2000.00);
        Room room = new Room(1,b1,SINGLE);
        Room room1 = new Room(2,b2,SINGLE);
        Room room2 = new Room(3,b3,DOUBLE);
        Room room3 = new Room(4,b4,SUITE);

        h1 = new HotelDTO("Calforina Hotel","Cali vej 1",LUXURY);
        h2 = new HotelDTO("Lyngby BK Hotel","Lyngbyvej 62",BUDGET);
        h3 = new HotelDTO("Hotel 5","Hotel vej 12",STANDARD);

        r1 = new RoomDTO(room);
        r2 = new RoomDTO(room1);
        r3 = new RoomDTO(room2);
        r4 = new RoomDTO(room3);

        h1 = hotelDAO.create(h1);
        h2 = hotelDAO.create(h2);
        h3 = hotelDAO.create(h3);

        roomDAO.create(r1);
        roomDAO.create(r2);
        roomDAO.create(r3);
        roomDAO.create(r4);

        roomDAO.addRoomToHotel(h1.getId(),r1);
        roomDAO.addRoomToHotel(h1.getId(),r2);
        roomDAO.addRoomToHotel(h2.getId(),r3);
        roomDAO.addRoomToHotel(h3.getId(),r4);


    }

    @AfterEach
    void tearDown() {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room").executeUpdate();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    void tearDownAll(){
        app.stop();
    }

    @Test
    void getHotels() {
        given()
                .when()
                .get(BASE_URL + "/hotels")
                .then()
                .statusCode(200)
                .body("size()", is(3));

    }

    @Test
    void getHotelById() {
        given()
                .when()
                .get(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .statusCode(200)
                .body("hotelName", is(h1.getHotelName()));

    }

    @Test
    void createHotel() {
        HotelDTO hotel = new HotelDTO("New Hotel", "New Hotel vej 1", LUXURY);
        given()
                .contentType("application/json")
                .body(hotel)
                .when()
                .post(BASE_URL + "/hotels")
                .then()
                .statusCode(201);
        given()
                .when()
                .get(BASE_URL + "/hotels")
                .then()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    void updateHotel() {
        HotelDTO hotel = new HotelDTO("Updated Hotel", "Updated Hotel vej 1", LUXURY);
        given()
                .contentType("application/json")
                .body(hotel)
                .when()
                .put(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .statusCode(200);
        given()
                .when()
                .get(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .statusCode(200)
                .body("hotelName", is("Updated Hotel"));
    }

    @Test
    void deleteHotel() {
        given()
                .when()
                .delete(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .statusCode(204);
        given()
                .when()
                .get(BASE_URL + "/hotels")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    void getRooms() {
        given()
                .when()
                .get(BASE_URL + "/rooms")
                .then()
                .statusCode(200)
                .body("size()", is(4));
    }

 */
}
