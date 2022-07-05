package com.example.web.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.dto.ChatRoom;
import com.example.web.service.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
	
	private final ChatService chatservice;
	
	private ChatRoom createRoom(@RequestBody String name) {
		return chatservice.createRoom(name);
	}
	
	@GetMapping
	public List<ChatRoom> findAllRoom(){
		return chatservice.findAllRoom();
	}
}
