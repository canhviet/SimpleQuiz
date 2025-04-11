package viet.DACN.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HistoryRequest implements Serializable{
    private Long quizzId;
    @NotNull(message = "User ID must not be null")
    private Long userId;
    private String score;
}