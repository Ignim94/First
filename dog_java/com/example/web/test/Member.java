package com.example.web.test;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter, setter를 대신해준다 << lombok으로 가능
@NoArgsConstructor	// private final <<의 변수들의 값이 변하지않게 유지하기위해 < 불변성을 준다
					// final이 붙은 애들에 대한 constructor를 생성해주는 것
public class Member {
	private int id;
	private String username;
	private String password;
	private String email;

	@Builder
	public Member(int id, String username, String password, String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}



	//변수는 private로 만들고 그걸 다른데서 수정할 수 있게 게터세터 하는것
	// 객체 지향!



}
