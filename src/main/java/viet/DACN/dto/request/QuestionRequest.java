package viet.DACN.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionRequest implements Serializable{
    private String title;
    List<OptionRequest> options;
}
