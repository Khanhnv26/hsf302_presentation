package org.ats.dao;

import org.ats.dto.UserRequest;
import org.ats.entities.User;

public interface AuthDao {
    User login(UserRequest userRequest);
    User register(User user);
}
