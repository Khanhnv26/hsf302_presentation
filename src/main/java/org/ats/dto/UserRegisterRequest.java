package org.ats.dto;

import lombok.*;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    private String fullName;
    private String email;
    private String passwordHash;
    private String phone;
}
