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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.example.web.repository.UserRepository;
import com.example.web.service.BoardService;
import com.example.web.service.UserService;

@RestController
public class AndroidUserController {
	
	private static final Logger log = LoggerFactory.getLogger(AndroidUserController.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(AndroidUserController.class);
	private static final int String = 0;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardSerivce;

	@GetMapping("/auth/android")
	public String getTest(HttpSession session, @RequestParam(required = false) String mem_id,@RequestParam(required = false) String mem_pw ) { //바디 데이터(raw)에 받기때문에 써줘야함
		  System.out.println("get success");

		  return "get";
		   //  return "post요청 : " + user.getUsername()+" ,"+ user.getPassword();
		   //json 데이터로 보내면 파싱되서 키 밸류 값으로 작성해도 겟처럼 한줄로 술술넘어옴 ( member라는 오브젝트에 멥핑해서 알아서 넘어온다)
		   //text로 보내면 member는 제대로 받지못함. 스트링부트의 메세지컨버터가 알아서 일해주는 것
		}


	@PostMapping("/auth/android")
	public String postTest(@RequestParam(required = false) String username,@RequestParam(required = false) String password ) { //바디 데이터(raw)에 받기때문에 써줘야함
		  System.out.println("post success");
		  
		  System.out.println(username);
		  System.out.println(password);

		  return "home";
	}


//		  Map result = new HashMap<String,Object>();
//		  result.put("username", user.getUsername());
//		  result.put("password", user.getPassword());
//		  return result;
		   //  return "post요청 : " + user.getUsername()+" ,"+ user.getPassword();
		   //json 데이터로 보내면 파싱되서 키 밸류 값으로 작성해도 겟처럼 한줄로 술술넘어옴 ( member라는 오브젝트에 멥핑해서 알아서 넘어온다)
		   //text로 보내면 member는 제대로 받지못함. 스트링부트의 메세지컨버터가 알아서 일해주는 것


	@GetMapping("/auth/android/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}

	@PostMapping("/auth/android/join")
	public Map join(User user,BindingResult bindingResult) {//key=value(약속된 규칙)
		System.out.println("안드로이드 회원가입");
	//RequestParam("userid") String u, - - - - 로도 가능
		System.out.println("id:"+user.getId());
		System.out.println("username:"+user.getUsername());
		System.out.println("password:"+user.getPassword());
		System.out.println("usernickname:"+user.getUsernickname());
		System.out.println("email:"+user.getEmail());
		System.out.println("address:"+user.getAddress());
		System.out.println("role:"+user.getRole());
		System.out.println("createDate:"+user.getCreateDate());

		String rawPassword = user.getPassword(); // 1234 원문
		String encPassword = encoder.encode(rawPassword); // 해쉬
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		user.setRole(RoleType.USER); // USER의 롤 타입
		userRepository.save(user);
		Map result = new HashMap<String,Object>();
		result.put("responseCode","success");
		return result;

	}
	
	
	
	@PostMapping("/auth/android/login3")
	@ResponseBody
	public Map postTest3( HttpServletRequest request,HttpSession session, @RequestParam(required = false) String username,@RequestParam(required = false) String password) { 
		  System.out.println("login3 post success 이게 진짜 로그인");
		  System.out.println(username);
		  System.out.println(password);
		 
		  Map result = new HashMap<String,Object>();
		  Map result2 = new HashMap<String,Object>();
		  result2.put("id", null);
		  result2.put("username", null);
		  result2.put("password", null);
		  result2.put("usernickname", null);
		  result2.put("email", null);
		  result2.put("address", null);
		  result2.put("role", null);			  
		  result2.put("createDate", null);
		  
		  User user = userRepository.findByUsernameOne(username);
		  System.out.println("loginUser:"+user);
		  		  
		  if(user==null) { 
			  result.put("responseCode", result2);
			  System.out.println(result);
			  return result;
		  }
		  boolean compose;
		  compose = encoder.matches(password, user.getPassword());
		  System.out.println(compose);
  		  
		  if(compose==true) {
			  result.put("responseCode", user);
			  return result;			  				  
		  }else {
			  result.put("responseCode", result2);
			  System.out.println(result);
			  return result;
		  }
	}
	
	@PostMapping("/auth/android/login4")
	@ResponseBody
	public User postTest4(HttpServletRequest request, @RequestParam(required = false) String username,@RequestParam(required = false) String password) { 
		  System.out.println("login4 post success");
		  System.out.println("username:"+username);
		  System.out.println("password:"+password);
		 // Map result = new HashMap<String,Object>();
		  
		  User user = userRepository.findByUsernameOne(username);
		  if(user==null) {
		//	  result.put("responseCode","ID 혹은 PW가 틀립니다.");
			  return user;		  				  
		  }else {
		//	  result.put("responseCode",user);
			  return user;
		  }
		   
	}
	
	//@RequestMapping("/andLogin.do")
	//public User andLogin(user user) {
	//	User info = userRepository.
	//}
	
	@PostMapping("/auth/android/login2")
	@ResponseBody
	public Map postTest2(User user) { 
		  System.out.println("lgin2 success");
		  System.out.println(user.getUsername());
		  System.out.println(user.getPassword());

		   Map result = new HashMap<String,Object>();
		  result.put("username", user.getUsername());
		  result.put("password", user.getPassword());
		  result.put("email", user.getEmail());
		  result.put("usernickname", user.getUsernickname());
		  result.put("join","success");
		  
//		  User user1 = userRepository.findByUsername("username").orElseThrow(()->{ //영속화해주는것
//				return new IllegalArgumentException("수정에 실패하였습니다.");
//			});
		  return result;
	}
	
	
	@PostMapping("/auth/android/usernameCheck")
	public Map usernameCheck(HttpServletRequest request, @RequestParam(required = false) String username) { 
		 // System.out.println("post success");		  
		  System.out.println(username);
		  Map result = new HashMap<String,String>();
		 
		  User user = userService.findByUserNameOne(username);
		  System.out.println(user);
		  if(user!=null) {
			  result.put("responseCode","이미 존재하는 ID입니다.");
			  return result;
		  }
				  
//		  if(user==null) {
//			  System.out.println("user가없습니다.");			  				  
//		  }else {
//			  System.out.println(user.toString());
//		  }
		  result.put("responseCode", "사용 가능한 ID입니다.");
		  return result;		  
	}
	
	@PostMapping("/auth/android/usernicknameCheck")
	public Map usernicknameCheck(HttpServletRequest request, @RequestParam(required = false) String usernickname) { 
		 // System.out.println("post success");		  
		  System.out.println(usernickname);
		  Map result = new HashMap<String,String>();
		 
		  User user = userService.findByUsernicknameIdfind(usernickname);
		  System.out.println(user);
		  if(user!=null) {
			  result.put("responseCode","이미 존재하는 닉네임입니다.");
			  return result;
		  }

		  result.put("responseCode", "사용 가능한 닉네임입니다.");
		  return result;		  
	}
}
