package com.example.web.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = "replys")
public class Board {
 
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY) // auto_incremnent
	private int id;

	@Column(nullable =false, length =100)
	private String title;

	@Lob
	private String content_text; //섬머노트 라이브러리 <html>태그가 섞여서 디자인이 됨.
						 // 경로 제외, 텍스트만 저장하는곳
	@Lob
	private String content_image; //이미지경로를 저장하는 곳
		
	private int count; // 조회수

	@ManyToOne(fetch= FetchType.EAGER)// many= board , one= user :많은게시글을 한명의 유저가 작성 가능. 한명의 유저가 여러개의 게시글을 쓸 수 있다는 말
	// 보드테이블 셀렉트하면 유저정보는 조인해서 바로 가지고 올거야 (하나뿐이니가) << eager
	@JoinColumn(name="userId") //field값은 userId로 만들어잔ㄷ
	private User user; //DB는 오브젝트를 저장할 수 없다. Fk,자바는 오브젝트를 저장할 수 있어서 충돌난다.
	// 테이블이 생성될때 FK로 만들어진다. User테이블을 참고

	//게시글을 눌러서 들어가면 (나름의 상세보기) 에 유저정보와 댓글정보를 다 가지고있게된다.
	//board는 user,board,reply를 다 가지고있어야하는샘..
	// 하나의 게시글에는 한명의 유저가 글을 쓰지만, 여러명의 유저가 여러개의 답글을 쓸 수 있음
	@OneToMany(mappedBy ="board", fetch= FetchType.EAGER, cascade = CascadeType.REMOVE) //board를 지우면 댓글도 remove해주겠다.(jpa cascade 옵션) 			 
	@JsonIgnoreProperties({"board"})
	private List<Reply> replys; // 댓글이 여러개이므로.. 리스트에 담겨있음
	// 하나의 게시글에는 여러개의 답글. 해당 필드를 보드가 가질 이윤없어서 조인을 작성 x FK는 아니며 db를 만들지말라는 의미로 맵바이 사용											
	// 댓글은 되게많으니까 필요하면 들고오고 안필요하면 안가져올게 << lazy
	// 게시글에서 댓글이 한번에 보이게 만들려면 eager. 펼치기로 댓글을 보고싶을때만 보려면 lazy

	@CreationTimestamp
	private Timestamp createDate;

	public Board orElseThrow(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
