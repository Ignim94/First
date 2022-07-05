package com.example.web.Controller;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.dto.ChatMessage;
import com.example.web.model.User;
// import com.example.web.service.UserEmailService;
import com.example.web.repository.UserRepository;
import com.example.web.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserEmailController {
	
	private static final Logger log = LoggerFactory.getLogger(UserEmailController.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(UserEmailController.class);
	
//	private final UserEmailService usermailService;

	@Autowired // 메일 서비스 기능을 이용하기 위한 ㅡㅡ 
    private JavaMailSender javamailSender;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserService userService;
	
	
//////////////////////////////////////////////////////////////////////////////////
	
	// 회원가입 : 이메일 인증
	@GetMapping("/dummy/mailCheck")
	public String SendMail(String email) throws Exception{
		
		/* 뷰(View)로부터 넘어온 데이터 확인 */
        LOGGER.info("이메일 데이터 전송 확인");
        LOGGER.info("인증번호 : " + email);
                
        /* 인증번호(난수) 생성 */
        Random random = new Random();
        int checkNum = random.nextInt(888888) + 111111;
        LOGGER.info("인증번호 " + checkNum);
        
        /* 이메일 보내기 */
        String setFrom = "danbi97771@gmail.com";
        String toMail = email;
        String title = "회원가입 인증 이메일 입니다.";
        String content = 
                "밍기네 홈페이지를 방문해주셔서 감사합니다." +
                "<br><br>" + 
                "인증 번호는 " + checkNum + "입니다." + 
                "<br>" + 
                "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
        // 데이터가 넘어오는지 확인하기 위해 작성했던 코드임 
        try {
            
            MimeMessage message = javamailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            javamailSender.send(message);
            
        }catch(Exception e) {
            e.printStackTrace();
        } 
        String num = Integer.toString(checkNum);
        return num;
    }
	
	// Id찾기 : 이메일 인증
	@GetMapping("/auth/mailCheck")
	public String SendMailIdFind(String email) throws Exception{
		
		/* 뷰(View)로부터 넘어온 데이터 확인 */
        LOGGER.info("이메일 데이터 전송 확인");
        LOGGER.info("Email : " + email + "입니다.");
                
        User user = userService.findByUserNameIdfind(email);
        System.out.println(user);
        
        String userId;
        
        userId = user.getUsername();
        System.out.println(userId+"없어");
        /* 이메일 보내기 */
        String setFrom = "danbi97771@gmail.com";
        String toMail = email;
        String title = "개르만족 Id찾기 결과";
        String content = 
                "밍기네 홈페이지를 방문해주셔서 감사합니다." +
                "<br><br>" + 
                "회원님의 Id는 " + userId + "입니다." + 
                "<br>"; 
        // 데이터가 넘어오는지 확인하기 위해 작성했던 코드임 
        try {
            
            MimeMessage message = javamailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            javamailSender.send(message);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        if(userId==null) {
			  System.out.println("user가없습니다.");
			  return "fail";

		  }else {		  
			  return "success";	
		  }
    }
	/////////////////////////////      비밀번호 찾기	 
	
	 @PostMapping("/auth/mailCheck/findPw")
	 public String SendMailPwFind(String username, String email) throws Exception{	        
	        /* 뷰(View)로부터 넘어온 데이터 확인 */
	        LOGGER.info("이메일 데이터 전송 확인");
	        LOGGER.info("username : " + username);
	        LOGGER.info("email : " + email);
	        
	        /* 인증번호(난수) 생성 */
	        Random random = new Random();
	        int checkNum = random.nextInt(888888) + 111111;
	        LOGGER.info("임시비밀번호: " + checkNum);
	        System.out.println();
	        String rawPassword= Integer.toString(checkNum);
	 	            
	        /* 이메일 보내기 */
	        String setFrom = "danbi97771@gmail.com";
	        String toMail = email;
	        String title = "개르만족 PW찾기 ";
	        String content = 
	                "밍기네 홈페이지를 방문해주셔서 감사합니다." +
	                "<br><br>" + 
	                "회원님의 임시password는 " +  rawPassword  + "입니다." + 
	                "<br>"; 
	        // 데이터가 넘어오는지 확인하기 위해 작성했던 코드임 
	        System.out.println(rawPassword);	        
	        User user = userRepository.findByUsernameIdfind(email);
	        System.out.println(user);
	        if(user!=null && user.getUsername().equals(username)) {	        	
			  try {
	 	            
	 	            MimeMessage message = javamailSender.createMimeMessage();
	 	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
	 	            helper.setFrom(setFrom);
	 	            helper.setTo(toMail);
	 	            helper.setSubject(title);
	 	            helper.setText(content,true);
	 	            javamailSender.send(message);
	 	            System.out.println("바꾸려는 user:"+user);
	 	            user.setPassword(rawPassword);
	 	            System.out.println("현재 비번:" + user.getPassword());
	 	            userService.회원비번찾기(user);
	 	            System.out.println("바꾼 비번:"+user.getPassword());
	 	            
	 	        }catch(Exception e) {
	 	            e.printStackTrace();
	 	        }
	        	 return "success";  				  
	        }
	        else {
	        	System.out.println("user가없습니다.");
				return "fail";
	        }
	        	       
	 }

	  
	// 아이디 중복 검사
	@PostMapping(value = "/dummy/member/memberIdChk")
	public String memberIdChkPOST(String username) throws Exception{
		
		LOGGER.info("memberIdChk() 진입");
		
		 User user = userService.findByUserNameOne(username);
		 System.out.println(user);
		  if(user==null) {
			  System.out.println("user가없습니다.");
			  return "success";
			  		
		  }else if(username==""){
			  return "blank";
		  }else {
		  
			  System.out.println(user.toString());
			  return "fail";	
		  }
		 
	} // memberIdChkPOST() 종료	
	
	// 닉네임 중복 검사
	@PostMapping(value = "/dummy/user/usernicknameChk")
	public String userNickNameChkPOST(String usernickname) throws Exception{
		
		LOGGER.info("usernickcnameChk() 진입");
		
		 User user = userRepository.findByUsernickname(usernickname);
		System.out.println(usernickname);
		  if(user==null) {
			  System.out.println("user가없습니다.");
			  return "success";
			  		
		  }else if(usernickname==""){
			  return "blank";
		  }else {
		  
			  System.out.println(user.toString());
			  return "fail";	
		  }
		 
	} // memberIdChkPOST() 종료	
	
}
