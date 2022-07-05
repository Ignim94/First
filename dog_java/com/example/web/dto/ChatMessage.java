package com.example.web.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
	public enum MessageType{
        ENTER, TALK
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}