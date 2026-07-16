package org.ats.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(indexes = {@Index(name = "U_UN_NAME", columnList = "full_name")}, name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@org.hibernate.annotations.NamedQuery(name = "login",
        query = "FROM User u WHERE u.email" +
        " = :email AND u.passwordHash = :password")
public class User extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_id")
    private Long deptId;

    @Column(name = "full_name", unique = false, nullable = false, columnDefinition = "VARCHAR(255)")
    private String fullName;

    @Column(name = "email", unique = true, nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "password_hash", unique = false, nullable = false, columnDefinition = "VARCHAR(255)")
    private String passwordHash;

    @Column(name = "phone", unique = true, nullable = false, columnDefinition = "VARCHAR(30)")
    private String phone;

    @Column(name = "role", unique = false, nullable = false, columnDefinition = "VARCHAR(50)")
    private String role;

    @Column(name = "sso_provider_id", unique = true, nullable = false, columnDefinition = "VARCHAR(255)")
    private String ssoProviderId;

    @Column(name = "status", unique = false, nullable = false, columnDefinition = "VARCHAR(50)")
    private String status;

}