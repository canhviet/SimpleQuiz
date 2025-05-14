package viet.DACN.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateAI {
    private String topic;
    private int numQuestions;
    private Long userId;
    private String quizzName;

    public GenerateAI() {}
    public GenerateAI(String topic, int numQuestions, Long userId, String quizzName) {
        this.topic = topic;
        this.numQuestions = numQuestions;
        this.userId = userId;
        this.quizzName = quizzName;
    }
}