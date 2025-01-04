package com.achiveme.mvp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Table(name="users")
@Entity
@Getter
@Setter
@ToString(exclude = {"challengeParticipants", "createdChallenges", "passwordHash"})
@EqualsAndHashCode(exclude = {"challengeParticipants", "createdChallenges"})
public class User {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="username")
    private String username;

    @Column(name="first_name")
    private String firstName;


    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="password_hash")
    private String passwordHash;

    @Column(name="join_date")
    private LocalDate joinDate;


    @Column(name = "other_details")
    private String otherDetails;

    @OneToMany(mappedBy = "creatorUser")
    private Set<Challenge> createdChallenges;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)  //if user is removed deleted -> chalengeParticipants also.
    Set<ChallengeParticipant> challengeParticipants;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private List<Role> roles;

    public void addRole(Role role){
        if(roles == null){
            roles = new ArrayList<>();
        }
        roles.add(role);
    }


}
