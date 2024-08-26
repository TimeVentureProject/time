package com.TimeVenture.model.entity.project;

import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.model.entity.task.Task;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Projects")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pid")
public class Project {

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProjectMember> projectMembers;

    @OneToMany(mappedBy = "pid", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> Tasks;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id", nullable = false)
    private int pid;

    @Column(name = "p_name", nullable = false)
    private String pname;

    @Column(name = "p_explain")
    private String pexplain;

    @Column(name = "startdate")
    private Timestamp pstartDate;


    @Column(name = "p_createdate", nullable = false)
    @CreationTimestamp
    private Timestamp pcreateDate;

    @Column(name = "p_updatedate")
    private Timestamp pupdateDate;

    @Column(name = "p_enddate")
    private Timestamp pendDate;

    @Column(name = "p_color", nullable = false)
    private String pcolor;

    @Builder
    public Project(String pName, String pExplain, Timestamp pStartDate, Timestamp pEndDate, String pColor) {
        this.pname = pName;
        this.pexplain = pExplain;
        this.pstartDate = pStartDate;
        this.pendDate = pEndDate;
        this.pcolor = pColor;
    }

    public void update(String pName, String pExplain, Timestamp pStartDate, Timestamp pEndDate, String pColor) {
        this.pname = pName;
        this.pexplain = pExplain;
        this.pstartDate = pStartDate;
        this.pendDate = pEndDate;
        this.pcolor = pColor;
    }
}
