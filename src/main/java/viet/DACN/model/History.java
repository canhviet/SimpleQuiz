package viet.DACN.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History extends AbstractEntity<Long>{

    @ManyToOne
    @JoinColumn(name = "quizz_id", nullable = false)
    private Quizz quizz;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String score;

}
