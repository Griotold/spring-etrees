package com.example.spring_etrees.application.board.provided;

import com.example.spring_etrees.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardFinder {

    /**
     * 게시글 목록 조회 (타입별 필터링 지원)
     * @param type 게시판 타입 ("all" 또는 BoardType name)
     * @param pageable 페이지 번호 (1부터 시작)
     * @return 게시글 목록 (5건씩)
     */
    Page<Board> getBoardList(String type, Pageable pageable);

    /**
     * 게시글 상세 조회
     * @param boardNum 게시글 번호
     * @return 게시글 정보
     */
    Board getBoard(Long boardNum);
}
