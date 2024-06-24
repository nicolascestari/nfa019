package org.calendrier;

import org.calendrier.dao.ActiviteDao;
import org.calendrier.model.Activite;
import org.calendrier.database.DatabaseManager;
import org.junit.jupiter.api.*;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActiviteDaoTest {

    private ActiviteDao activiteDao;
    private EntityManager entityManager;

    @BeforeAll
    public void setUp() {
        // Initialize DatabaseManager and EntityManager
        DatabaseManager.getEntityManagerFactory();
        entityManager = DatabaseManager.getEntityManagerFactory().createEntityManager();
        activiteDao = new ActiviteDao();
    }

    @AfterAll
    public void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
        DatabaseManager.getEntityManagerFactory().close();
    }

    @Test
    public void testCreateActivite() {
        Activite activite = new Activite("2024-06-24", "Test Commentaire", "Test Statut");
        activiteDao.saveActivite(activite);

        Activite retrievedActivite = entityManager.find(Activite.class, activite.getId());
        assertNotNull(retrievedActivite, "L'activité doit être créée");
        assertEquals("Test Commentaire", retrievedActivite.getCommentaire());
        assertEquals("Test Statut", retrievedActivite.getStatut());
    }

    @Test
    public void testUpdateActivite() {
        Activite activite = new Activite("2024-06-24", "Initial Commentaire", "Initial Statut");
        activiteDao.saveActivite(activite);

        activite.setCommentaire("Updated Commentaire");
        activite.setStatut("Updated Statut");
        activiteDao.updateActivite(activite);

        Activite retrievedActivite = entityManager.find(Activite.class, activite.getId());
        assertNotNull(retrievedActivite, "L'activité doit être mise à jour");
        assertEquals("Updated Commentaire", retrievedActivite.getCommentaire());
        assertEquals("Updated Statut", retrievedActivite.getStatut());
    }

    @Test
    public void testDeleteActivite() {
        Activite activite = new Activite("2024-06-24", "To Delete Commentaire", "To Delete Statut");
        activiteDao.saveActivite(activite);

        activiteDao.deleteActivite(activite.getId());

        Activite retrievedActivite = entityManager.find(Activite.class, activite.getId());
        assertNull(retrievedActivite, "L'activité doit être supprimée");
    }
}
