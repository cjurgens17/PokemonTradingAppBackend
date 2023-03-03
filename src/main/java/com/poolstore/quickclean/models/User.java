package com.poolstore.quickclean.models;




import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name="first_name")
    private String firstname;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="pass_word")
    private String password;

    @Column(name="profile_picture")
    private String profilePicture;

    @Column(name="admin")
    private Boolean admin;

}
