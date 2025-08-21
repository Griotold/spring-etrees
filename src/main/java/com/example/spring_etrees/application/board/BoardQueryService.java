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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService implements BoardFinder {

    private final BoardRepository boardRepository;

    @Override
    public Page<Board> getBoardListByTypes(List<String> types, Pageable pageable) {
        // "all"이 포함되어 있거나 types가 비어있으면 전체 조회
        if (types.contains("all") || types.isEmpty()) {
            return boardRepository.findAllByOrderByBoardNumDesc(pageable);
        }

        // String 타입을 BoardType enum으로 변환
        List<BoardType> boardTypes = types.stream()
                .map(type -> {
                    try {
                        return BoardType.valueOf(type);
                    } catch (IllegalArgumentException e) {
                        // 잘못된 타입은 무시
                        return null;
                    }
                })
                .filter(boardType -> boardType != null)
                .collect(Collectors.toList());

        // 유효한 타입이 없으면 전체 조회
        if (boardTypes.isEmpty()) {
            return boardRepository.findAllByOrderByBoardNumDesc(pageable);
        }

        return boardRepository.findByBoardTypeInOrderByBoardNumDesc(boardTypes, pageable);
    }


    @Override
    public Board getBoard(Long boardNum) {
        return boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardNum));
    }
}
