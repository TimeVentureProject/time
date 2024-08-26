package com.TimeVenture.repository.review;

/* reviews 의 리포지토리 인터페이스
JpaRepository<엔티티, Integer(PK 데이터 타입)> 를 상속 받음으로써 JPA 쿼리 메서드를 통해
기본적인 CRUD 기능을 이용할 수 있음
ex) findAll() 전체 조회 , findById(id) : PK로 엔티티 조회 , save(S entity) 엔티티 저장 등

추가로 특정 조건으로 데이터를 조회하거나, 복잡한 쿼리 같은 경우 @Query 에너테이션과 함께
해당 인터페이스에 메서드를 정의하여 사용 가능

ex) findBy(컬럼명), deleteBy(컬럼명)
ex) @Query("쿼리") : 해당 부분에 직접 쿼리를 입력하여 사용도 가능함 */

import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.review.Reviews;
import com.TimeVenture.model.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    //리뷰 조회 : tasks 별로 댓글이 조회되기 때문에 t_id를 조건으로 검색
    List<Reviews> findByPid(Project pid);
}