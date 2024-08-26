package com.TimeVenture.model.dto.project;

import com.TimeVenture.model.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RequestProjectDto {

    private int pid;
    private String pexplain;
    private String pname;
    private Date pstartDate;
    private Date pendDate;
    private String pcolor;

    public RequestProjectDto(Project project) {
        this.pid = project.getPid();
        this.pexplain = project.getPexplain();
        this.pname = project.getPname();
        this.pstartDate = project.getPstartDate();
        this.pendDate = project.getPendDate();
        this.pcolor = project.getPcolor();
    }
}
