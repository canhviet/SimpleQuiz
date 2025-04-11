package viet.DACN.dto.response;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizzResponse {
    Long id;
    String name;
    Long userId;
    List<QuestionResponse> questions;
    private Date updatedAt;
}
