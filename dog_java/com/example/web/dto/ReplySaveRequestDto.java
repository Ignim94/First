package com.example.web.dto;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.web.config.auth.PrincipalDetail;
import com.example.web.model.Reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplySaveRequestDto {
	public int userId;
	public int boardId;
	public String content;
}

/* 위 DTO를 사용할 경우.
	public ResponseDto<Integer> replySave
(@PathVariable int boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal) { // username, password, email
 처럼 하나하나 만들어주지 않고
  public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto reply)
로 받아올 수 있다.. 부족한건 .jsp에서 <input type="hidden" id="userId" valye="${principal.user.id}"
처럼 받아놓을 수 있음


*/