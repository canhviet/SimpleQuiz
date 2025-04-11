package viet.DACN.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import viet.DACN.model.Quizz;

@Repository
public interface QuizzRepository extends JpaRepository<Quizz, Long> {
    @Query("SELECT q FROM Quizz q LEFT JOIN FETCH q.questions ques LEFT JOIN FETCH ques.options")
    List<Quizz> findAllWithQuestionsAndOptions();

    @Query("SELECT q FROM Quizz q LEFT JOIN FETCH q.questions ques LEFT JOIN FETCH ques.options WHERE q.user.id = :userId")
    List<Quizz> findAllWithUserId(@Param("userId") Long userId);

    @Query("SELECT q FROM Quizz q LEFT JOIN FETCH q.questions ques LEFT JOIN FETCH ques.options WHERE q.id = :quizzId")
    Optional<Quizz> findByIdWithQuestionsAndOptions(@Param("quizzId") Long quizzId);

    @Query("SELECT DISTINCT q FROM Quizz q " +
           "LEFT JOIN FETCH q.questions ques " +
           "LEFT JOIN FETCH ques.options " +
           "WHERE LOWER(q.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Quizz> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
    
}


