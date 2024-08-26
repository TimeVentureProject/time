package com.TimeVenture.model;

import com.TimeVenture.model.dto.review.ReviewsDto;
import com.TimeVenture.model.entity.review.Reviews;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T21:09:48+0900",
    comments = "version: 1.5.0.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ReviewsModelMapperImpl implements ReviewsModelMapper {

    @Override
    public ReviewsDto toDto(Reviews reviews) {
        if ( reviews == null ) {
            return null;
        }

        ReviewsDto reviewsDto = new ReviewsDto();

        reviewsDto.setReviewId( reviews.getReviewId() );
        reviewsDto.setTid( reviews.getTid() );
        reviewsDto.setMid( reviews.getMid() );
        reviewsDto.setContent( reviews.getContent() );
        reviewsDto.setCreatedDate( reviews.getCreatedDate() );

        return reviewsDto;
    }

    @Override
    public Reviews toReviews(ReviewsDto reviewsdto) {
        if ( reviewsdto == null ) {
            return null;
        }

        Reviews reviews = new Reviews();

        reviews.setReviewId( reviewsdto.getReviewId() );
        reviews.setTid( reviewsdto.getTid() );
        reviews.setMid( reviewsdto.getMid() );
        reviews.setContent( reviewsdto.getContent() );
        reviews.setCreatedDate( reviewsdto.getCreatedDate() );

        return reviews;
    }
}
