package com.example.spring_etrees.domain.board;

public class BoardFixture {

    /**
     * 기본 BoardCreateRequest 생성 (일반 타입)
     */
    public static BoardCreateRequest createBoardCreateRequest() {
        return createBoardCreateRequest(BoardType.GENERAL, "테스트 제목", "테스트 내용");
    }

    /**
     * 제목과 내용을 지정한 BoardCreateRequest 생성 (일반 타입)
     */
    public static BoardCreateRequest createBoardCreateRequest(String title, String content) {
        return createBoardCreateRequest(BoardType.GENERAL, title, content);
    }

    /**
     * 타입을 지정한 BoardCreateRequest 생성
     */
    public static BoardCreateRequest createBoardCreateRequest(BoardType boardType) {
        return createBoardCreateRequest(boardType, "테스트 제목", "테스트 내용");
    }

    /**
     * 모든 값을 지정한 BoardCreateRequest 생성
     */
    public static BoardCreateRequest createBoardCreateRequest(BoardType boardType, String title, String content) {
        return new BoardCreateRequest(boardType, title, content);
    }

    /**
     * 기본 BoardUpdateRequest 생성
     */
    public static BoardUpdateRequest createBoardUpdateRequest() {
        return createBoardUpdateRequest("수정된 제목", "수정된 내용");
    }

    /**
     * 제목과 내용을 지정한 BoardUpdateRequest 생성
     */
    public static BoardUpdateRequest createBoardUpdateRequest(String title, String content) {
        return new BoardUpdateRequest(title, content);
    }

    /**
     * 기본 Board 엔티티 생성
     */
    public static Board createBoard() {
        return Board.create(createBoardCreateRequest());
    }

    /**
     * 타입을 지정한 Board 엔티티 생성
     */
    public static Board createBoard(BoardType boardType) {
        return Board.create(createBoardCreateRequest(boardType));
    }

    /**
     * 모든 값을 지정한 Board 엔티티 생성
     */
    public static Board createBoard(BoardType boardType, String title, String content) {
        return Board.create(createBoardCreateRequest(boardType, title, content));
    }
}