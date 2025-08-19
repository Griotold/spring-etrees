package com.example.spring_etrees.application.board.provided;

public interface BoardCreator {

    /**
     * 게시글 생성
     * @param boardTitle 게시글 제목
     * @param boardComment 게시글 내용
     * @return 생성된 게시글 ID
     */
    Long createBoard(String boardTitle, String boardComment);
}
