package com.example.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.dto.ResponseDto;


@ControllerAdvice//어디에서든 ,, 익셉션이 실행됐을때 이 함수를 실행시킬 것
@RestController
public class GlobalExceptionHandler {

	@ExceptionHandler(value=IllegalArgumentException.class) //해당익셉션이 발생하면 이 함수가 실행된다.
	public String handleArgumentException(IllegalArgumentException e) {
		return "<h1>"+e.getMessage()+"</h1>";
		//더미컨트롤러 딜리트에 예외처리로 작성해둔 리턴값이 겟메시지로 가져와진다
	}

	@ExceptionHandler(value=Exception.class) //해당익셉션이 발생하면 이 함수가 실행된다.
	public ResponseDto<String> handleArgumentException(Exception e) {
		return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
		//더미컨트롤러 딜리트에 예외처리로 작성해둔 리턴값이 겟메시지로 가져와진다
	}
}
