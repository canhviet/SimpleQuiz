package viet.DACN.dto.request;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class OptionRequest implements Serializable{
    private String title;

    private Boolean isCorrect;
}
