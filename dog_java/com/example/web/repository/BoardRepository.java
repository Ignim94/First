package com.example.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.web.model.Board;
import com.example.web.model.Reply;

// DAO
// 자동으로 BEAN 등록이 된다.
// @Repository가 생략된 것
public interface BoardRepository extends JpaRepository<Board,Integer>{

	Board findByTitle(String title);

	@Query(value="select * from Board b where b.id =:id" ,nativeQuery=true)
	Board findByBoardIdfind(@Param("id") int id);
	
    List<Board> findByTitleLike(String title);
    Page<Board> findAll(Pageable pageable);
    
    @Modifying
    @Query("update Board p set p.count = p.count + 1 where p.id = :id")
    int updateView(int id);
}
