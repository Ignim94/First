package com.example.web.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.web.model.User;
import com.example.web.repository.UserRepository;

@Service
public class PrincipalDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override //스프링이 로그인요청을 가로챌 때 username,password 두 개를 가로챔. //password 부분 처리는 알아서 함 // username이 db에 있는지만 확인해주면 돼
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findByUsername(username)// 옵셔널타입이므로
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.:"+username);
				});
		return new PrincipalDetail(principal); // 시큐리티의 세션에 유저정보를 저장
	}
}

