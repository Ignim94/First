package com.example.web.Controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.config.auth.PrincipalDetail;
import com.example.web.dto.ResponseDto;
import com.example.web.model.User;
import com.example.web.service.UserService;

@RestController
public class UserApiController {

	@Autowired
	private UserService userService;
	
	@Autowired 
	private AuthenticationManager authenticationManager; // 회원정보 수정 후 세션을 재시작해주기위함

	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) { // username, password, email
		System.out.println("UserApiController: save 호출됨");
		// 실제로 DB에 insert하고 return 해보자..
		userService.회원가입(user);
		return new ResponseDto<>(HttpStatus.OK.value(), 1); //console(resp) 해보면 resp에 200, data에 1 return 되는걸 확인
		//1이면 성공, -1이면 실패..

	}

	@PutMapping("/user/update")
	public ResponseDto<Integer> update(@RequestBody User user){ //key=value, x-www-form-urlencoded
		System.out.println(user);
		System.out.println(user.getPassword());
		userService.회원수정(user);
		// 위 실행 후, 트랜잭션 종료되어 DB 값은 변경되나, 현재 로그인한 유저의 상태가 (세션의 유지) 유지되어 정보수정에 들어가보면
		// 수정화면에서는 그대로라.. 세션값을 변경해줘야한다.
		System.out.println(user.getPassword());
		// 세션 등록
		Authentication authentication =
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		System.out.println("authen:"+authentication);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
	}
	

	
	@PostMapping("/auth/mailCheck")
	public ResponseDto<Integer> idFind(@RequestBody String email){
		System.out.println("UserApiController: idFind 호출됨");
		userService.usernameFind(email);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
	}
}








//	@PutMapping("/user/update")
//	public ResponseDto<Integer> update(@RequestBody User user,
//			 @AuthenticationPrincipal PrincipalDetail principal, HttpSession session){ //key=value, x-www-form-urlencoded
//		
//		userService.회원수정(user);
//		// 위 실행 후, 트랜잭션 종료되어 DB 값은 변경되나, 현재 로그인한 유저의 상태가 (세션의 유지) 유지되어 정보수정에 들어가보면
//		// 수정화면에서는 그대로라.. 세션값을 변경해줘야한다.
//		
//		Authentication authentication = // 시큐리티의 세션생성(authentication을 생성) 로직을 타기위해 authen을 하나 만들어본다.
//				new UsernamePasswordAuthenticationToken(principal, null);
//		SecurityContext securityContext = SecurityContextHolder.getContext();//컨텍스트홀더->컨텍스트로 접근
//		//이제 시큐리티컨텍스트에 Authen객체를 넣어주면된다 
//		securityContext.setAuthentication(authentication);//세션을 바꿔주는것 
//		// 이 작업까지하면 정보수정후 로그인이 풀린다.
//		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);// 스컨이 갖고잇는 auth를 세션에 넣자
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
//	}

