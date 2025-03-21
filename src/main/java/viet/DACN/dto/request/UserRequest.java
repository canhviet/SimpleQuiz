package viet.DACN.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserRequest implements Serializable{
    @NotNull(message = "username must be not null")
    String username;

    @NotNull(message = "password must be not null")
    String password;

    @NotNull(message = "first name must be not null")
    String first_name;

    @NotNull(message = "last name must be not null")
    String last_name;

}
