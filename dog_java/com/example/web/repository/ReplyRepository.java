package com.example.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.web.model.Reply;
import com.example.web.model.User;

public interface ReplyRepository extends JpaRepository<Reply,Integer>{

	@Query(value="select * from reply r where r.id =:id" ,nativeQuery=true)
	Reply findByReplyIdfind(@Param("id") int id);

}
