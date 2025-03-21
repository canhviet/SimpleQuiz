package viet.DACN.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionResponse {
    Long id;
    String title;
    List<OptionResponse> options;
}
