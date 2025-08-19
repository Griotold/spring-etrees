package com.example.spring_etrees.domain.board;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    void createBoard() {
        Board board = Board.create("타이틀", "코멘트");

        assertThat(board.getBoardType()).isEqualTo("1");
        assertThat(board.getCreator()).isEqualTo("SYSTEM");
        assertThat(board.getModifier()).isEqualTo("SYSTEM");
    }

    @Test
    void updateBoard() {
        Board board = Board.create("타이틀", "코멘트");

        board.update("수정된 타이틀", "수정된 코멘트");

        assertThat(board.getBoardTitle()).isEqualTo("수정된 타이틀");
        assertThat(board.getBoardComment()).isEqualTo("수정된 코멘트");
    }

}