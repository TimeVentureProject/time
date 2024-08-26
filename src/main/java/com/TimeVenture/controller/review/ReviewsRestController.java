package com.TimeVenture.controller.review;

import com.TimeVenture.model.dto.review.CreateReviewsDto;
import com.TimeVenture.model.dto.review.ReviewsDto;
import com.TimeVenture.model.dto.task.ResponseTaskDto;
import com.TimeVenture.model.entity.task.Task;
import com.TimeVenture.service.project.ProjectService;
import com.TimeVenture.service.review.ReviewsService;
import com.TimeVenture.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/reviews")
public class ReviewsRestController {

    private final ReviewsService reviewsService;

    @Autowired
    public ReviewsRestController(ReviewsService reviewsService, ProjectService projectService) {
        this.reviewsService = reviewsService;
    }

    /* DB 연동 확인 Test
    @GetMapping
    public List<Reviews> getReviews() {
        return reviewsService.getAllReviews();
    }*/

    //CREATE : 댓글 생성
    @PostMapping
    public ReviewsDto addReviews(@RequestBody CreateReviewsDto createReviewsDto){
        /* @RequestBody : HTTP 요청의 본문을 Java 객체로 변환할 때 사용
        클라이언트가 서버로 데이터를 보낼 때(POST, PUT 등) 요청 본문을 Java 객체로 변환 */
        return reviewsService.addReviews(createReviewsDto);
    }

    //READ : tasks 1개 당 생성되는 리뷰 조회(댓글 조회)
    @GetMapping("/{pid}")
    public List<ReviewsDto> getReviews(@PathVariable("pid") int pid){
        return reviewsService.getReviewsByPid(pid);
    }

    //UPDATE : 댓글 내용 수정
    @PutMapping("/{reviewId}")
    public ReviewsDto updateReviews(@PathVariable("reviewId") int reviewId, @RequestBody ReviewsDto reviewsDto){
        return reviewsService.updateReviews(reviewId, reviewsDto);
    }

    //DELETE : 댓글 삭제
    @DeleteMapping("/{reviewId}")
    public void deleteReviews(@PathVariable("reviewId") int reviewId){
        reviewsService.deleteReviews(reviewId);
    }

}