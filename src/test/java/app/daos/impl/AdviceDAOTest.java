package app.daos.impl;

import app.config.HibernateConfig;
import app.dtos.AdviceDTO;
import app.enums.Category;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class AdviceDAOTest {

    private static AdviceDAO adviceDAO;
    private static EntityManagerFactory emf;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    void setUp() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        adviceDAO = AdviceDAO.getInstance(emf);
    }

    @BeforeEach
    void cleanDatabase() {
        // Clean database before each test
        try (var em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            em.createQuery("DELETE FROM Advice").executeUpdate();
            tx.commit();
        }
    }


    @Test
    void read() {
        // Arrange
        AdviceDTO adviceToCreate = new AdviceDTO(null, "Test Advice", 5, Category.Health);
        AdviceDTO createdAdvice = adviceDAO.create(adviceToCreate);

        // Act
        AdviceDTO retrievedAdvice = adviceDAO.read(createdAdvice.getId());

        // Assert
        assertEquals("Test Advice", retrievedAdvice.getAdviceText());
    }

    @Test
    void readAll() {
        // Arrange
        AdviceDTO advice1 = new AdviceDTO(null, "Advice 1", 4, Category.Finance);
        AdviceDTO advice2 = new AdviceDTO(null, "Advice 2", 5, Category.Health);
        adviceDAO.create(advice1);
        adviceDAO.create(advice2);

        // Act
        var allAdvices = adviceDAO.readAll();

        // Assert
        assertEquals(2, allAdvices.size());
    }

    @Test
    void create() {
        // Arrange
        AdviceDTO adviceToCreate = new AdviceDTO(null, "New Advice", 3, Category.Career);

        // Act
        AdviceDTO createdAdvice = adviceDAO.create(adviceToCreate);

        // Assert
        assertNotNull(createdAdvice.getId());
        assertEquals("New Advice", createdAdvice.getAdviceText());
        assertEquals(3, createdAdvice.getRating());
        assertEquals(Category.Career, createdAdvice.getCategory());
    }

    @Test
    void createMultiple() {
        // Arrange
        AdviceDTO[] sampleAdvices = {
                new AdviceDTO(null, "Advice 1", 4, Category.Finance),
                new AdviceDTO(null, "Advice 2", 5, Category.Health),
                new AdviceDTO(null, "Advice 3", 2, Category.Career)
        };

        // Act
        List<AdviceDTO> createdAdvices = adviceDAO.createMultiple(sampleAdvices);

        // Assert
        assertEquals(3, createdAdvices.size());
    }

    @Test
    void update() {
        // Arrange
        AdviceDTO adviceToCreate = new AdviceDTO(null, "Old Advice", 2, Category.Finance);
        AdviceDTO createdAdvice = adviceDAO.create(adviceToCreate);

        AdviceDTO adviceToUpdate = new AdviceDTO(null, "Updated Advice", 8, Category.Health);

        // Act
        AdviceDTO updatedAdvice = adviceDAO.update(createdAdvice.getId(), adviceToUpdate);

        // Assert
        assertEquals(createdAdvice.getId(), updatedAdvice.getId());
        assertEquals("Updated Advice", updatedAdvice.getAdviceText());
        assertEquals(8, updatedAdvice.getRating());
        assertEquals(Category.Health, updatedAdvice.getCategory());
    }

    @Test
    void delete() {
        // Arrange
        AdviceDTO adviceToCreate = new AdviceDTO(null, "Advice to be deleted", 6, Category.Career);
        AdviceDTO createdAdvice = adviceDAO.create(adviceToCreate);

        // Act
        adviceDAO.delete(createdAdvice.getId());

        // Assert
        AdviceDTO deletedAdvice = adviceDAO.read(createdAdvice.getId());
        assertNull(deletedAdvice);
    }

    @AfterAll
    void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }
}