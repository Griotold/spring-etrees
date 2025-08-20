package com.example.spring_etrees.application.board;

import com.example.spring_etrees.application.board.provided.BoardFinder;
import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService implements BoardFinder {

    private final BoardRepository boardRepository;

    @Override
    public Page<Board> getBoardList(String type, Pageable pageable) {
        // "all"이면 전체 조회
        if ("all".equals(type)) {
            return boardRepository.findAllByOrderByBoardNumDesc(pageable);
        }

        // 특정 타입 조회 (BoardType Enum 변환)
        try {
            BoardType boardType = BoardType.valueOf(type.toUpperCase());
            return boardRepository.findByBoardTypeOrderByBoardNumDesc(boardType, pageable);
        } catch (IllegalArgumentException e) {
            // 잘못된 타입이면 전체 조회로 fallback
            return boardRepository.findAllByOrderByBoardNumDesc(pageable);
        }
    }

    @Override
    public Board getBoard(Long boardNum) {
        return boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardNum));
    }
}
