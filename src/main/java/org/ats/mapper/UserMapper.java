package org.ats.mapper;

import org.ats.dto.UserRegisterRequest;
import org.ats.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegisterRequest userRegisterRequest);
}
