package com.TimeVenture.model;

import com.TimeVenture.model.dto.review.ReviewsDto;
import com.TimeVenture.model.entity.review.Reviews;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/* 엔티티와 DTO 의 매핑 인터페이스

생성된 매퍼 인스턴스가 Spring의 빈으로 등록 (생성된 Mapper 인스턴스가 Spring 빈으로 등록)
-> @Autowired를 사용하여 해당 매퍼 주입 가능 */
@Mapper(componentModel = "spring")
public interface ReviewsModelMapper {
    //
    ReviewsModelMapper INSTANCE = Mappers.getMapper(ReviewsModelMapper.class);

    //엔티티를 DTO로 변환 : 엔티티의 필드 값을 DTO 필드 값으로 복사
    ReviewsDto toDto(Reviews reviews);

    //DTO를 엔티티로 변환 : DTO 필드 값을 엔티티 필드 값으로 복사
    Reviews toReviews(ReviewsDto reviewsdto);
}
