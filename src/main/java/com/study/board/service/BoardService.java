package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {


   @Autowired
   private BoardRepository boardRepository; //스프링 오토와이어 어노테이션이 빈이 읽어와서 알아서 주입해줌

    //글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception {
        if(!file.isEmpty() && !file.getOriginalFilename().isEmpty()) {


            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            board.setFilename(fileName); //데이터 넣기
            board.setFilepath("/files/" + fileName);
        }
        boardRepository.save(board);

    }
    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id) {
        return boardRepository.findById(id).get(); //get을 안 줄 경우 옵셔널 값으로 받아온다는데 모르게승ㅁ..
    }

    //특정 게시글 삭제

    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }
}