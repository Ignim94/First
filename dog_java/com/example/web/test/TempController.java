package com.example.web.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempController {

		@GetMapping("/temp/home")
		public String tempHome() {
			System.out.println("temphome()");
			return "/home.html"; // home.html파일을 리턴해주겠다.
		}

	@GetMapping("/temp/img")
	public String tempImg() {
		return "/star.jpg";

	}

	@GetMapping("/temp/jsp")
	public String tempJsp() {
		return "test";

	}
}
