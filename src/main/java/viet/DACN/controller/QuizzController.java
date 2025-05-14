package viet.DACN.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import viet.DACN.dto.request.GenerateAI;
import viet.DACN.dto.request.QuizzRequest;
import viet.DACN.dto.response.QuizzResponse;
import viet.DACN.dto.response.ResponseData;
import viet.DACN.service.QuizzService;

@RestController
@RequestMapping("/quizz")
@RequiredArgsConstructor
public class QuizzController {
    private final QuizzService quizzService;

    @PostMapping("/")
    public ResponseData<?> createQuizz(@RequestBody QuizzRequest request) {
        Long quizzId = quizzService.saveQuizz(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "quizz add success", quizzId);
    }

    @GetMapping("/all")
    public ResponseData<?> getAllQuizzs() {
        try {
            List<QuizzResponse> list = quizzService.getAllQuizz();

            return new ResponseData<>(HttpStatus.OK.value(), "list all quizz", list);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/{quizzId}")
    public ResponseData<?> getQuizzById(@PathVariable Long quizzId) {
        QuizzResponse quizz = quizzService.getQuizzResponseByQuizzId(quizzId);
        return new ResponseData<>(HttpStatus.OK.value(), "get quizz with id: " + quizzId, quizz);
    }

    @GetMapping("/user/{userId}")
    public ResponseData<?> getQuizzByUserId(@PathVariable Long userId) {
        try {
            List<QuizzResponse> list = quizzService.getAllQuizzByUserId(userId);

            return new ResponseData<>(HttpStatus.OK.value(), "list all quizz", list);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/{quizzId}")
    public ResponseData<?> updateQuizz(
            @PathVariable Long quizzId,
            @RequestBody QuizzRequest request) {
        try {
            Long updatedQuizzId = quizzService.updateQuizz(quizzId, request);
            return new ResponseData<>(HttpStatus.OK.value(), 
                "Quizz updated successfully", updatedQuizzId);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/{quizzId}")
    public ResponseData<?> deleteQuizz(@PathVariable Long quizzId) {
        try {
            quizzService.deleteQuizz(quizzId);
            return new ResponseData<>(HttpStatus.OK.value(), 
                "Quizz deleted successfully", null);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseData<?> searchQuizz(@RequestParam String keyword) {
        try {
            List<QuizzResponse> list = quizzService.searchQuizz(keyword);
            return new ResponseData<>(HttpStatus.OK.value(), 
                "Search results", list);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/generate-ai")
    public ResponseData<?> generateQuizWithAI(@RequestBody GenerateAI request) {
        try {
            Long quizzId = quizzService.generateQuizWithAI(request);
            return new ResponseData<>(HttpStatus.CREATED.value(), 
                "AI-generated quizz created successfully", quizzId);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), 
                "Failed to generate AI quizz: " + e.getMessage());
        }
    }
}
