package com.TimeVenture.model;

import com.TimeVenture.model.dto.file.FileDto;
import com.TimeVenture.model.entity.file.File;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T21:09:48+0900",
    comments = "version: 1.5.0.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class FileModelMapperImpl implements FileModelMapper {

    @Override
    public FileDto toDto(File file) {
        if ( file == null ) {
            return null;
        }

        FileDto fileDto = new FileDto();

        fileDto.setFileId( file.getFileId() );
        fileDto.setTid( file.getTid() );
        fileDto.setMid( file.getMid() );
        fileDto.setPid( file.getPid() );
        fileDto.setFileName( file.getFileName() );
        fileDto.setFileUrl( file.getFileUrl() );
        fileDto.setRegDate( file.getRegDate() );

        return fileDto;
    }

    @Override
    public File toFile(FileDto fileDto) {
        if ( fileDto == null ) {
            return null;
        }

        File file = new File();

        return file;
    }
}
