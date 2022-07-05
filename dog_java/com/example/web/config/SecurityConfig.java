package com.example.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.example.web.config.auth.PrincipalDetailService;

import lombok.RequiredArgsConstructor;

// 설정을 위한 config 패키지

// 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration // 빈 등록(IoC 관리)
@EnableWebSecurity //필터( 세큐리티) 를 추가하는 것// 스프링 시큐리티 활성화중에 어떤 설정을 해당 파일에서 하겠다.
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
//	private final CorsFilter corsFilter;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	

	@Autowired
	private PrincipalDetailService principalDetailService;
	@Bean // 리턴하는 값을 IoC화하여 스프링이 관리하겠다.
	public BCryptPasswordEncoder encodePWD() {
	// String encPassword = new BCryptPasswordEncoder().encode("1234");
		return new BCryptPasswordEncoder();
	}

	// 시큐리티가 대신 로그할때 password를 가로채기 하는데
	// 해당 password의 해시화된 값을 알도록하고 DB의 해쉬와 비교할 수 있게 만들어줘야함
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD()); // userDetailsService(null)자리에있는 오브젝트에게 알려줘야하는것
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	//	http.addFilterAfter(new MyFilter3(), BasicAuthenticationFilter.class);
	//	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);	
		http
		.csrf().disable() // csrf 토큰 비활성화	   
		.authorizeRequests()//요청이 들어오면
		.antMatchers("/","/token","/home","/auth/**","/js/**","/css/**","/image/**","/dummy/**","/user/passwordReset","/auth/android/**") //auth로
		.permitAll() // 다 허락해줌
		.anyRequest() // 외의 다른 모든 요청은
		.authenticated() // 인증을 받아야해
      .and()
  //   	.addFilter(corsFilter) // @CrossOrigin(인증x), 시큐류티 필더에 등록 인증(o)
      	.formLogin()//.disable() // JWT 토근스면 DISABLE작성. 아닐땐 지워.
  //   	.httpBasic().disable(); // jwt 토큰쓰면 선언..
      	.loginPage("/auth/loginForm")
		.loginProcessingUrl("/auth/loginProc") // 로그인 가로채서 대신 로그인 한다.
		.defaultSuccessUrl("/"); // 정상완료시, 로그인하고 사용자를 url /로 보내준다.

	}
}
