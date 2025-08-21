package com.example.spring_etrees.application.board.provided;

import com.example.spring_etrees.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardFinder {

    /**
     * 게시글 목록 조회 (여러 타입 지원)
     * @param types 게시판 타입 목록
     * @param pageable 페이지 번호 (1부터 시작)
     * @return 게시글 목록 (5건씩)
     */
    Page<Board> getBoardListByTypes(List<String> types, Pageable pageable);

    /**
     * 게시글 상세 조회
     * @param boardNum 게시글 번호
     * @return 게시글 정보
     */
    Board getBoard(Long boardNum);
}
