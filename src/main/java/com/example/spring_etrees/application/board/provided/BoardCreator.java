package com.example.spring_etrees.application.board.provided;

import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardCreateRequest;

public interface BoardCreator {

    /**
     * 게시글 생성
     * @param request 게시글 생성 요청
     * @param creator 작성자
     * @return 생성된 게시글 엔티티
     */
    Board createBoard(BoardCreateRequest request, String creator);
}
