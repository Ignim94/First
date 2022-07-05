package com.example.web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChattController {

	
	@RequestMapping("/mychatt")
	public ModelAndView chatt() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/board/chatting");
		return mv;
	}
}
