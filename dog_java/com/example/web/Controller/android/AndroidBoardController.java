package com.example.web.Controller.android;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.config.auth.PrincipalDetail;
import com.example.web.dto.ResponseDto;
import com.example.web.model.Board;
import com.example.web.model.Reply;
import com.example.web.model.RoleType;
import com.example.web.model.User;
import com.example.web.repository.BoardRepository;
import com.example.web.repository.ReplyRepository;
import com.example.web.repository.UserRepository;
import com.example.web.service.BoardService;
import com.example.web.service.UserService;

@RestController
public class AndroidBoardController {
	
	private static final Logger log = LoggerFactory.getLogger(AndroidBoardController.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(AndroidBoardController.class);
	private static final int String = 0;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardSerivce;

	@PostMapping("/auth/android/boards")
	public Map boardList(){
		  System.out.println("??????????????? ????????? ??????");
		  
		  List<Board> boardlist = boardRepository.findAll();
		  Map result = new HashMap<String,Object>();
		  result.put("board", boardlist);
		return result;
	}
	
	@PostMapping("/auth/android/boardDetail")
	public Map boardDetailList (@RequestParam(required = false) int id){
		  System.out.println("??????????????? ????????? ??????");
		  System.out.println("title:"+id);
		  
		  Board board = boardRepository.findById(id).orElseThrow(()->{
				return new IllegalArgumentException("??????????????? ??????");
			});
		  Map result = new HashMap<String,Object>();
		  
		  result.put("board", board);
	//	  System.out.println("result:"+result);
		  return result;
	}
	
	@PostMapping("/auth/android/boardWrite")
	public Map androidBoardWrite(Board board, String usernickname) throws IOException { 
		System.out.println("??????????????? ????????? ?????????");
		System.out.println("????????? title:"+board.getTitle());
	//	System.out.println("????????? content_text:"+newBoard.getContent_text());
	//	System.out.println("????????? content_image:"+newBoard.getContent_image());
		System.out.println("???????????????:"+ usernickname);
		
		String sentence = board.getContent_image();
		System.out.println(sentence);
		boardSerivce.androidGetBase64DecodeString(sentence, board);
		Map result = new HashMap<String,Object>();
		System.out.println("?????? ?????? ???");
	//	System.out.println("board:"+newBoard);
		User user = userService.findByUsernicknameIdfind(usernickname);
	//	System.out.println("user:"+ user);
		board.setUser(user);
		System.out.println("?????? ?????? ???");
		// ????????? null??? ????????? ????????? ????????? ????????????.
		if(user!=null) {
			boardRepository.save(board); // ????????? ??????
			System.out.println("????????? ?????? ??????");
	//		System.out.println("DB??? ????????? BOARD:"+newBoard);			
			List<Board> boardlist = boardRepository.findAll();			
			result.put("board", boardlist);
		//	System.out.println("result:"+result);
			return result;
		}else {
			
			return result;
		}
	}
	
	@PutMapping("/auth/android/boardModify")
	public Map androidBoardModify(int id, Board board /*, String usernickname*/ ){
		System.out.println("??????????????? ????????? ?????? ??????");
		System.out.println("board update:"+ id);
		System.out.println("board update:"+ board.getTitle());
		System.out.println("board update:"+ board.getContent_text());
		System.out.println("board update:"+ board.getContent_image());
	//	System.out.println("??????????????? ?????? ??????:"+usernickname);
		
		Map result = new HashMap<String,Object>();
	//	User user = userRepository.findByUsernickname(usernickname);
		boardSerivce.???????????????(id,board);
		List<Board> boardlist = boardRepository.findAll();	
		result.put("board", boardlist);
		return result;
	}
	
	@PostMapping("/auth/android/boardDelete")
	public Map androidBoardDelete(int id, String usernickname){
		
		Map result = new HashMap<String,Object>();
		System.out.println("??????????????? ????????? ?????? ??????");
		System.out.println("??????????????? ?????? ????????? ??????:" +id+ "???");
		
		Board board = boardRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("??????????????? ??????");
		});
		User user = userRepository.findByUsernickname(usernickname);
	//	System.out.println("?????????:"+board);
		String writer = board.getUser().getUsernickname();
	//	System.out.println("???????????? ?????????:" + writer);
	//	System.out.println("?????????????????? ???????????????:" + user);
		
		if(user == null) {
			result.put("responseCode", "????????? ????????? ????????????.");
			return result;
		}else{
			boardSerivce.androidBoardDeleteService(id, user);
			List<Board> boardlist = boardRepository.findAll();	
			result.put("board", boardlist);
			return result;
		}
	}
	
	@PostMapping("/auth/android/boardDetail/replyWrite")
	public Map androidreplySave(int id,String usernickname, String content, Reply reply) { // username, password, email
		
	    Map result = new HashMap<String,Object>();
	    System.out.println("?????? ????????? ??????");
	    System.out.println(id);
	    reply.setId(0);
	    Board board = boardRepository.findByBoardIdfind(id);
	   
	    System.out.println(board);
		if(board==null) {
			result.put("responseCode",  "???????????? ?????? ??????????????????.");
			return result;
		}
		User user = userRepository.findByUsernickname(usernickname);
		
		if(user==null) {
			result.put("responseCode",  "???????????? ?????? ??????????????????.");
			return result;
		}
		System.out.println(board);
		System.out.println(user);
		reply.setUser(user);
		reply.setBoard(board);
		System.out.println(reply);
		boardSerivce.androidBoardReplyWriteService(user, id, reply);
		
		
		result.put("responseCode",  "?????? ????????? ?????????????????????.");
		return result; //console(resp) ????????? resp??? 200, data??? 1 return ????????? ??????
	}
	
	@PostMapping("/auth/android/boardDetail/replyDelete")
	public Map androidReplyDelete(int id, String usernickname, int boardId){
		
		Map result = new HashMap<String,Object>();
		Reply reply = replyRepository.findByReplyIdfind(id);
		User user = userRepository.findByUsernickname(usernickname);
		if(user==null) {
			result.put("responseCode",  "???????????? ?????? ??????????????????.");
			return result;
		}
		if(reply==null) {
			result.put("responseCode",  "???????????? ?????? ???????????????.");
			return result;
		}
		
		boardSerivce.androidBoardReplyDeleteService(id,user);
		Board board = boardRepository.findById(boardId).orElseThrow(()->{
			return new IllegalArgumentException("??????????????? ??????");
		});
		
		result.put("board", board);
		return result;
		
	}

}