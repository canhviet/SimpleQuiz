package viet.DACN;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import viet.DACN.dto.request.HistoryRequest;
import viet.DACN.dto.request.QuizzRequest;
import viet.DACN.dto.request.UserRequest;
import viet.DACN.dto.response.HistoryResponse;
import viet.DACN.dto.response.PageResponse;
import viet.DACN.dto.response.QuizzResponse;
import viet.DACN.dto.response.ResponseData;
import viet.DACN.exception.InvalidDataException;
import viet.DACN.model.History;
import viet.DACN.model.Quizz;
import viet.DACN.model.Role;
import viet.DACN.model.User;
import viet.DACN.repo.HistoryRepository;
import viet.DACN.repo.QuizzRepository;
import viet.DACN.repo.RoleRepository;
import viet.DACN.repo.UserRepository;
import viet.DACN.service.impl.HistoryServiceImpl;
import viet.DACN.service.impl.QuizzServiceImpl;
import viet.DACN.service.impl.UserServiceImpl;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceUnitTests {

    // HistoryServiceImpl
    @Mock private HistoryRepository historyRepository;
    @Mock private UserRepository historyUserRepository;
    @Mock private QuizzRepository historyQuizzRepository;
    @InjectMocks private HistoryServiceImpl historyService;

    // QuizzServiceImpl
    @Mock private QuizzRepository quizzRepository;
    @Mock private UserRepository quizzUserRepository;
    @InjectMocks private QuizzServiceImpl quizzService;

    // UserServiceImpl
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserServiceImpl userService;

    private User user;
    private Quizz quizz;
    private History history;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("testuser")
                .build();
        user.setId(1L);  // Gán ID bằng setter

        quizz = Quizz.builder()
                .name("Math Quiz")
                .user(user)
                .build();
        quizz.setId(1L);  // Gán ID bằng setter

        history = History.builder()
                .score("85")
                .user(user)
                .quizz(quizz)
                .build();
        history.setId(1L);  // Gán ID bằng setter
    }

    // Tests for HistoryServiceImpl
    @Test
    public void testAllHistoryOfUser_Valid() {
        when(historyRepository.findAllByUserId(1L)).thenReturn(Collections.singletonList(history));
        List<HistoryResponse> result = historyService.allHistoryOfUser(1L);
        assertEquals(1, result.size());
        assertEquals("85", result.get(0).getScore());
        assertEquals(1L, result.get(0).getQuizzId());
    }

    @Test
    public void testAllHistoryOfUser_Empty() {
        when(historyRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());
        List<HistoryResponse> result = historyService.allHistoryOfUser(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSaveHistory_Valid() {
        HistoryRequest request = new HistoryRequest(1L, 1L, "90");
        when(historyUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(historyQuizzRepository.findByIdWithQuestionsAndOptions(1L)).thenReturn(Optional.of(quizz));
        when(historyRepository.save(any(History.class))).thenReturn(history);

        Long result = historyService.saveHistory(request);
        assertEquals(1L, result);
        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    public void testSaveHistory_NullUserId() {
        HistoryRequest request = new HistoryRequest(null, 1L, "90");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            historyService.saveHistory(request);
        });
        assertEquals("User ID must not be null", exception.getMessage());
    }

    @Test
    public void testSaveHistory_NullQuizzId() {
        HistoryRequest request = new HistoryRequest(1L, null, "90");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            historyService.saveHistory(request);
        });
        assertEquals("Quiz ID must not be null", exception.getMessage());
    }

    @Test
    public void testSaveHistory_UserNotFound() {
        HistoryRequest request = new HistoryRequest(1L, 1L, "90");
        when(historyUserRepository.findById(1L)).thenReturn(Optional.empty());
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            historyService.saveHistory(request);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void testSaveHistory_QuizzNotFound() {
        HistoryRequest request = new HistoryRequest(1L, 1L, "90");
        when(historyUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(historyQuizzRepository.findByIdWithQuestionsAndOptions(1L)).thenReturn(Optional.empty());
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            historyService.saveHistory(request);
        });
        assertEquals("Quizz not found with id: 1", exception.getMessage());
    }

    // Tests for QuizzServiceImpl
    @Test
    public void testSaveQuizz_Valid() {
        QuizzRequest request = new QuizzRequest("Math Quiz", 1L, Collections.emptyList());
        when(quizzUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(quizzRepository.save(any(Quizz.class))).thenReturn(quizz);

        Long result = quizzService.saveQuizz(request);
        assertEquals(1L, result);
        verify(quizzRepository, times(1)).save(any(Quizz.class));
    }

    @Test
    public void testSaveQuizz_UserNotFound() {
        QuizzRequest request = new QuizzRequest("Math Quiz", 1L, Collections.emptyList());
        when(quizzUserRepository.findById(1L)).thenReturn(Optional.empty());
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            quizzService.saveQuizz(request);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void testGetAllQuizz_Valid() {
        when(quizzRepository.findAllWithQuestionsAndOptions()).thenReturn(Collections.singletonList(quizz));
        List<QuizzResponse> result = quizzService.getAllQuizz();
        assertEquals(1, result.size());
        assertEquals("Math Quiz", result.get(0).getName());
    }

    @Test
    public void testGetAllQuizz_Empty() {
        when(quizzRepository.findAllWithQuestionsAndOptions()).thenReturn(Collections.emptyList());
        List<QuizzResponse> result = quizzService.getAllQuizz();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetQuizzResponseByQuizzId_Valid() {
        when(quizzRepository.findByIdWithQuestionsAndOptions(1L)).thenReturn(Optional.of(quizz));
        QuizzResponse result = quizzService.getQuizzResponseByQuizzId(1L);
        assertEquals("Math Quiz", result.getName());
        assertEquals(1L, result.getUserId());
    }

    @Test
    public void testGetQuizzResponseByQuizzId_NotFound() {
        when(quizzRepository.findByIdWithQuestionsAndOptions(1L)).thenReturn(Optional.empty());
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            quizzService.getQuizzResponseByQuizzId(1L);
        });
        assertEquals("Quizz not found with id: 1", exception.getMessage());
    }

    @Test
    public void testGetAllQuizzByUserId_Valid() {
        when(quizzRepository.findAllWithUserId(1L)).thenReturn(Collections.singletonList(quizz));
        List<QuizzResponse> result = quizzService.getAllQuizzByUserId(1L);
        assertEquals(1, result.size());
        assertEquals("Math Quiz", result.get(0).getName());
    }

    @Test
    public void testUpdateQuizz_Valid() {
        QuizzRequest request = new QuizzRequest("Updated Quiz", 1L, Collections.emptyList());
        when(quizzRepository.findByIdWithQuestionsAndOptions(1L)).thenReturn(Optional.of(quizz));
        when(quizzUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(quizzRepository.saveAndFlush(any(Quizz.class))).thenReturn(quizz);

        Long result = quizzService.updateQuizz(1L, request);
        assertEquals(1L, result);
        verify(quizzRepository, times(1)).saveAndFlush(any(Quizz.class));
    }

    @Test
    public void testDeleteQuizz_Valid() {
        when(quizzRepository.findById(1L)).thenReturn(Optional.of(quizz));
        doNothing().when(quizzRepository).delete(quizz);
        quizzService.deleteQuizz(1L);
        verify(quizzRepository, times(1)).delete(quizz);
    }

    @Test
    public void testSearchQuizz_Valid() {
        when(quizzRepository.findByNameContainingIgnoreCase("Math")).thenReturn(Collections.singletonList(quizz));
        List<QuizzResponse> result = quizzService.searchQuizz("Math");
        assertEquals(1, result.size());
        assertEquals("Math Quiz", result.get(0).getName());
    }

    // Tests for UserServiceImpl
    @Test
    public void testUserDetailsService_Valid() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        UserDetailsService uds = userService.userDetailsService();
        assertNotNull(uds.loadUserByUsername("testuser"));
    }

    @Test
    public void testUserDetailsService_NotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        UserDetailsService uds = userService.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> uds.loadUserByUsername("testuser"));
    }

    @Test
    public void testAllUsers_Valid() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        ResponseData<?> result = userService.all_users();
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertEquals(1, ((List<?>) result.getData()).size());
    }

    @Test
    public void testGetAllUser_Valid() {
        Page<User> page = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);
        PageResponse<?> result = userService.getAllUser(0, 10);
        assertEquals(1, ((List<History>) result.getItems()).size());
        assertEquals(1, result.getTotalPage());
    }

    @Test
    public void testGetByUsername_Valid() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        User result = userService.getByUsername("testuser");
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testGetByUsername_NotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            userService.getByUsername("testuser");
        });
        assertEquals("username not found", exception.getMessage());
    }

    @Test
    public void testSaveUser_Valid() {
        UserRequest request = new UserRequest("John", "Doe", "johndoe", "pass123");
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role()));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Long result = userService.saveUser(request);
        assertEquals(1L, result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSaveUser_RoleNotFound() {
        UserRequest request = new UserRequest("John", "Doe", "johndoe", "pass123");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            userService.saveUser(request);
        });
        assertEquals("Vai trò ROLE_USER không tồn tại", exception.getMessage());
    }
}
