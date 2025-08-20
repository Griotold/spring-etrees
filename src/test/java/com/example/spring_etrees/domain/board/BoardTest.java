package com.example.spring_etrees.domain.board;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    void createBoard() {
        BoardCreateRequest request = BoardFixture.createBoardCreateRequest();
        Board board = Board.create(request);

        assertThat(board.getBoardType()).isEqualTo(BoardType.GENERAL);
        assertThat(board.getCreator()).isEqualTo("SYSTEM");
        assertThat(board.getModifier()).isEqualTo("SYSTEM");
    }

    @Test
    void updateBoard() {
        BoardCreateRequest request = BoardFixture.createBoardCreateRequest();
        Board board = Board.create(request);

        BoardUpdateRequest updateRequest = BoardFixture.createBoardUpdateRequest();
        board.update(updateRequest);

        assertThat(board.getBoardTitle()).isEqualTo("수정된 제목");
        assertThat(board.getBoardComment()).isEqualTo("수정된 내용");
    }

}