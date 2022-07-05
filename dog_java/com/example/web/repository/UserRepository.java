package com.example.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.web.model.User;

// DAO
// 자동으로 BEAN 등록이 된다.
// @Repository가 생략된 것
public interface UserRepository extends JpaRepository<User,Integer>{

	//select * from user where username = 1?;
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameAndPassword(String username,String password);
	
	@Query(value="select * from user u where u.username =:username" ,nativeQuery=true)
	User findByUsernameOne(@Param("username") String username);
	
	@Query(value="select * from user u where u.usernickname =:usernickname" ,nativeQuery=true)
	User findByUsernickname(@Param("usernickname") String usernickname);
	
	@Query(value="select * from user u where u.email =:email" ,nativeQuery=true)
	User findByUsernameIdfind(@Param("email") String email);

	
}









// JPA naming 쿼리
//  select * from user where username =?(para1) and password = ?(para2);
//	User findByUsernameAndPassword(String username, String password);
//	@Query(value="select * from user where username =?(para1) and password = ?(para2)",nativeQuery = true)
//	User login(String username, String password);