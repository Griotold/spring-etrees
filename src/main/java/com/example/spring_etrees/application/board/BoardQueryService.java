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
        // 전체 조회 조건 체크
        if (shouldReturnAllBoards(types)) {
            return boardRepository.findAllByOrderByBoardNumDesc(pageable);
        }

        // String 타입을 BoardType 으로 변환
        List<BoardType> boardTypes = convertToBoardTypes(types);

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

    private boolean shouldReturnAllBoards(List<String> types) {
        return types == null || types.isEmpty() || types.contains("all") || types.contains("ALL");
    }

    private List<BoardType> convertToBoardTypes(List<String> types) {
        return types.stream()
                .map(this::parseToBoardType)
                .filter(boardType -> boardType != null)
                .collect(Collectors.toList());
    }

    private BoardType parseToBoardType(String type) {
        try {
            return BoardType.valueOf(type);
        } catch (IllegalArgumentException e) {
            // 잘못된 타입은 무시
            return null;
        }
    }
}
