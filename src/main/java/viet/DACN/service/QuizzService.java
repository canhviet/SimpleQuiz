package viet.DACN.service;

import java.util.List;

import viet.DACN.dto.request.QuizzRequest;
import viet.DACN.dto.response.QuizzResponse;

public interface QuizzService {
    Long saveQuizz(QuizzRequest request);
    List<QuizzResponse> getAllQuizz();
    QuizzResponse getQuizzResponseByQuizzId(Long quizzId);
}
