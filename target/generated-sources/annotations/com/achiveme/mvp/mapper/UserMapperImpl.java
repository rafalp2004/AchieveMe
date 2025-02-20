package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.user.UserResponseDTO;
import com.achiveme.mvp.entity.User;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T17:08:07+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        LocalDate createdDate = null;
        int id = 0;
        String firstName = null;
        String lastName = null;

        createdDate = user.getJoinDate();
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();

        UserResponseDTO userResponseDTO = new UserResponseDTO( id, firstName, lastName, createdDate );

        return userResponseDTO;
    }
}
