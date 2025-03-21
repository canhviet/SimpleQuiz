package viet.DACN.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

@Getter
public class QuestionRequest implements Serializable{
    private String title;
    List<OptionRequest> options;
}
