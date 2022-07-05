package com.example.web.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.model.User;
import com.example.web.repository.UserRepository;

@RestController
public class DummyControllerTest {

	@Autowired //의존성 주입(DI) DummyControllerTest라는애가 메모리에 뜰대 밑의 유저래포지토리도 메모리로 떠라!
	private UserRepository userRepository;

	@DeleteMapping("/dummy/user/{id}")
	public String deleteUser(@PathVariable int id) {
	try {
		userRepository.deleteById(id);
		}catch(EmptyResultDataAccessException e) { //Exception e 라고 해도 상관은없음. empty가 여기선 더 정확한 excep
			return "DB에 존재하지 않는 ID입니다."+id+"번";
		}return "삭제 되었습니다.id:"+id+"번";
	}

	//save 함수는 id를 전달하지 않으면 insert를 수행. id를 전달시,
	// id에 대한 데이터가 있으면 update를 수행, 없으면 insert를 수행
	//email, password
	@Transactional
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
	//json데이터를 요청 -> java object(messageconverter의 jackson 라이브러리가 변환해서 받아준다)-> 이때 필요한 라이브러리가 requestbody
		System.out.println("id:"+id);
		System.out.println("password:"+requestUser.getPassword());
		System.out.println("email:"+requestUser.getEmail());

		User user = userRepository.findById(id).orElseThrow(()->{ //영속화해주는것
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(requestUser.getPassword()); // 업데이트하면서 영속화된 개체가 변경을감지
		user.setEmail(requestUser.getEmail());	// DB에 수정하라고 한다 << 더티체킹방식

		// userRepository.save(user);
		// 더티체킹:
		// 트랜잭셔널을 붙여주면 save가 없어도 업데이트가 실행 된다.
		return user;  // 함수 종료시에 자동 COMMIT이 된다.
	}


	@GetMapping("/dummy/users")
	public List<User> list(){

		return userRepository.findAll();

	}

	//한 페이지당 2건의 데이터를 리턴받아 볼 예정
	@GetMapping("/dummy/user") //아래는 2건씩 id를 최신순으로 분류하여 페이징하겠다는 말
	public Page<User> pageList(@PageableDefault(size=6,sort="id",direction=Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);// getC 쓰면 내용물만나옴

		List<User> users = pagingUser.getContent();
		return pagingUser;
	}

	@GetMapping("/dummy/user/{id}")
	// {id} 주소로 파라미터를 전달받을 수 있음 http://localhost:8000/blog/dummy/user/3
	public User detail(@PathVariable int id)/*id가 int타입임*/{
		//.get: null일리가없을때 사용/ .orElseGet : supplier
		//findById는 optional 타입인데.. 만약 데이터베이스에서 user/4를 찾고싶은데 못찾을 경우
		//user에 null값이 반영,리턴되는데 이걸 optional로 감싸서 null인지 아닌지를 한번더 체크하고 return함
		User user  = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			// 정상적으로 셀렉트가 됐다면 이 안의 오버라이트 함수를 타지않을것임
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없는 유저입니다. id:"+id);

			}
		});
		// 리턴되는 user 객체는 자바오브젝트. 요청 처리는 웹 브라우저인데..
		// RestController라서 데이터를 리턴해줌.. 웹브라우저는 이 유저 데이터를 인지할수없음( html 이런걸 인지함)
		// 변환을 해줘야함 ( 웹브라우저가 이해할 수 있는 데이터로) -> json(Gson 라이브러리)
		// 스프링부트 = MessageConverter라는 애가 응답시에 자동 작동
		// 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서 유저 오브젝트를 json으로 변환해서 브라우저에게 줌
		return user;
	}

//	//http://localhost:8000/dummy/join(요청)
//	//http의 body에 username, password, email 데이터를 가지고 (요청)
//	@PostMapping("/dummy/join")
//	public String join(String username,String password,String email) {//key=value(약속된 규칙)
//	//RequestParam("username") String u, - - - - 로도 가능
//		System.out.println("username:"+username);
//		System.out.println("username:"+password);
//		System.out.println("username:"+email);
//		return "회원가입이 완료되었습니다.";
//	}
//
//	@PostMapping("/dummy/join")
//	public String join(User user) {//key=value(약속된 규칙)
//	//RequestParam("username") String u, - - - - 로도 가능
//		System.out.println("id:"+user.getId());
//		System.out.println("username:"+user.getUsername());
//		System.out.println("password:"+user.getPassword());
//		System.out.println("email:"+user.getEmail());
//		System.out.println("role:"+user.getRole());
//		System.out.println("createDate:"+user.getCreateDate());
//
//		user.setRole(RoleType.USER); // USER의 롤 타입
//		userRepository.save(user);
//		return "회원가입이 완료되었습니다.";
//	}
}
