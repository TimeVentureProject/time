package com.TimeVenture.model.entity.projectMember;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.enums.Auth;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "project_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "projectMemberId")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "p_member", nullable = false)
    private int projectMemberId;

    @ManyToOne
    @JoinColumn(name = "id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private Auth auth;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private Member member;

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

}
