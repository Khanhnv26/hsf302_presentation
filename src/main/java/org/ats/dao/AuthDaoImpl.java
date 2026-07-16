package org.ats.dao;

import lombok.RequiredArgsConstructor;
import org.ats.dto.UserRequest;
import org.ats.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AuthDaoImpl implements AuthDao {
    private final SessionFactory sessionFactory;

    @Override
    @Transactional
    public User login(UserRequest userRequest) {
        Session session = sessionFactory.openSession(); // Connect to DB
        Query<User> query = session.createNamedQuery("login", User.class);

        query.setParameter("email", userRequest.getEmail());
        query.setParameter("password", userRequest.getPassword());
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public User register(User user) {
        Session session = sessionFactory.openSession();
        session.persist(user);
        return user;
    }
}
