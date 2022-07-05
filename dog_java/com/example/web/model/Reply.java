package com.example.web.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; //시퀀스, auto_increment

	@Column(nullable=false, length=200)
	private String content;

	@ManyToOne // 여러개의 댓글이 하나의 게시글에 가능하다.
	@JoinColumn(name="boardId")
	private Board board;

	@ManyToOne // 여러개의 답변을 하나의 유저가 가능하다.
	@JoinColumn(name="userId")
	private User user;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	 private String reparent;
	 private String redepth;
	 private int reorder;
}
