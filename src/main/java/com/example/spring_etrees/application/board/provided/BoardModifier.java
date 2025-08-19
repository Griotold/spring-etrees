package com.example.spring_etrees.application.board.provided;

import com.example.spring_etrees.domain.board.BoardUpdateRequest;

public interface BoardModifier {

    /**
     * 게시글 수정
     * @param boardNum 게시글 번호
     * @param request 게시글 수정 요청
     */
    void updateBoard(Long boardNum, BoardUpdateRequest request);

    /**
     * 게시글 삭제
     * @param boardNum 게시글 번호
     */
    void deleteBoard(Long boardNum);
}