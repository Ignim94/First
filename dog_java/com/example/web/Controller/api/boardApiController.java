package com.example.web.Controller.api;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.web.config.auth.PrincipalDetail;
import com.example.web.dto.ResponseDto;
import com.example.web.model.Board;
import com.example.web.model.Reply;
import com.example.web.service.BoardService;
import com.google.gson.JsonObject;

@RestController
public class boardApiController {


	@Autowired
	private BoardService boardSerivce;
	

	
	@PostMapping("/api/boardWrite")
	public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) throws IOException { // username, password, email
		System.out.println("게시판 글 작성");
		
		String sentence = board.getContent_text(); // p태그를 벗긴 content내용 다 가지고옴.
	//	System.out.println(sentence);
		
		boardSerivce.contentOnlyText(sentence, board); // content_text에 text만 남기기,글만 있으면 image는 null
		boardSerivce.getBase64DecodeString(sentence, board); // 디코딩 보내기
		boardSerivce.글쓰기(board, principal.getUser()); //글쓰기 해주는 서비스
		return new ResponseDto<>(HttpStatus.OK.value(), 1); //console(resp) 해보면 resp에 200, data에 1 return 되는걸 확인

	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id,  @AuthenticationPrincipal PrincipalDetail principal){
		boardSerivce.글삭제하기(id,principal);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> modify(@PathVariable int id, @RequestBody Board board) throws IOException{
		System.out.println("board update:"+ id);
		System.out.println("board update:"+ board.getTitle());
	//	System.out.println("board update:"+ board.getContent.());
		String sentence = board.getContent_text(); // p태그를 벗긴 content내용 다 가지고옴.
		boardSerivce.contentOnlyText(sentence, board); // content_text에 text만 남기기,글만 있으면 image는 null
		boardSerivce.getBase64DecodeString(sentence, board); // 디코딩 보내기
		boardSerivce.글수정하기(id,board);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	//데이터 받을 때 컨트롤러에서 DTO를 만들어서 받는게 좋다.
	//DTO를 사용하지 않은 이유?-> 작은 프로젝트라 ? 거대해지면 ?? 관리할 데이터들을 나눠서 수정을 촘촘히 가능하게?
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Integer> replySave(@PathVariable int boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal) { // username, password, email
		
		reply.setUser(principal.getUser());	
		boardSerivce.댓글쓰기(principal.getUser(), boardId, reply);
		return new ResponseDto<>(HttpStatus.OK.value(), 1); //console(resp) 해보면 resp에 200, data에 1 return 되는걸 확인
	}
	
	@DeleteMapping("/api/board/{boardId}/reply/{replyId}")
	public ResponseDto<Integer> replyDelete(@PathVariable int replyId, @AuthenticationPrincipal PrincipalDetail principal){
		boardSerivce.댓글삭제(replyId,principal);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
		
	}
	
}
