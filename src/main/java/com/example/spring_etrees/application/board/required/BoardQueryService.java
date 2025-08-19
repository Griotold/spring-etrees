package com.example.spring_etrees.application.board.required;

import com.example.spring_etrees.application.board.provided.BoardFinder;
import com.example.spring_etrees.domain.board.Board;
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
    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findByBoardTypeOrderByBoardNumDesc("1", pageable);
    }

    @Override
    public Board getBoard(Long boardNum) {
        return boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardNum));
    }
}
