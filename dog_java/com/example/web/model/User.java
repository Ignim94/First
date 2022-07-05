package com.example.web.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//ORM -> Java(다른언어) object -> 테이블로 매핑해주는 기술
// @DynamicInsert << insert시에 null인 필드를 제외시켜준다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // User 클래스가 아래 변수들을 읽어서 MySQL에 해당내용이 담긴 테이블을 생성한다.
public class User {

	@Id	// Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; // 시퀀스, auto-increment

	@Column(nullable=false, length=15, unique=true) // 빈 값 x, 20이상 x
	private String username; //아이디

	@Column(nullable=false, length=100) // password를 암호화하게되면 길어지므로 넉넉하게 줘야함
	private String password;

	@Column(length=8, nullable=false, unique=true) // 빈 값 x, 20이상 x
	private String usernickname; //닉네임
	
	@Column(nullable=false, length=50)
	private String email; //네이밍을 내맘대로 바꿔도 mysql에 잘 적용된다.
	
	@Column(nullable=false,  length=50)
	private String address; //네이밍을 내맘대로 바꿔도 mysql에 잘 적용된다.
	
	// @ColumnDefault(" 'user' ") //문자임을 알려주기 위해 ''를 한겹 더 처리
	@Enumerated(EnumType.STRING) // DB에 타입이 스트링임을 알려주기위해 작성
	private RoleType role; // 관리자,매니저,유저 등 권한확인. //Enum을 쓰면 도메인을 만들어줄 수 있다.
	//RoleType에 작성해둔 것만 들어간다 ?..
	@CreationTimestamp // 회원가입시, 시간이 자동으로 입력
	private Timestamp createDate;
	
	/*
	public List<String> getRoleList(){
		if(this.role.length()>0) {
			return Arrays.asList(this.role.split(","));
		}
		return new ArrayList<>();
	} */
	

}
