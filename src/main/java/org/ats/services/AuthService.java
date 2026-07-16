package org.ats.services;

import org.ats.dto.UserRegisterRequest;
import org.ats.dto.UserRequest;
import org.ats.entities.User;

public interface AuthService {
    User authenticate(UserRequest userRequest);

    Long register(UserRegisterRequest registerRequest);
}
