package com.example.web.Controller.android;

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
public class AndroidGpsController {
	
	private static final Logger log = LoggerFactory.getLogger(AndroidGpsController.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(AndroidGpsController.class);
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


	@PostMapping("/auth/android/distance")
	public Map GpsList (@RequestParam(required = false) String distance, @RequestParam(required = false) String timer){
		  System.out.println("안드로이드 GPS 접속");
		  System.out.println(distance);
		  System.out.println(timer);
		
		  Map result = new HashMap<String,Object>();
		  result.put("거리", distance);
		  result.put("시간", timer);
		  System.out.println("result:"+result);
		 
		  System.out.println("거리시간 저장");
		  return result;
	}
	
}