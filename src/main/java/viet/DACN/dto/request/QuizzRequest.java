package viet.DACN.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

@Getter
public class QuizzRequest implements Serializable{
    private String name;
    private Long userId;
    List<QuestionRequest> questions;
}
