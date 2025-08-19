package com.example.spring_etrees.application.board;

import com.example.spring_etrees.application.board.provided.BoardCreator;
import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardModifyService implements BoardCreator {

    private final BoardRepository boardRepository;

    @Override
    public Long createBoard(String boardTitle, String boardComment) {
        // 도메인 메서드 사용하여 게시글 생성
        Board board = Board.create(boardTitle, boardComment);

        // 저장 후 생성된 ID 반환
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getBoardNum();
    }
}