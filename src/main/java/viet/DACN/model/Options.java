package viet.DACN.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Options extends AbstractEntity<Long>{
    
    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    @Column(name = "is_correct")
    private Boolean is_correct;
}
