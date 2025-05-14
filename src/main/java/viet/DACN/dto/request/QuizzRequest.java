package viet.DACN.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QuizzRequest implements Serializable{
    private String name;
    private Long userId;
    List<QuestionRequest> questions;
}
