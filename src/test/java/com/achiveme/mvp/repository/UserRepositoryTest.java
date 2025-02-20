package com.achiveme.mvp.repository;

import com.achiveme.mvp.MvpApplication;
import com.achiveme.mvp.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

@ContextConfiguration(classes = {MvpApplication.class})
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void findByNameSuccess() {

        // Arrange
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("jan@nowak.pl");

        userRepository.save(user);

        // Act
        User user1 = userRepository.findByEmail("jan@nowak.pl").get();

        // Assert
        Assertions.assertThat(user1).isEqualTo(user);

    }

    @Test
    void findByNameFail() {

        // Arrange
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("jan@nowak.pl");

        userRepository.save(user);


        // Act
        Optional<User> user1 = userRepository.findByEmail("jan@gmai.com");

        // Assert
        Assertions.assertThat(user1).isEmpty();

    }


}
