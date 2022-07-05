package com.example.web.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.model.Board;
import com.example.web.model.Reply;
import com.example.web.repository.BoardRepository;
import com.example.web.repository.ReplyRepository;

@RestController
public class ReplyControllerTest {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@GetMapping("/dummy/test/board/{id}")
	public Board getBoard(@PathVariable int id) {
		return boardRepository.findById(id).get();
	}
	
	@GetMapping("/dummy/test/reply/{id}")
	public List<Reply> getReply(@PathVariable int id) {
		return replyRepository.findAll();
	}
}
