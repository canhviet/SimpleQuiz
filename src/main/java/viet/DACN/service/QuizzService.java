package viet.DACN.service;

import java.util.List;

import viet.DACN.dto.request.QuizzRequest;
import viet.DACN.dto.response.QuizzResponse;

public interface QuizzService {
    Long saveQuizz(QuizzRequest request);
    List<QuizzResponse> getAllQuizz();
    List<QuizzResponse> getAllQuizzByUserId(Long userId);
    QuizzResponse getQuizzResponseByQuizzId(Long quizzId);
    Long updateQuizz(Long quizzId, QuizzRequest request);
    void deleteQuizz(Long quizzId);
    List<QuizzResponse> searchQuizz(String keyword);
}
