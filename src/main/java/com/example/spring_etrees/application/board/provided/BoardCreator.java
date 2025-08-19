package com.example.spring_etrees.application.board.provided;

import com.example.spring_etrees.domain.board.BoardCreateRequest;

public interface BoardCreator {

    /**
     * 게시글 생성
     * @param request 게시글 생성 요청
     * @return 생성된 게시글 ID
     */
    Long createBoard(BoardCreateRequest request);
}
