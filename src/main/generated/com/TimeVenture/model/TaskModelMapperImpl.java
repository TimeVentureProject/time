package com.TimeVenture.model;

import com.TimeVenture.model.dto.task.ResponseTaskDto;
import com.TimeVenture.model.entity.task.Task;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T21:09:48+0900",
    comments = "version: 1.5.0.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class TaskModelMapperImpl implements TaskModelMapper {

    @Override
    public ResponseTaskDto toResponseDto(Task task) {
        if ( task == null ) {
            return null;
        }

        Task task1 = null;

        task1 = task;

        ResponseTaskDto responseTaskDto = new ResponseTaskDto( task1 );

        return responseTaskDto;
    }

    @Override
    public List<ResponseTaskDto> toResponseDtoList(List<Task> tasks) {
        if ( tasks == null ) {
            return null;
        }

        List<ResponseTaskDto> list = new ArrayList<ResponseTaskDto>( tasks.size() );
        for ( Task task : tasks ) {
            list.add( toResponseDto( task ) );
        }

        return list;
    }
}
