package com.TimeVenture.model.entity.file;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.task.Task;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="attachments")
@Getter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int fileId;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private Member mid;

    @ManyToOne
    @JoinColumn(name = "p_id")
    private Member pid;

    @ManyToOne
    @JoinColumn(name = "t_id")
    private Task tid;

    private String fileName;
    private String fileUrl;
    private Timestamp regDate;
}
