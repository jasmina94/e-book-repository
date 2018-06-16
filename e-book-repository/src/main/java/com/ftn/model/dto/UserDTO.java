package com.ftn.model.dto;

import com.ftn.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Data
public class UserDTO {

    private long id;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String type;

    private CategoryDTO category;

    public UserDTO() {}

    public UserDTO(User user){
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.type = user.getType().toString();
        if(user.getCategory() != null)
            this.category = new CategoryDTO(user.getCategory());
    }

    public User construct() {
        final User user = new User();
        user.setId(this.id);
        user.setFirstname(this.firstname);
        user.setLastname(this.lastname);
        user.setUsername(this.username);
        user.setPassword(this.password);
        if(this.category != null)
            user.setCategory(this.category.construct());
        switch (this.type){
            case "ADMIN":
                user.setType(User.Type.ADMIN);
                break;
            case "SUBSCRIBER":
                user.setType(User.Type.SUBSCRIBER);
                break;
            case "VISITOR":
                user.setType(User.Type.VISITOR);
                break;
        }
        return user;
    }
}
