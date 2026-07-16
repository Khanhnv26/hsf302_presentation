package org.ats.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DbContext {
    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;

    public static EntityManager getEntityManager() {
        entityManager = Persistence.createEntityManagerFactory("ats-jpa-unit").createEntityManager();

        return entityManager;
    }
}
