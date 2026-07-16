package org.ats.dao;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.ats.entities.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // IoC
@RequiredArgsConstructor
public class DepartmentDaoImpl implements DepartmentDao {
    private final SessionFactory sessionFactory;


    /**
     *
     * @param dept
     * @return In JPA/Hibernate: change data (Insert, delete, update) -> transaction (tự quản lý)
     */
    @Override
    public Department createDepartment(Department dept) {
        Session session = sessionFactory.openSession();
        session.persist(dept);
        return dept;
    }

    @Override
    public Department findById(Long id) {
        return sessionFactory.openSession().get(Department.class, id);
    }

    @Override
    public boolean isExisted(String name) {
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery("SELECT COUNT(d) FROM Department d WHERE d.departmentName = :param", Long.class);

        query.setParameter("param", name);

        Long amount = query.getSingleResult();

        return amount > 0;


    }

    private static boolean extracted(Long amount) {
        return amount > 0;
    }

    @Override
    public List<Department> findAll() {
        Session session = sessionFactory.openSession();
        TypedQuery<Department> query = session.createQuery("SELECT d " +
                "FROM Department d", Department.class);
        return query.getResultList();
    }


}
