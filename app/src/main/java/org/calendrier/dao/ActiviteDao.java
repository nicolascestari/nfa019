package org.calendrier.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import org.calendrier.model.Activite;
import org.calendrier.database.DatabaseManager;

import java.util.List;

public class ActiviteDao {

    public void saveActivite(Activite activite) {
        EntityManager em = DatabaseManager.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(activite);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void updateActivite(Activite activite) {
        EntityManager em = DatabaseManager.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(activite);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Activite getActiviteById(int id) {
        EntityManager em = DatabaseManager.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Activite.class, id);
        } finally {
            em.close();
        }
    }

    public List<Activite> getAllActivites() {
        EntityManager em = DatabaseManager.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Activite> query = em.createQuery("from Activite", Activite.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void deleteActivite(int id) {
        EntityManager em = DatabaseManager.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            Activite activite = em.find(Activite.class, id);
            if (activite != null) {
                em.remove(activite);
                System.out.println("Activité supprimée avec succès");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
