package com.example.web.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.web.config.auth.PrincipalDetail;
import com.example.web.repository.UserRepository;

// 인증이 되지않은 사용자들의 출입경로는 /auth/**를 통하도록 하기 위함.
// 그냥 주소가 /인 경우를 허용해주기 위함
// static 이하의 resource 파일(js/**, /css/**, /image/**)
// 인증이 필요없는 것들.
@Controller
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}

	@GetMapping("/user/updateForm")
	public String updateForm(@AuthenticationPrincipal PrincipalDetail principal){ //SecurityContextHolder의 Authentication 객체를 가져온다.
		
		return "user/updateForm";
	}
	
	@GetMapping("/user/passwordReset")
	public String FwupdateForm(@AuthenticationPrincipal PrincipalDetail principal){ //SecurityContextHolder의 Authentication 객체를 가져온다.
		
		return "user/passwordReset";
	}
	
	@GetMapping("/auth/findId")
	public String userIdFind() {
		return "user/findId";
	}
	
	@GetMapping("/auth/findPw")
	public String userPwFind() {
		return "user/findPw";
	}
}
