package com.example.web.service;


import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.web.model.RoleType;
import com.example.web.model.User;
import com.example.web.repository.UserRepository;

@Service //스프링이 컴포넌트 스캔을 통해서 bean에 등록해줌 IoC! 메모리에 띄워주겠따..
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired 
	private AuthenticationManager authenticationManager; // 회원정보 수정 후 세션을 재시작해주기위함
	
	@Autowired // 메일 서비스 기능을 이용하기 위한 ㅡㅡ 
    private JavaMailSender javamailSender;
	
	@Transactional // 전체가 성공하면 commit, 실패하면 rollback <물론 로직 다 짜야함>
	public void 회원가입(User user) {
		String rawPassword = user.getPassword(); // 1234 원문
		String encPassword = encoder.encode(rawPassword); // 해쉬
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);

	}

	@Transactional
	public void 회원수정(User user) {
		// 수정시에는 영속성컨텍스트에 User 오브젝트를 영속화시키고, 영속화된 user오브젝트를 수정하고 flush하는것
		// select를 통해 영속화.
		User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("회원찾기실패");
		});
		String rawPassword= user.getPassword();
		String encPassword= encoder.encode(rawPassword);
		persistance.setPassword(encPassword);
		persistance.setEmail(user.getEmail());
		//회원수정 함수의 종료 = service의 종료 = 트랜잭션의 종료= flush가 되었다.
		
	}
	
	@Transactional
	public void 회원비번찾기(User user) {
		// 수정시에는 영속성컨텍스트에 User 오브젝트를 영속화시키고, 영속화된 user오브젝트를 수정하고 flush하는것
		// select를 통해 영속화.
		
		String rawPassword= user.getPassword();
		String encPassword= encoder.encode(rawPassword);
		user.setPassword(encPassword);
		//회원수정 함수의 종료 = service의 종료 = 트랜잭션의 종료= flush가 되었다.
		
	}

	@Transactional
	public void usernameFind(String email) {
						 
		User user = userRepository.findByUsernameIdfind(email);
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
	}
//	// 회원 PW 찾기 < 입력 , DB 비교 >
//	public boolean userEmailCheck(String email, String username) {
//
//        User user = userRepository.findByUsernameIdfind(email);
//        if(user!=null && user.getUsername().equals(username)) {
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
	
	
	public User findByUserNameOne(String username) {
		return userRepository.findByUsernameOne(username);
	}
	
	public User findByUserNameIdfind(String useremail) {
		return userRepository.findByUsernameIdfind(useremail);
	}
	
	public User findByUsernicknameIdfind(String usernickname) {
		return userRepository.findByUsernickname(usernickname);
	}
//	@Transactional(readOnly=true) // select할 때 트랜잭션 시작, 서비스 종료시 트랜잭션 종료 ( 정합성 유지 )
//	public User 로그인(User user){
//		return userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());
//	}
//
}
