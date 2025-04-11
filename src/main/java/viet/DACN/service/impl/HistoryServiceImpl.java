package viet.DACN.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import viet.DACN.dto.request.HistoryRequest;
import viet.DACN.dto.response.HistoryResponse;
import viet.DACN.exception.InvalidDataException;
import viet.DACN.model.History;
import viet.DACN.model.Quizz;
import viet.DACN.model.User;
import viet.DACN.repo.HistoryRepository;
import viet.DACN.repo.QuizzRepository;
import viet.DACN.repo.UserRepository;
import viet.DACN.service.HistoryService;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final QuizzRepository quizzRepository;

    @Override
    public List<HistoryResponse> allHistoryOfUser(Long userId) {
        List<History> list = historyRepository.findAllByUserId(userId);

        List<HistoryResponse> historyList = list.stream()
        .map(h -> HistoryResponse.builder()
            .score(h.getScore())
            .quizzId(h.getQuizz().getId())
            .createAt(h.getCreatedAt())
            .build()
        ).toList();

        return historyList;
    }

    @Override
    public Long saveHistory(HistoryRequest request) {
        
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (request.getQuizzId() == null) {
            throw new IllegalArgumentException("Quiz ID must not be null");
        }

        History history = History.builder()
            .score(request.getScore())
            .user(getUserById(1L))
            .quizz(getQuizzById(request.getQuizzId()))
            .build();

        historyRepository.save(history);

        return history.getId();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new InvalidDataException("User not found with id: " + userId));
    }

    private Quizz getQuizzById(Long quizzId) {
        return quizzRepository.findByIdWithQuestionsAndOptions(quizzId)
            .orElseThrow(() -> new InvalidDataException("Quizz not found with id: " + quizzId));
    }
}
