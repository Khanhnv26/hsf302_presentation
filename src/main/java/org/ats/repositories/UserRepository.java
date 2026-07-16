package org.ats.repositories;

import org.ats.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("FROM User u WHERE u.email = :email AND u.passwordHash = :password") // JPQL
    Optional<User> findByEmailAndPass(@Param("email") String email, @Param("password") String password);

    Boolean existsByEmail(String email);
}
