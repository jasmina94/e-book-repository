package com.ftn.model;

import com.ftn.model.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Entity
@Data
@NoArgsConstructor
public class User {

    public enum Type {
        ADMIN,
        SUBSCRIBER,
        VISITOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(optional = true)
    private Category category;

    @Column(nullable = false)
    private boolean enabled;


    public void merge(UserDTO userDTO){
        this.firstname = userDTO.getFirstname();
        this.lastname = userDTO.getLastname();
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.category = (userDTO.getCategory() != null ? userDTO.getCategory().construct() : null);
        switch (userDTO.getType()){
            case "ADMIN":
                this.setType(User.Type.ADMIN);
                break;
            case "SUBSCRIBER":
                this.setType(User.Type.SUBSCRIBER);
                break;
            case "VISITOR":
                this.setType(User.Type.VISITOR);
                break;
        }
    }
}
