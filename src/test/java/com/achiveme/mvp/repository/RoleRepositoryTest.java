package com.achiveme.mvp.repository;


import com.achiveme.mvp.MvpApplication;
import com.achiveme.mvp.entity.Role;
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
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;


    @Test
    void findByNameSuccess() {

        // Arrange
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");
        roleRepository.save(role);

        // Act
        Role roleTest = roleRepository.findByName(role.getName()).get();

        // Assert
        Assertions.assertThat(roleTest.getId()).isEqualTo(role.getId());

    }

    @Test
    void findByNameFail() {

        // Arrange
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");
        roleRepository.save(role);

        // Act
        Optional<Role> roleTest = roleRepository.findByName("ADMINN");

        // Assert
        Assertions.assertThat(roleTest).isEmpty();

    }

}