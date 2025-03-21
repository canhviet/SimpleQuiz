package viet.DACN.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OptionResponse {
    Long id;
    String title;
    Boolean isCorrect;
}
