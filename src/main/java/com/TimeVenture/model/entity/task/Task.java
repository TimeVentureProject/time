package com.TimeVenture.model.entity.task;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.model.enums.Priority;
import com.TimeVenture.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Tasks")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tid")
public class Task {

    @Id
    @Column(name = "t_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;

    @ManyToOne
    @JoinColumn(name = "p_id", nullable = false)
    private Project pid;

    @ManyToOne
    @JoinColumn(name = "m_id", nullable = false)
    private Member mid;

    @ManyToOne
    @JoinColumn(name = "p_member", nullable = true)
    private ProjectMember pmember;

    private String title;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(nullable = true)
    private Timestamp dueDate;

    @Column(nullable = true)
    private Timestamp updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TaskStatus taskStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Priority priority;

}
