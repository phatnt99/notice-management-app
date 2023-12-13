package com.phatnt15.noticemanagement.mappers;

import com.phatnt15.noticemanagement.dtos.UserRequest;
import com.phatnt15.noticemanagement.dtos.UserResponse;
import com.phatnt15.noticemanagement.entities.User;
import org.mapstruct.Mapper;

/**
 * The interface User mapper.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * To entity user.
     *
     * @param request the request
     * @return the user
     */
    User toEntity(UserRequest request);

    /**
     * To response user response.
     *
     * @param entity the entity
     * @return the user response
     */
    UserResponse toResponse(User entity);
}
