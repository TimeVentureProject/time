package com.TimeVenture.model.entity.review;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.task.Task;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne
    @JoinColumn(name = "p_id")
    private Project pid;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private Member mid;

    private String content;

    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

}