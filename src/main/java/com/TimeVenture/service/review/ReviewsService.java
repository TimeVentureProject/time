package com.TimeVenture.service.review;

import com.TimeVenture.model.ReviewsModelMapper;
import com.TimeVenture.model.dto.member.MemberDto;
import com.TimeVenture.model.dto.review.CreateReviewsDto;
import com.TimeVenture.model.dto.review.ReviewsDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.review.Reviews;
import com.TimeVenture.model.entity.task.Task;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.repository.project.ProjectRepository;
import com.TimeVenture.repository.review.ReviewsRepository;
import com.TimeVenture.repository.task.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewsService {

    private final ReviewsRepository reviewsRepository;
    private final ReviewsModelMapper reviewsMapper;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ReviewsService(ReviewsRepository reviewsRepository, ReviewsModelMapper reviewsMapper, TaskRepository taskRepository, ProjectRepository projectRepository, MemberRepository memberRepository){
        this.reviewsRepository = reviewsRepository;
        this.reviewsMapper = reviewsMapper;
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }

    //CREATE : 댓글 추가
    public ReviewsDto addReviews(CreateReviewsDto createReviewsDto) {
        Project project = projectRepository.findById(createReviewsDto.getPid())
                        .orElseThrow(() -> new RuntimeException("Project not found"));
        Member member = memberRepository.findByEmail(createReviewsDto.getMid())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Reviews review = createReviewsDto.toEntity(project, member);
        Reviews saveReview = reviewsRepository.save(review);
        return reviewsMapper.INSTANCE.toDto(saveReview);
    }

    // READ : 댓글 전체 조회(test 용)
    /*public List<ReviewsDto> getAllReviews() {
        List<Reviews> reviews = reviewsRepository.findAll();
        return reviews.stream().map(reviewsMapper::toDto).toList() } */
    // READ : tasks 별 댓글 조회 (Reviews by Task ID)
    public List<ReviewsDto> getReviewsByPid(int pid) {
        Project project = projectRepository.findById(pid)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with task_id: " + pid));
        List<Reviews> reviews = reviewsRepository.findByPid(project);
        return reviews.stream().map(review -> {
            ReviewsDto reviewsDto = reviewsMapper.toDto(review);
            MemberDto memberDto = new MemberDto();
            memberDto.setEmail(review.getMid().getEmail());
            memberDto.setName(review.getMid().getName());
            memberDto.setImg(review.getMid().getImg());
            reviewsDto.setMid(memberDto);
            return reviewsDto;
        }).collect(Collectors.toList());
    }

    // UPDATE : 댓글 수정
    public ReviewsDto updateReviews(int reviewId, ReviewsDto reviewsDto){
        //댓글 존재 여부 확인(reviewId를 통해) 후 존재하지 않을 경우 런타임 익셉션 던짐
        Reviews existingReviews = reviewsRepository.findById(reviewId).orElseThrow(()
                -> new RuntimeException("댓글이 없습니다"));
        //존재할 경우 세팅
        existingReviews.setContent(reviewsDto.getContent());
        existingReviews.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        Reviews updateReviews = reviewsRepository.save(existingReviews);
        return reviewsMapper.toDto(updateReviews);
    }

    // DELETE : 댓글 삭제
    public void deleteReviews(int reviewId) {
        reviewsRepository.deleteById(reviewId);
    }

}