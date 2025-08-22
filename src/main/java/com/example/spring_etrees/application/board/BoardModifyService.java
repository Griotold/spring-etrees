package com.example.spring_etrees.application.board;

import com.example.spring_etrees.application.board.provided.BoardCreator;
import com.example.spring_etrees.application.board.provided.BoardModifier;
import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardCreateRequest;
import com.example.spring_etrees.domain.board.BoardUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardModifyService implements BoardCreator, BoardModifier {

    private final BoardRepository boardRepository;

    @Override
    public Board createBoard(BoardCreateRequest request, String creator) {
        // 도메인 메서드 사용하여 게시글 생성
        Board board = Board.create(request, creator);

        // 저장 후 엔티티 반환
        return boardRepository.save(board);
    }

    @Override
    public Board updateBoard(Long boardNum, BoardUpdateRequest request, String modifier) {
        // 게시글 조회
        Board board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardNum));

        // 작성자 권한 검증
        validateBoardOwnership(board, modifier);

        // 도메인 메서드로 수정
        board.update(request, modifier);

        // 명시적 save 호출
        return boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Long boardNum, String requester) {
        // 게시글 조회
        Board board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardNum));

        // 작성자 권한 검증
        validateBoardOwnership(board, requester);

        // 게시글 삭제
        boardRepository.deleteById(boardNum);
    }

    /**
     * 게시글 소유권 검증
     */
    private void validateBoardOwnership(Board board, String requester) {
        if (!board.getCreator().equals(requester)) {
            throw new IllegalArgumentException("게시글 작성자만 수정/삭제할 수 있습니다.");
        }
    }
}