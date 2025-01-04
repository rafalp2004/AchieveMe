package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.user.UserResponseDTO;
import com.achiveme.mvp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //Entity to DTO
    @Mapping(source = "joinDate", target = "createdDate")
    UserResponseDTO userToUserDTO(User user);

}

