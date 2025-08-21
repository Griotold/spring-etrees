package com.example.spring_etrees.application.board.required;

import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    /**
     * 전체 게시글 목록 조회 (게시글 번호 내림차순)
     * @param pageable 페이징 정보
     * @return 전체 게시글 목록 (페이징)
     */
    Page<Board> findAllByOrderByBoardNumDesc(Pageable pageable);

    /**
     * 여러 게시판 타입의 게시글 목록 조회 (게시글 번호 내림차순)
     * @param boardTypes 게시판 타입 목록
     * @param pageable 페이징 정보
     * @return 게시글 목록 (페이징)
     */
    Page<Board> findByBoardTypeInOrderByBoardNumDesc(List<BoardType> boardTypes, Pageable pageable);

    /**
     * 게시판 타입별 게시글 개수
     * @param boardType 게시판 타입
     * @return 게시글 개수
     */
    long countByBoardType(BoardType boardType);
}
