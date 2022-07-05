package com.example.web.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Gps {
 
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY) // auto_incremnent
	private int id;

	@Column
	private String distance  ; //섬머노트 라이브러리 <html>태그가 섞여서 디자인이 됨.
						 // 경로 제외, 텍스트만 저장하는곳
	@Column
	private String timer; //이미지경로를 저장하는 곳
		
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="userId") //field값은 userId로 만들어잔ㄷ
	private User user; //DB는 오브젝트를 저장할 수 없다. Fk,자바는 오브젝트를 저장할 수 있어서 충돌난다.
	// 테이블이 생성될때 FK로 만들어진다. User테이블을 참고


	@CreationTimestamp
	private Timestamp createDate;

	public Gps orElseThrow(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
