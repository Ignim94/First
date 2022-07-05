package com.example.web.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.web.model.User;

import lombok.Data;

//로그인 완료시, UserDetails타입의 오브젝트를 시큐리티의 고유 세션저장소에 저장해준다.(principalDetail을 저장)
@Data
public class PrincipalDetail implements UserDetails {
  private User user; // 컴포지션 ( 객체를 품고있음 )

  public PrincipalDetail(User user) {
	  this.user=user;
  }
//
@Override
public String getPassword() {
	// TODO Auto-generated method stub
	return user.getPassword();
}

@Override
public String getUsername() {
	// TODO Auto-generated method stub
	return user.getUsername();
}


@Override // 계정이 만료되었는가? 트루면 만료 x
public boolean isAccountNonExpired() {
	// TODO Auto-generated method stub
	return true;
}

@Override // 계정이 잠겨있는가? 트루면 안잠김
public boolean isAccountNonLocked() {
	// TODO Auto-generated method stub
	return true;
}

@Override // 비밀번호가 만료되었는가? 트루면 만료안됨
public boolean isCredentialsNonExpired() {
	// TODO Auto-generated method stub
	return true;
}

@Override // 계정이 활성화(사용가능)인가? 트루면 사용가능
public boolean isEnabled() {
	// TODO Auto-generated method stub
	return true;
}


@Override //GrantedAuthority를 상속한 컬랙션타입 계정의 권한
public Collection<? extends GrantedAuthority> getAuthorities() {
	Collection<GrantedAuthority> collectors = new ArrayList<>();
//	collectors.add(new GrantedAuthority() {
//
//		@Override
//		public String getAuthority() {
//			return "ROLE_"+user.getRole(); // 지금 로그인한사람의 권한을 받아와야함..정해진형태.
//		}
//	}); 이 주석문들을 람다식으로 표현한게 아래임.
	collectors.add(()->{return "ROLE_"+user.getRole();});
	return collectors;
}

}
