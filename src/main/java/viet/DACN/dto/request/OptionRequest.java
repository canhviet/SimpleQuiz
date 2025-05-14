package viet.DACN.dto.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionRequest implements Serializable{
    private String title;

    private Boolean isCorrect;
}
