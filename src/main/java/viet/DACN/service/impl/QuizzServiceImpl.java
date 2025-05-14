package viet.DACN.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ai.chat.ChatClient;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import viet.DACN.dto.request.GenerateAI;
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
    private final ChatClient chatClient;

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

    @Override
    @Transactional
    public Long generateQuizWithAI(GenerateAI request) {
        List<QuestionRequest> aiQuestions = generateAIQuestions(request.getTopic(), request.getNumQuestions());
        
        QuizzRequest quizzRequest = QuizzRequest.builder()
            .name(request.getQuizzName())
            .userId(request.getUserId())
            .questions(aiQuestions)
            .build();

        return saveQuizz(quizzRequest);
    }

    private List<QuestionRequest> generateAIQuestions(String topic, int numQuestions) {
        int numOptions = 4;
        Set<String> uniqueTitles = new HashSet<>();
        List<QuestionRequest> questions = new ArrayList<>();
    
        String prompt = 
            "Tạo ra " + numQuestions + " câu hỏi trắc nghiệm bằng tiếng Việt về chủ đề '" + topic + 
            "', mỗi câu có đúng " + numOptions + " đáp án lựa chọn. Trả về theo định dạng chính xác sau, không thêm hoặc bớt bất kỳ nội dung nào ngoài định dạng này:\n\n" +
            "Câu hỏi: [nội dung câu hỏi]\n" +
            "A. [đáp án A]\n" +
            "B. [đáp án B]\n" +
            "C. [đáp án C]\n" +
            "D. [đáp án D]\n" +
            "Đáp án đúng: [A/B/C/D]\n\n" +
            "Yêu cầu: \n" +
            "- Câu hỏi phải rõ ràng, đúng ngữ pháp, phù hợp với chương trình học tại Việt Nam.\n" +
            "- Đáp án đúng phải chính xác và chỉ có một đáp án đúng.\n" +
            "- Không thêm tiêu đề, số thứ tự, hoặc bất kỳ nội dung ngoài định dạng trên.\n" +
            "- Không được bỏ sót bất kỳ phần nào trong mỗi câu hỏi.\n" +
            "- Đảm bảo mỗi câu hỏi có đúng 4 đáp án và một đáp án đúng.\n" +
            "- Không để câu hỏi hoặc đáp án rỗng, trùng lặp, hoặc vượt quá 500 ký tự.";
    
        int maxRetries = 3; // Số lần thử tối đa để lấy đủ câu hỏi
        int retries = 0;
    
        while (questions.size() < numQuestions && retries < maxRetries) {
            try {
                String rawResponse = chatClient.call(prompt);
                // Làm sạch phản hồi: loại bỏ khoảng trắng thừa, ký tự xuống dòng không cần thiết
                rawResponse = rawResponse.replaceAll("\\n\\s*\\n+", "\n").trim();
                String[] questionBlocks = rawResponse.split("(?=Câu hỏi:)");
    
                for (String block : questionBlocks) {
                    block = block.trim();
                    if (block.isEmpty()) continue;
    
                    QuestionRequest q = parseAIResponse(block, numOptions);
                    if (q != null && isValidQuestion(q) && uniqueTitles.add(q.getTitle())) {
                        questions.add(q);
                        if (questions.size() >= numQuestions) break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error generating questions (Retry " + (retries + 1) + "): " + e.getMessage());
            }
            retries++;
        }
    
        if (questions.size() < numQuestions) {
            System.err.println("Could not generate enough valid questions. Requested: " + numQuestions + ", Generated: " + questions.size());
        }
    
        return questions;
    }
    
    private QuestionRequest parseAIResponse(String text, int numOptions) {
        try {
            Pattern questionPattern = Pattern.compile("Câu hỏi:\\s*(.*?)(?=\\n[A-D]\\.|\\nĐáp án đúng:|$)", Pattern.DOTALL);
            Pattern optionPattern = Pattern.compile("([A-D])\\.\\s*(.*?)(?=\\n[A-D]\\.|\\nĐáp án đúng:|$)", Pattern.DOTALL);
            Pattern correctPattern = Pattern.compile("Đáp án( đúng)?[:：]\\s*([A-D])(?!.*Đáp án đúng)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    
            Matcher qMatcher = questionPattern.matcher(text);
            Matcher oMatcher = optionPattern.matcher(text);
            Matcher cMatcher = correctPattern.matcher(text);
    
            if (!qMatcher.find() || !cMatcher.find()) {
                System.err.println("Invalid format: Missing question or correct answer in block: " + text);
                return null;
            }
    
            String questionTitle = qMatcher.group(1).trim();
            String correctAnswer = cMatcher.group(2);
            List<OptionRequest> options = new ArrayList<>();
            Set<String> optionTitles = new HashSet<>(); // Kiểm tra trùng lặp đáp án
    
            while (oMatcher.find() && options.size() < numOptions) {
                String label = oMatcher.group(1);
                String title = oMatcher.group(2).trim();
    
                if (title.isEmpty() || optionTitles.contains(title)) {
                    System.err.println("Invalid or duplicate option for question: " + questionTitle + ", Option: " + title);
                    return null;
                }
    
                optionTitles.add(title);
                boolean isCorrect = label.equals(correctAnswer);
                options.add(OptionRequest.builder()
                    .title(title)
                    .isCorrect(isCorrect)
                    .build());
            }
    
            if (options.size() != numOptions) {
                System.err.println("Incorrect number of options for question: " + questionTitle + " (Found: " + options.size() + ")");
                return null;
            }
    
            // Kiểm tra chỉ có một đáp án đúng
            long correctCount = options.stream().filter(option -> option.getIsCorrect()).count();
            if (correctCount != 1) {
                System.err.println("Invalid number of correct answers for question: " + questionTitle + " (Found: " + correctCount + ")");
                return null;
            }
    
            return QuestionRequest.builder()
                .title(questionTitle)
                .options(options)
                .build();
    
        } catch (Exception e) {
            System.err.println("Error parsing AI response: " + e.getMessage() + "\nBlock: " + text);
            return null;
        }
    }
    
    // Kiểm tra tính hợp lệ của câu hỏi trước khi thêm vào danh sách
    private boolean isValidQuestion(QuestionRequest question) {
        if (question == null || question.getTitle() == null || question.getTitle().isEmpty()) {
            System.err.println("Invalid question: Null or empty title");
            return false;
        }
    
        if (question.getTitle().length() > 500) {
            System.err.println("Invalid question: Title too long: " + question.getTitle());
            return false;
        }
    
        List<OptionRequest> options = question.getOptions();
        if (options == null || options.size() != 4) {
            System.err.println("Invalid question: Incorrect number of options: " + (options == null ? 0 : options.size()));
            return false;
        }
    
        for (OptionRequest option : options) {
            if (option == null || option.getTitle() == null || option.getTitle().isEmpty()) {
                System.err.println("Invalid question: Null or empty option for question: " + question.getTitle());
                return false;
            }
            if (option.getTitle().length() > 500) {
                System.err.println("Invalid question: Option too long for question: " + question.getTitle());
                return false;
            }
        }
    
        return true;
    }
}