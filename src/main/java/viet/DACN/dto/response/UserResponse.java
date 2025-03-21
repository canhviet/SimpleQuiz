package viet.DACN.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {
    Long id;
    String first_name;
    String last_name;
    String username;
}
