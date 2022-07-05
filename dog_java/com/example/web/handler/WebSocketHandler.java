package com.example.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.web.Controller.MainController;
import com.example.web.dto.ChatMessage;
import com.example.web.dto.ChatRoom;
import com.example.web.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
	
	private final ObjectMapper objectMapper;
	private final ChatService chatService;
	
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("{}", payload);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        
        ChatRoom chatRoom = chatService.findRoomById(chatMessage.getRoomId());
        chatRoom.handlerActions(session, chatMessage, chatService);
    }
}