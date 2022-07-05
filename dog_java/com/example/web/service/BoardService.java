package com.example.web.service;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.example.web.config.auth.PrincipalDetail;
import com.example.web.model.Board;
import com.example.web.model.Reply;
import com.example.web.model.User;
import com.example.web.repository.BoardRepository;
import com.example.web.repository.ReplyRepository;

@Service //스프링이 컴포넌트 스캔을 통해서 bean에 등록해줌 IoC! 메모리에 띄워주겠따..
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;

	@Transactional // 전체가 성공하면 commit, 실패하면 rollback <물론 로직 다 짜야함>
	public void 글쓰기(Board board, User user) { // title,content
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}
	
	
	
	@Transactional
	public Page<Board> 글목록(Pageable pageable){
		return boardRepository.findAll(pageable);
    }
	
	@Transactional
	public int updateView(int id) {
		return boardRepository.updateView(id);
	}
	
	
	@Transactional
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패:아이디 찾을 수 없습니다.");
				});
	}

	@Transactional
	 public void 글삭제하기(int id, PrincipalDetail principal) {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("글 찾기 실패 : 해당 글이 존재하지 않습니다.");
        });

        if (board.getUser().getId() != principal.getUser().getId()) {
            throw new IllegalStateException("글 삭제 실패 : 해당 글을 삭제할 권한이 없습니다.");
        }
        boardRepository.delete(board);
	}
	
	@Transactional
	 public void androidBoardDeleteService(int id, User user) {
       Board board = boardRepository.findById(id).orElseThrow(() -> {
           return new IllegalArgumentException("글 찾기 실패 : 해당 글이 존재하지 않습니다.");
       });

       if (board.getUser().getId() != user.getId()) {
           throw new IllegalStateException("글 삭제 실패 : 해당 글을 삭제할 권한이 없습니다.");
       }
       boardRepository.delete(board);
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패:아이디 찾을 수 없습니다.");
				}); //영속화 작업
		board.setTitle(requestBoard.getTitle());
		board.setContent_text(requestBoard.getContent_text());
		board.setContent_image(requestBoard.getContent_image());
		// 해당 함수로 종료시에 트랜잭션이 Service가 종료될 때 트랜잭션종료. 더티체킹 < 영속화로 데이터변경후 자동업데이트되는<flush> >
	}
	
	@Transactional
	public void 댓글쓰기(User user, int boardId, Reply requestReply) {
		Board board = boardRepository.findById(boardId).orElseThrow(()->{
			return new IllegalArgumentException("댓글쓰기 실패: 해당 아이디 찾을 수 없습니다.");
		});
		
		requestReply.setUser(user);
		requestReply.setBoard(board);
		
		replyRepository.save(requestReply);
	}
	
	@Transactional
	public void androidBoardReplyWriteService(User user, int id, Reply requestReply) {
		Board board = boardRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("댓글쓰기 실패: 해당 아이디 찾을 수 없습니다.");
		});
		
		requestReply.setUser(user);
		requestReply.setBoard(board);
		
		replyRepository.save(requestReply);
	}
	
	public void 댓글삭제(int replyId, PrincipalDetail principal) {
		 Reply reply = replyRepository.findById(replyId).orElseThrow(() -> {
	            return new IllegalArgumentException("댓글 찾기 실패 : 해당 댓글이 존재하지 않습니다.");
	        });

	        if (reply.getUser().getId() != principal.getUser().getId()) {
	            throw new IllegalStateException("댓글 삭제 실패 : 해당 글을 삭제할 권한이 없습니다.");
	        }
		replyRepository.deleteById(replyId);
	}
	
	public void androidBoardReplyDeleteService(int replyId, User user) {
//		 Reply reply = replyRepository.findById(replyId).orElseThrow(() -> {
//	            return new IllegalArgumentException("댓글 찾기 실패 : 해당 댓글이 존재하지 않습니다.");
//	        });
		Map result = new HashMap<String,Object>();
		
		 Reply reply = replyRepository.findByReplyIdfind(replyId);
		 	if(reply ==null) {
				result.put("responseCode",  "존재하지 않는 댓글입니다.");	
		 	}
	        if (reply.getUser().getId() != user.getId()) {
	        	result.put("responseCode","댓글 삭제 실패 : 해당 글을 삭제할 권한이 없습니다.");
	        }
		replyRepository.deleteById(replyId);
	
	}
	
	 public void contentOnlyText(String sentence, Board board) throws IOException{
		 	
		 	  System.out.println("게시판 문장 자르기<text> 부분 시작");
		 	String fullContent = sentence;
		 	board.setContent_image(null);
		 	  System.out.println("게시판 문장 자르기<text> 부분 base64 있는지 판별");
		 	if(fullContent.contains("base64")) {
			 	String[] arr = fullContent.split("<");
			 	String text0 = arr[0];
			 	board.setContent_text(text0);
			 	System.out.println("게시판 문장 자르기<text> 부분 base64 1개 있을때 저장까지 완료");
			 	System.out.println("첫< >:"+arr[1]);
			 	int num = arr[1].indexOf("data-filename");
			 	String contex = arr[1].substring(num); //22
			 	System.out.println("contex:"+contex);
			 	
			 	int num2 = contex.indexOf(">"); // context의 > 위치..
			 	String contex2 = contex.substring(num2+1);
			 	System.out.println(contex2);
			 	int contex_len = contex.length();
//			 	String context = .substring(22)
			 	if(contex_len>23) {

			 		System.out.println("사진뒤에 글:"+contex2);
			 		board.setContent_text(text0+contex2);
			 	}
		 	
//			 	System.out.println("뒤에 또 <가 존재한다면 나오는 것:"+arr[2]);
//			 		if(arr[2].contains("base64")) {
//			 			System.out.println("게시판 문장 자르기<text> 부분 base64 2개 있는지 판별");
//			 			int num1 = arr[1].indexOf(">");
//			 			String text1 = arr[1].substring(num1);
//			 			int num2 = arr[2].indexOf(">");
//			 			String text2 = arr[2].substring(num2);
//			 			String content_t = text0+text1+text2;
//					 	board.setContent_text(content_t);
//			 		}
//		 	System.out.println("*****첫문장:"+arr[0]);
//		 	System.out.println("첫< >:"+arr[1]);
//		 	System.out.println("뒤에 또 <가 존재한다면 나오는 것:"+arr[2]);
//		 	String content_t = text0+text1+text2;
//		 	board.setContent_text(content_t);
		 	}
	 }
	
	
	
	// [base64 디코딩 수행 메소드 : base64 문자열 >> 문자열 데이터]
    // [import org.springframework.util.Base64Utils;]
    public void getBase64DecodeString(String sentence, Board board) throws IOException{
    	
    	if(sentence.contains("base64")){
			int num = sentence.indexOf("data-filename=");		
			String s1 = sentence.substring(num);
			int startttttttt = s1.indexOf("\"");
			int endddddddd = s1.indexOf("\"", startttttttt+1);
			String uploadFileName = s1.substring(startttttttt+1, endddddddd);
			System.out.println("파일명:"+ uploadFileName);
			
			// 디코딩을 위한 코드 
	    	int start = sentence.indexOf("\"");
			int end = sentence.indexOf("\"", start+1);
			String list = sentence.substring(start+1, end); // img src 내부의 data:로 시작하고 =으로 끝나는 아이들
			
			String pattern = "data:image\\/[a-z]+;base64,"; // 인코딩문자 지우기 위한 정규 표현식
			String list2 = list.replaceAll(pattern, ""); // 실제 이미지의 인코딩된 url
			Decoder decoder = Base64.getDecoder();
			byte[] decodedBytes = decoder.decode(list2);
			//System.out.println("디코딩 text : " + new String(decodedBytes));
			byte[] list3 = Base64.getDecoder().decode(list2); // 디코딩 된 값 : list3
			
			
			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(list3));
			if(uploadFileName.contains("png")) {
				ImageIO.write(bufImg, "png", new File("E:\\sts-workspace\\Dog\\src\\main\\resources\\static\\image\\boardImage\\"+uploadFileName));
				String image_route = "http://localhost:8080/image/boardImage/"+uploadFileName;
				if(uploadFileName==null) {
					board.setContent_image(null);
				}
				board.setContent_image(image_route);
				System.out.println(image_route);
			}else if(uploadFileName.contains("jpg")) {
				ImageIO.write(bufImg, "jpg", new File("E:\\sts-workspace\\Dog\\src\\main\\resources\\static\\image\\boardImage\\"+uploadFileName));
				String image_route = "http://localhost:8080/image/boardImage/"+uploadFileName;
				if(uploadFileName==null) {
					board.setContent_image(null);
				}
				board.setContent_image(image_route);
				System.out.println(image_route);
			}else {
				ImageIO.write(bufImg, "jpeg", new File("E:\\sts-workspace\\Dog\\src\\main\\resources\\static\\image\\boardImage\\"+uploadFileName));
				String image_route = "http://localhost:8080/image/boardImage/"+uploadFileName;
				if(uploadFileName==null) {
					board.setContent_image(null);
				}
				board.setContent_image(image_route);
				System.out.println(image_route);
			}
			
			
//			String sentence2 = board.getContent();		
//			String arr[] = sentence2.split("<");		
//			System.out.println("첫 내용"+(arr[0])); // 글과 사진 둘 다 있을때 글..
//			String brr[] = sentence2.split(">");		
//			System.out.println("사진 뒤"+(brr[1]));
//			System.out.println("그냥 글자"+(brr[3]));
//			//String edit = arr[0]+ "구분선!," + list;
	
	        System.out.println("---끝---");		
		}
    }
	

	// [base64 디코딩 수행 메소드 : base64 문자열 >> 문자열 데이터]
    // [import org.springframework.util.Base64Utils;]
    public void androidGetBase64DecodeString(String sentence, Board board) throws IOException{
    	
    		String list2 = sentence;
    		if(list2==null) {
    			board.setContent_image(null);
    		}else {
    		String arr[] = list2.split(",");
    		
//    		System.out.println("스플릿0"+ arr[0]);
//    		System.out.println("스플릿1:"+arr[1]);
    		System.out.println(list2);	
    		String uploadFileName = arr[1];
//    		String uploadFileName = "dogdog.jpg";
    		System.out.println("파일명:"+ uploadFileName);
			
			Decoder decoder = Base64.getDecoder();
			//byte[] decodedBytes = decoder.decode(list2);
			//System.out.println("디코딩 text : " + new String(decodedBytes));
//			byte[] list3 = Base64.getDecoder().decode(list2); // 디코딩 된 값 : list2
			byte[] list3 = Base64.getDecoder().decode(arr[0]); // 디코딩 된 값 : list3
			System.out.println("안드로이드의 보드 서비스에서 디코딩된 이미지:"+list3);
			
			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(list3));
			if(uploadFileName.contains("png")) {
				ImageIO.write(bufImg, "png", new File("E:\\sts-workspace\\Dog\\src\\main\\resources\\static\\image\\boardImage\\"+uploadFileName));
				String image_route = "http://localhost:8080/image/boardImage/"+uploadFileName;
				board.setContent_image(image_route);
				System.out.println(image_route);
			}else if(uploadFileName.contains("jpg")) {
				ImageIO.write(bufImg, "jpg", new File("E:\\sts-workspace\\Dog\\src\\main\\resources\\static\\image\\boardImage\\"+uploadFileName));
				String image_route = "http://localhost:8080/image/boardImage/"+uploadFileName;
				board.setContent_image(image_route);
				System.out.println(image_route);
			}else {
				ImageIO.write(bufImg, "jpeg", new File("E:\\sts-workspace\\Dog\\src\\main\\resources\\static\\image\\boardImage\\"+uploadFileName));
				String image_route = "http://localhost:8080/image/boardImage/"+uploadFileName;
				board.setContent_image(image_route);
				System.out.println(image_route);
			}
			
    		}
			
//			String sentence2 = board.getContent();		
//			String arr[] = sentence2.split("<");		
//			System.out.println("첫 내용"+(arr[0])); // 글과 사진 둘 다 있을때 글..
//			String brr[] = sentence2.split(">");		
//			System.out.println("사진 뒤"+(brr[1]));
//			System.out.println("그냥 글자"+(brr[3]));
//			//String edit = arr[0]+ "구분선!," + list;
	
	        System.out.println("---끝---");		
	
    }
}