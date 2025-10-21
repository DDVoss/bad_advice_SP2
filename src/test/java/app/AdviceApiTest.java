package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.impl.AdviceDAO;
import app.dtos.AdviceDTO;
import app.enums.Category;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class AdviceApiTest {

    private Javalin app;
    private static EntityManagerFactory emf;
    private static AdviceDAO adviceDAO;
    private AdviceDTO createdAdvice;
    private String authToken;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    void setUp()  {
        // Setup database
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        adviceDAO = AdviceDAO.getInstance(emf);

        // Start Javalin app
        app = ApplicationConfig.startServer(7070);
        RestAssured.baseURI = "http://localhost:7070";

        // Register user once and store token
        UserDTO userDTO = new UserDTO("testuser", "testpass");
        authToken = given()
                .contentType("application/json")
                .body(userDTO)
                .when()
                .post("/api/v1/auth/register")
                .then()
                .statusCode(201)
                .extract()
                .path("token");
    }

    @BeforeEach
    void setUpEachTest()  {
        // Clear database before each test
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Truncate advice table and reset identity
            em.createNativeQuery("TRUNCATE TABLE advice RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
        }

        // Create sample advice for testing
        AdviceDTO adviceDTO = new AdviceDTO(null, "Test advice", 5, Category.Finance);
        createdAdvice = adviceDAO.create(adviceDTO);
    }

    @Test
    void testReadAdvice()   {
        given()
                .when()
                .get("/api/v1/advice/1")
                .then()
                .statusCode(200)
                .body("adviceText", equalTo("Test advice"))
                .body("rating", equalTo(5))
                .body("category", equalTo("Finance"));
    }

    @Test
    void testCreateAdvice() {

        AdviceDTO newAdvice = new AdviceDTO(null, "New advice", 4, Category.Health);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(newAdvice)
                .when()
                .post("/api/v1/advice")
                .then()
                .statusCode(201)
                .body("adviceText", equalTo("New advice"))
                .body("rating", equalTo(4))
                .body("category", equalTo("Health"));
    }

    @Test
    void testUpdateAdvice() {

        // Create advice to update
        AdviceDTO originalAdvice = new AdviceDTO(null, "Original advice", 5, Category.Health);
        int adviceId = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(originalAdvice)
                .when()
                .post("/api/v1/advice")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Updated advice data
        AdviceDTO updatedAdvice = new AdviceDTO(null, "Updated advice", 3, Category.Relationships);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(updatedAdvice)
                .when()
                .put("/api/v1/advice/" + adviceId)
                .then()
                .statusCode(200)
                .body("adviceText", equalTo("Updated advice"))
                .body("rating", equalTo(3))
                .body("category", equalTo("Relationships"));
    }

    @Test
    void testDeleteAdvice() {

        // Create advice to delete
        AdviceDTO adviceToDelete = new AdviceDTO(null, "Advice to delete", 2, Category.Finance);
        int adviceId = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(adviceToDelete)
                .when()
                .post("/api/v1/advice")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Delete the advice
        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/api/v1/advice/" + adviceId)
                .then()
                .statusCode(204);

        // Verify deletion
        given()
                .when()
                .get("/api/v1/advice/" + adviceId)
                .then()
                .statusCode(400);
    }

    @Test
    void testReadAllAdvice() {

        // Create additional advice entries
        AdviceDTO advice1 = new AdviceDTO(null, "Advice 1", 6, Category.Health);
        AdviceDTO advice2 = new AdviceDTO(null, "Advice 2", 7, Category.Relationships);
        adviceDAO.create(advice1);
        adviceDAO.create(advice2);

        given()
                .when()
                .get("/api/v1/advice")
                .then()
                .statusCode(200)
                .body("size()", is(3)) // Including the one created in setUpEachTest
                .body("adviceText", hasItems("Test advice", "Advice 1", "Advice 2"));
    }

    @AfterAll
    void tearDown() {
        if (app != null) {
            ApplicationConfig.stopServer(app);
        }
        if (emf != null) {
            emf.close();
        }
    }
}
