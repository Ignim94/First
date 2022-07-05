package com.example.web.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//사용자가 요청-> 응답(HTML 파일)
// @Controller 라고 해야함 << 스프링에서 하던것.

@RestController // 사용자가 요청 -> 응답 (Data에 관한)
public class HttpController {

	private static final String TAG="HttpControllerTest";

	@GetMapping("/http/lombok")
	public String lombokTest() {
		Member m = new Member(1,"ssar","1234","email");
		System.out.println(TAG+"getter:"+m.getId());
		m.setId(5000);
		System.out.println(TAG+"setter:"+m.getId());
		return "lombok test 완료";
	}

		// http://localhost:8080/http/get
		@GetMapping("/http/get")
		public String getTest(Member m) {
			return "get요청 : " + m.getId() +",username : "+ m.getUsername() +","+ m.getPassword()+","+m.getEmail();

		}

		// http://localhost:8080/http/get
		@PostMapping("/http/post")
		public String postTest(@RequestBody Member m) { //바디 데이터(raw)에 받기때문에 써줘야함
		   return "post요청 : " + m.getId() +",username : "+ m.getUsername() +","+ m.getPassword()+","+m.getEmail();
		   //json 데이터로 보내면 파싱되서 키 밸류 값으로 작성해도 겟처럼 한줄로 술술넘어옴 ( member라는 오브젝트에 멥핑해서 알아서 넘어온다)
		   //text로 보내면 member는 제대로 받지못함. 스트링부트의 메세지컨버터가 알아서 일해주는 것
		}

		@PutMapping("/http/put")
		public String putTest(@RequestBody Member m) {
			return "put요청 : " + m.getId() +",username : "+ m.getUsername() +","+ m.getPassword()+","+m.getEmail();
		}

		@DeleteMapping("/http/delete")
		public String deleteTest() {
						return "delete요청";

		}
}