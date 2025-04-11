package viet.DACN.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import viet.DACN.dto.request.OptionRequest;
import viet.DACN.dto.request.QuestionRequest;
import viet.DACN.dto.request.QuizzRequest;
import viet.DACN.dto.response.OptionResponse;
import viet.DACN.dto.response.QuestionResponse;
import viet.DACN.dto.response.QuizzResponse;
import viet.DACN.exception.InvalidDataException;
import viet.DACN.model.Options;
import viet.DACN.model.Questions;
import viet.DACN.model.Quizz;
import viet.DACN.model.User;
import viet.DACN.repo.QuizzRepository;
import viet.DACN.repo.UserRepository;
import viet.DACN.service.QuizzService;

@Service
@RequiredArgsConstructor
public class QuizzServiceImpl implements QuizzService {

    private final QuizzRepository quizzRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long saveQuizz(QuizzRequest request) {
        Quizz quizz = Quizz.builder()
            .name(request.getName())
            .user(getUserById(request.getUserId()))
            .build();

        quizz.setQuestions(convertToQuestions(request.getQuestions(), quizz));

        quizzRepository.save(quizz);

        return quizz.getId();
    }

    @Override
    public List<QuizzResponse> getAllQuizz() {
        List<Quizz> quizzs = quizzRepository.findAllWithQuestionsAndOptions();
        return convertToQuizzResponses(quizzs);
    }

    @Override
    public QuizzResponse getQuizzResponseByQuizzId(Long quizzId) {
        Quizz quizz = quizzRepository.findByIdWithQuestionsAndOptions(quizzId)
            .orElseThrow(() -> new InvalidDataException("Quizz not found with id: " + quizzId));
        return QuizzResponse.builder()
                .id(quizz.getId())
                .name(quizz.getName())
                .userId(quizz.getUser().getId())
                .questions(convertToQuestionResponses(quizz.getQuestions()))
                .build();
    }

    private List<QuizzResponse> convertToQuizzResponses(List<Quizz> quizzes) {
        return quizzes.stream()
            .map(
                q -> QuizzResponse.builder()
                .id(q.getId())
                .name(q.getName())
                .userId(q.getUser().getId())
                .questions(convertToQuestionResponses(q.getQuestions()))
                .updatedAt(q.getUpdatedAt())
                .build()
            )
            .collect(Collectors.toList());
    }

    private List<QuestionResponse> convertToQuestionResponses(Set<Questions> questions) {
        if (questions == null) {
            return Collections.emptyList();
        }
        return questions.stream()
            .map(
                q -> QuestionResponse.builder()
                .id(q.getId()).title(q.getTitle())
                .options(convertToOptionResponses(q.getOptions()))
                .build())
            .collect(Collectors.toList());
    }

    private List<OptionResponse> convertToOptionResponses(Set<Options> options) { 
        if (options == null) {
            return Collections.emptyList();
        }
        return options.stream()
            .map(
                o -> OptionResponse.builder()
                .id(o.getId())
                .title(o.getTitle())
                .isCorrect(o.getIs_correct())
                .build()
            )
            .collect(Collectors.toList());
    }



    private Set<Questions> convertToQuestions(List<QuestionRequest> request, Quizz quizz) {
        return request.stream().map(
            q -> {
                Questions question = Questions.builder()
                    .title(q.getTitle())
                    .quizz(quizz)
                    .build();
                question.setOptions(convertToOptions(q.getOptions(), question));
                return question;
            }
        ).collect(Collectors.toSet()); 
    }

    private Set<Options> convertToOptions(List<OptionRequest> request, Questions question) {
        return request.stream().map(
            o -> Options.builder()
                .title(o.getTitle())
                .is_correct(o.getIsCorrect())
                .question(question)
                .build()
        ).collect(Collectors.toSet()); 
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new InvalidDataException("User not found with id: " + userId));
    }

    @Override
    public List<QuizzResponse> getAllQuizzByUserId(Long userId) {
        List<Quizz> quizzs = quizzRepository.findAllWithUserId(userId);
        return convertToQuizzResponses(quizzs);
    }

    @Override
    @Transactional
    public Long updateQuizz(Long quizzId, QuizzRequest request) {
        System.out.println("Request data: " + request);
        Quizz quizz = quizzRepository.findByIdWithQuestionsAndOptions(quizzId)
            .orElseThrow(() -> new InvalidDataException("Quizz not found with id: " + quizzId));

        System.out.println("Before update: " + quizz);

        quizz.setName(request.getName());
        quizz.setUser(getUserById(request.getUserId()));

        quizz.getQuestions().clear();

        Set<Questions> newQuestions = convertToQuestions(request.getQuestions(), quizz);
        quizz.getQuestions().addAll(newQuestions);

        quizzRepository.saveAndFlush(quizz);
        return quizz.getId();
    }

    @Override
    @Transactional
    public void deleteQuizz(Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId)
            .orElseThrow(() -> new InvalidDataException("Quizz not found with id: " + quizzId));
        quizzRepository.delete(quizz);
    }

    @Override
    public List<QuizzResponse> searchQuizz(String keyword) {
        List<Quizz> quizzes = quizzRepository.findByNameContainingIgnoreCase(keyword);
        return convertToQuizzResponses(quizzes);
    }
}