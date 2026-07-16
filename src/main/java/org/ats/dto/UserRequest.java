package org.ats.dto;

import lombok.*;

@NoArgsConstructor@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserRequest {
    private String email;
    private String password;
}
